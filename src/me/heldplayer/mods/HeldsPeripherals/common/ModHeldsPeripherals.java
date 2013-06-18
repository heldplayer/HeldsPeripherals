
package me.heldplayer.mods.HeldsPeripherals.common;

import java.io.File;
import java.util.Iterator;

import me.heldplayer.mods.HeldsPeripherals.common.network.Network;
import me.heldplayer.util.HeldCore.Updater;
import me.heldplayer.util.HeldCore.UsageReporter;
import me.heldplayer.util.HeldCore.config.Config;
import me.heldplayer.util.HeldCore.config.ConfigValue;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;

@Mod(name = Objects.MOD_NAME, modid = Objects.MOD_ID, version = Objects.MOD_VERSION, dependencies = Objects.MOD_DEPENCIES)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModHeldsPeripherals {

    @Instance(value = Objects.MOD_ID)
    public static ModHeldsPeripherals instance;
    @SidedProxy(clientSide = Objects.CLIENT_PROXY, serverSide = Objects.SERVER_PROXY)
    public static CommonProxy proxy;

    // HeldCore Objects
    private UsageReporter reporter;
    private Config config;
    public static ConfigValue<Integer> blockTransWorldModemId;
    public static ConfigValue<Integer> blockMulti1Id;
    public static ConfigValue<Integer> itemEnderChargeId;
    public static ConfigValue<Integer> itemMoltenDyeId;
    public static ConfigValue<Integer> fireworksEntityId;
    public static ConfigValue<Integer> chargeYieldEnderPearl;
    public static ConfigValue<Integer> chargeYieldEyeOfEnder;
    public static ConfigValue<Integer> chargeYieldEnderPearlDust;
    public static ConfigValue<Integer> chargeYieldEyeOfEnderDust;
    public static ConfigValue<Integer> chargeCostSend;
    public static ConfigValue<Integer> chargeCostTransport;
    public static ConfigValue<Integer> chargeCostostTransportLiquid;
    public static ConfigValue<Boolean> enhancedFireworksEntity;
    public static ConfigValue<Boolean> enhancedEnderChargeRenderer;
    // Config values for HeldCore
    public static ConfigValue<Boolean> silentUpdates;
    public static ConfigValue<Boolean> optOut;
    public static ConfigValue<String> modPack;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        File file = new File(event.getModConfigurationDirectory(), "HeldCore");

        if (!file.exists()) {
            file.mkdirs();
        }

        Objects.log = event.getModLog();

        // Config
        blockTransWorldModemId = new ConfigValue<Integer>("TransWorldModem", Configuration.CATEGORY_BLOCK, null, 2050, "The block ID for the Trans-World Modem");
        blockMulti1Id = new ConfigValue<Integer>("MultiBlock1", Configuration.CATEGORY_BLOCK, null, 2051, "The block ID for the Electrical Fireworks Lighter, Noise Maker and thaumic scanner");
        itemEnderChargeId = new ConfigValue<Integer>("EnderCharge", Configuration.CATEGORY_ITEM, null, 5230, "The item ID for the ender charge");
        itemMoltenDyeId = new ConfigValue<Integer>("MoltenDye", Configuration.CATEGORY_ITEM, null, 5231, "The item ID for the molten dyes");
        fireworksEntityId = new ConfigValue<Integer>("FireworksEntityID", Configuration.CATEGORY_GENERAL, null, 160, "The entity ID for the custom fireworks entity");
        chargeYieldEnderPearl = new ConfigValue<Integer>("YieldEnderPearl", "charges", null, 40, "The amount of ender charges an ender pearl is worth");
        chargeYieldEyeOfEnder = new ConfigValue<Integer>("YieldEyeOfEnder", "charges", null, 60, "The amount of ender charges an eye of ender is worth");
        chargeYieldEnderPearlDust = new ConfigValue<Integer>("YieldEnderPearlDust", "charges", null, 40, "The amount of charges one ender pearl dust is worth (GregTech Compat)");
        chargeYieldEyeOfEnderDust = new ConfigValue<Integer>("YieldEyeOfEnderDust", "charges", null, 30, "The amount of charges one eye of ender dust is worth (GrehTech Compat)");
        chargeCostSend = new ConfigValue<Integer>("CostSend", "charges", null, 1, "The amount of charges that are required to send a text message");
        chargeCostTransport = new ConfigValue<Integer>("CostTransport", "charges", null, 5, "The amount of charges that are required to send matter");
        chargeCostostTransportLiquid = new ConfigValue<Integer>("CostTransportLiquid", "charges", null, 4, "The amount of charges that are required to send a liquid");
        enhancedFireworksEntity = new ConfigValue<Boolean>("EnhancedFireworks", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Determines whether fireworks launched by the Electrical Fireworks Lighter create grouped particles");
        enhancedEnderChargeRenderer = new ConfigValue<Boolean>("EnhancedEnderChargeRenderer", Configuration.CATEGORY_GENERAL, Side.CLIENT, Boolean.TRUE, "Determines whether ender charges render with a charge amount counter");
        silentUpdates = new ConfigValue<Boolean>("silentUpdates", Configuration.CATEGORY_GENERAL, null, Boolean.TRUE, "Set this to true to hide update messages in the main menu");
        optOut = new ConfigValue<Boolean>("optOut", Configuration.CATEGORY_GENERAL, null, Boolean.FALSE, "Set this to true to opt-out from statistics gathering. If you are configuring this mod for a modpack, please leave it set to false");
        modPack = new ConfigValue<String>("modPack", Configuration.CATEGORY_GENERAL, null, "", "If this mod is running in a modpack, please set this config value to the name of the modpack");
        config = new Config(event.getSuggestedConfigurationFile());
        config.addConfigKey(blockTransWorldModemId);
        config.addConfigKey(blockMulti1Id);
        config.addConfigKey(itemEnderChargeId);
        config.addConfigKey(itemMoltenDyeId);
        config.addConfigKey(fireworksEntityId);
        config.addConfigKey(chargeYieldEnderPearl);
        config.addConfigKey(chargeYieldEyeOfEnder);
        config.addConfigKey(chargeYieldEnderPearlDust);
        config.addConfigKey(chargeYieldEyeOfEnderDust);
        config.addConfigKey(chargeCostSend);
        config.addConfigKey(chargeCostTransport);
        config.addConfigKey(chargeCostostTransportLiquid);
        config.addConfigKey(enhancedFireworksEntity);
        config.addConfigKey(enhancedEnderChargeRenderer);
        config.addConfigKey(silentUpdates);
        config.addConfigKey(optOut);
        config.addConfigKey(modPack);
        config.load();
        config.saveOnChange();

        reporter = new UsageReporter(Objects.MOD_ID, Objects.MOD_VERSION, modPack.getValue(), FMLCommonHandler.instance().getSide(), file);

        Updater.initializeUpdater(Objects.MOD_ID, Objects.MOD_VERSION, silentUpdates.getValue());

        proxy.preInit(event);
    }

    @Init
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        if (optOut.getValue()) {
            Thread thread = new Thread(reporter, Objects.MOD_ID + " usage reporter");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

        proxy.postInit(event);
    }

    @ServerStopping
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

    public static Block getBlock(Integer id) {
        switch (id) {
        case 0:
            return Objects.blockTransWorldModem;
        case 1:
            return Objects.blockMulti1;

        default:
            return null;
        }
    }

    public static Item getItem(Integer id) {
        switch (id) {
        case 0:
            return Objects.itemEnderCharge;
        case 1:
            return Objects.itemMoltenDye;

        default:
            return null;
        }
    }
}
