
package me.heldplayer.mods.HeldsPeripherals;

import java.util.Iterator;

import me.heldplayer.mods.HeldsPeripherals.network.Network;
import me.heldplayer.mods.HeldsPeripherals.packet.PacketHandler;
import me.heldplayer.util.HeldCore.HeldCoreMod;
import me.heldplayer.util.HeldCore.HeldCoreProxy;
import me.heldplayer.util.HeldCore.ModInfo;
import me.heldplayer.util.HeldCore.config.Config;
import me.heldplayer.util.HeldCore.config.ConfigValue;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;

@Mod(name = Objects.MOD_NAME, modid = Objects.MOD_ID, version = Objects.MOD_VERSION, dependencies = Objects.MOD_DEPENCIES)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { Objects.MOD_CHANNEL }, packetHandler = PacketHandler.class)
public class ModHeldsPeripherals extends HeldCoreMod {

    @Instance(value = Objects.MOD_ID)
    public static ModHeldsPeripherals instance;
    @SidedProxy(clientSide = Objects.CLIENT_PROXY, serverSide = Objects.SERVER_PROXY)
    public static CommonProxy proxy;

    // HeldCore Objects
    public static ConfigValue<Integer> blockEnderModemId;
    public static ConfigValue<Integer> blockMulti1Id;
    public static ConfigValue<Integer> itemEnderChargeId;
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
    // Molten dyes
    public static ConfigValue<?>[] blockMoltenDye;

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Objects.log = event.getModLog();

        // Config
        blockEnderModemId = new ConfigValue<Integer>("EnderModem", Configuration.CATEGORY_BLOCK, null, 2050, "The block ID for the Ender Modem");
        blockMulti1Id = new ConfigValue<Integer>("MultiBlock1", Configuration.CATEGORY_BLOCK, null, 2051, "The block ID for the Electrical Fireworks Lighter, Noise Maker and thaumic scanner");
        itemEnderChargeId = new ConfigValue<Integer>("EnderCharge", Configuration.CATEGORY_ITEM, null, 5230, "The item ID for the ender charge");
        fireworksEntityId = new ConfigValue<Integer>("FireworksEntityID", Configuration.CATEGORY_GENERAL, null, 160, "The entity ID for the custom fireworks entity");
        chargeYieldEnderPearl = new ConfigValue<Integer>("YieldEnderPearl", "charges", null, 40, "The amount of ender charges an ender pearl is worth");
        chargeYieldEyeOfEnder = new ConfigValue<Integer>("YieldEyeOfEnder", "charges", null, 60, "The amount of ender charges an eye of ender is worth");
        chargeYieldEnderPearlDust = new ConfigValue<Integer>("YieldEnderPearlDust", "charges", null, 40, "The amount of charges one ender pearl dust is worth (GregTech Compat)");
        chargeYieldEyeOfEnderDust = new ConfigValue<Integer>("YieldEyeOfEnderDust", "charges", null, 30, "The amount of charges one eye of ender dust is worth (GrehTech Compat)");
        chargeCostSend = new ConfigValue<Integer>("CostSend", "charges", null, 1, "The amount of charges that are required to send a text message");
        chargeCostTransport = new ConfigValue<Integer>("CostTransport", "charges", null, 5, "The amount of charges that are required to send matter");
        chargeCostostTransportFluid = new ConfigValue<Integer>("CostTransportFluid", "charges", null, 4, "The amount of charges that are required to send a fluid");
        enhancedFireworksEntity = new ConfigValue<Boolean>("EnhancedFireworks", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Determines whether fireworks launched by the Electrical Fireworks Lighter create grouped particles");
        enhancedEnderChargeRenderer = new ConfigValue<Boolean>("EnhancedEnderChargeRenderer", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Determines whether ender charges render with a charge amount counter");

        blockMoltenDye = new ConfigValue[16];
        for (int i = 0; i < blockMoltenDye.length; i++) {
            blockMoltenDye[i] = new ConfigValue<Integer>("MoltenDye" + i, Configuration.CATEGORY_BLOCK, null, 2060 + i, "The block ID for molten dye " + i);
        }

        this.config = new Config(event.getSuggestedConfigurationFile());
        this.config.addConfigKey(blockEnderModemId);
        this.config.addConfigKey(blockMulti1Id);
        this.config.addConfigKey(itemEnderChargeId);
        this.config.addConfigKey(fireworksEntityId);
        this.config.addConfigKey(chargeYieldEnderPearl);
        this.config.addConfigKey(chargeYieldEyeOfEnder);
        this.config.addConfigKey(chargeYieldEnderPearlDust);
        this.config.addConfigKey(chargeYieldEyeOfEnderDust);
        this.config.addConfigKey(chargeCostSend);
        this.config.addConfigKey(chargeCostTransport);
        this.config.addConfigKey(chargeCostostTransportFluid);
        this.config.addConfigKey(enhancedFireworksEntity);
        this.config.addConfigKey(enhancedEnderChargeRenderer);
        for (ConfigValue<?> setting : blockMoltenDye) {
            this.config.addConfigKey(setting);
        }

        super.preInit(event);
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        Network.clearModems();
    }

    public static void addCharge(ItemStack item, Integer value) {
        if (value <= 0) {
            return;
        }

        Iterator<ItemStack> iterator = CommonProxy.enderCharges.keySet().iterator();

        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();

            if (stack.itemID == item.itemID && stack.getItemDamage() == item.getItemDamage()) {
                return;
            }
        }

        CommonProxy.enderCharges.put(item, value);
    }

    public static int getChargeDelivered(ItemStack item) {
        Iterator<ItemStack> iterator = CommonProxy.enderCharges.keySet().iterator();

        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();

            if (stack.itemID == item.itemID && stack.getItemDamage() == item.getItemDamage()) {
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
    public ModInfo getModInfo() {
        return Objects.MOD_INFO;
    }

    @Override
    public HeldCoreProxy getProxy() {
        return proxy;
    }

}
