package me.heldplayer.mods.HeldsPeripherals;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import me.heldplayer.mods.HeldsPeripherals.network.Network;
import me.heldplayer.mods.HeldsPeripherals.packet.HeldsPeripheralsPacket;
import me.heldplayer.mods.HeldsPeripherals.packet.Packet1PlaySound;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.specialattack.forge.core.ModInfo;
import net.specialattack.forge.core.SpACoreMod;
import net.specialattack.forge.core.SpACoreProxy;
import net.specialattack.forge.core.config.Config;
import net.specialattack.forge.core.config.ConfigCategory;
import net.specialattack.forge.core.config.ConfigValue;
import net.specialattack.forge.core.packet.PacketHandler;

import java.util.Iterator;

@Mod(name = Objects.MOD_NAME, modid = Objects.MOD_ID, dependencies = Objects.MOD_DEPENCIES, guiFactory = Objects.GUI_FACTORY)
public class ModHeldsPeripherals extends SpACoreMod {

    @Instance(value = Objects.MOD_ID)
    public static ModHeldsPeripherals instance;
    @SidedProxy(clientSide = Objects.CLIENT_PROXY, serverSide = Objects.SERVER_PROXY)
    public static CommonProxy proxy;

    // HeldCore Objects
    public static ConfigValue<Integer> fireworksEntityId;
    public static ConfigValue<Integer> chargeYieldEnderPearl;
    public static ConfigValue<Integer> chargeYieldEyeOfEnder;
    public static ConfigValue<Integer> chargeYieldEnderPearlDust;
    public static ConfigValue<Integer> chargeYieldEyeOfEnderDust;
    public static ConfigValue<Integer> chargeCostSend;
    public static ConfigValue<Integer> chargeCostTransport;
    public static ConfigValue<Integer> chargeCostostTransportFluid;
    public static ConfigValue<Boolean> enhancedFireworksEntity;
    public static ConfigValue<Boolean> enhancedEnderChargeRenderer;

    public static PacketHandler<HeldsPeripheralsPacket> packetHandler;

    public static void addCharge(ItemStack item, Integer value) {
        if (value <= 0) {
            return;
        }

        Iterator<ItemStack> iterator = CommonProxy.enderCharges.keySet().iterator();

        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();

            if (stack.getItem() == item.getItem() && stack.getItemDamage() == item.getItemDamage()) {
                return;
            }
        }

        CommonProxy.enderCharges.put(item, value);
    }

    public static int getChargeDelivered(ItemStack item) {
        Iterator<ItemStack> iterator = CommonProxy.enderCharges.keySet().iterator();

        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();

            if (stack.getItem() == item.getItem() && stack.getItemDamage() == item.getItemDamage()) {
                return CommonProxy.enderCharges.get(stack);
            }
        }

        return -1;
    }

    public static ItemStack getItemForCharge(int charge) {
        Iterator<ItemStack> iterator = CommonProxy.enderCharges.keySet().iterator();

        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();

            if (CommonProxy.enderCharges.get(stack) == charge) {
                return stack;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        ModHeldsPeripherals.packetHandler = new PacketHandler<HeldsPeripheralsPacket>(Objects.MOD_CHANNEL, Packet1PlaySound.class);

        // Config
        this.config = new Config(event.getSuggestedConfigurationFile());

        ConfigCategory<?> category = new ConfigCategory(Configuration.CATEGORY_GENERAL, "config.heldsperipherals.category.general", null, "General mod settings");
        this.config.addCategory(category);
        ModHeldsPeripherals.fireworksEntityId = new ConfigValue<Integer>("fireworksEntityId", "config.heldsperipherals.key.FireworksEntityID", null, 160, "The entity ID for the custom fireworks entity");
        ModHeldsPeripherals.enhancedFireworksEntity = new ConfigValue<Boolean>("enhancedFireworksEntity", "config.heldsperipherals.key.EnhancedFireworks", Side.CLIENT, Boolean.TRUE, "Determines whether fireworks launched by the Electrical Fireworks Lighter create grouped particles");
        ModHeldsPeripherals.enhancedEnderChargeRenderer = new ConfigValue<Boolean>("enhancedEnderChargeRenderer", "config.heldsperipherals.key.EnhancedEnderChargeRenderer", Side.CLIENT, Boolean.TRUE, "Determines whether ender charges render with a charge amount counter");
        category.addValue(ModHeldsPeripherals.fireworksEntityId);
        category.addValue(ModHeldsPeripherals.enhancedFireworksEntity);
        category.addValue(ModHeldsPeripherals.enhancedEnderChargeRenderer);

        category = new ConfigCategory("charges", "config.heldsperipherals.category.charges", null, "Ender Charge settings");
        this.config.addCategory(category);
        ModHeldsPeripherals.chargeYieldEnderPearl = new ConfigValue<Integer>("chargeYieldEnderPearl", "config.heldsperipherals.key.YieldEnderPearl", null, 40, "The amount of ender charges an ender pearl is worth");
        ModHeldsPeripherals.chargeYieldEyeOfEnder = new ConfigValue<Integer>("chargeYieldEyeOfEnder", "config.heldsperipherals.key.YieldEyeOfEnder", null, 60, "The amount of ender charges an eye of ender is worth");
        ModHeldsPeripherals.chargeYieldEnderPearlDust = new ConfigValue<Integer>("chargeYieldEnderPearlDust", "config.heldsperipherals.key.YieldEnderPearlDust", null, 40, "The amount of charges one ender pearl dust is worth (GregTech Compat)");
        ModHeldsPeripherals.chargeYieldEyeOfEnderDust = new ConfigValue<Integer>("chargeYieldEyeOfEnderDust", "config.heldsperipherals.key.YieldEyeOfEnderDust", null, 30, "The amount of charges one eye of ender dust is worth (GrehTech Compat)");
        ModHeldsPeripherals.chargeCostSend = new ConfigValue<Integer>("chargeCostSend", "config.heldsperipherals.key.CostSend", null, 1, "The amount of charges that are required to send a text message");
        ModHeldsPeripherals.chargeCostTransport = new ConfigValue<Integer>("chargeCostTransport", "config.heldsperipherals.key.CostTransport", null, 5, "The amount of charges that are required to send matter");
        ModHeldsPeripherals.chargeCostostTransportFluid = new ConfigValue<Integer>("chargeCostostTransportFluid", "config.heldsperipherals.key.CostTransportFluid", null, 4, "The amount of charges that are required to send fluid");
        category.addValue(ModHeldsPeripherals.chargeYieldEnderPearl);
        category.addValue(ModHeldsPeripherals.chargeYieldEyeOfEnder);
        category.addValue(ModHeldsPeripherals.chargeYieldEnderPearlDust);
        category.addValue(ModHeldsPeripherals.chargeYieldEyeOfEnderDust);
        category.addValue(ModHeldsPeripherals.chargeCostSend);
        category.addValue(ModHeldsPeripherals.chargeCostTransport);
        category.addValue(ModHeldsPeripherals.chargeCostostTransportFluid);

        super.preInit(event);
    }

    @Override
    public ModInfo getModInfo() {
        return Objects.MOD_INFO;
    }

    @Override
    public SpACoreProxy getProxy() {
        return ModHeldsPeripherals.proxy;
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        Network.clearModems();
    }

}
