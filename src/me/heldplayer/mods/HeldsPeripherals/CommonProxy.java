
package me.heldplayer.mods.HeldsPeripherals;

import java.util.ArrayList;
import java.util.HashMap;

import me.heldplayer.mods.HeldsPeripherals.api.HeldsPeripheralAPI;
import me.heldplayer.mods.HeldsPeripherals.block.BlockMulti1;
import me.heldplayer.mods.HeldsPeripherals.block.BlockTransWorldModem;
import me.heldplayer.mods.HeldsPeripherals.client.BlockRendererHeldsPeripheral;
import me.heldplayer.mods.HeldsPeripherals.client.ClientProxy;
import me.heldplayer.mods.HeldsPeripherals.client.gui.CreativeTab;
import me.heldplayer.mods.HeldsPeripherals.entity.EntityFireworkRocket;
import me.heldplayer.mods.HeldsPeripherals.inventory.ContainerFireworksLauncher;
import me.heldplayer.mods.HeldsPeripherals.inventory.ContainerTransWorldModem;
import me.heldplayer.mods.HeldsPeripherals.item.ItemBlockMulti1;
import me.heldplayer.mods.HeldsPeripherals.item.ItemEnderCharge;
import me.heldplayer.mods.HeldsPeripherals.item.ItemMoltenDye;
import me.heldplayer.mods.HeldsPeripherals.peripherals.ElectricalFireworksLighterUpgrade;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityNoiseMaker;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityThaumicScanner;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityTransWorldModem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.turtle.api.TurtleAPI;

public class CommonProxy implements IGuiHandler {

    public static HashMap<ItemStack, Integer> enderCharges;

    @Instance("Thaumcraft")
    public static Object thaumcraft;

    public static boolean thaumcraftInstalled = false;

    public static int renderId;

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        //CCInjector.inject(event);

        enderCharges = new HashMap<ItemStack, Integer>();
    }

    public void init(FMLInitializationEvent event) {
        thaumcraftInstalled = thaumcraft != null;

        Objects.creativeTab = new CreativeTab("CCHeldsPeripherals");

        // Trans World Modem
        Objects.blockTransWorldModem = new BlockTransWorldModem(ModHeldsPeripherals.blockTransWorldModemId.getValue());
        GameRegistry.registerBlock(Objects.blockTransWorldModem, "computercraft.heldsperipherals.transworldmodem");
        Objects.blockTransWorldModem.setUnlocalizedName("HP.transWorldModem").setCreativeTab(Objects.creativeTab);

        GameRegistry.addRecipe(new ItemStack(ModHeldsPeripherals.blockTransWorldModemId.getValue(), 1, 0), "STS", "IPG", "SRS", 'S', Block.stone, 'T', Block.torchRedstoneActive, 'I', Item.ingotIron, 'P', Item.enderPearl, 'G', Item.ingotGold, 'R', Item.redstone);

        // Electrical Fireworks Lighter + Noise Maker + Thaumic Scanner
        Objects.blockMulti1 = new BlockMulti1(ModHeldsPeripherals.blockMulti1Id.getValue());
        GameRegistry.registerBlock(Objects.blockMulti1, ItemBlockMulti1.class, "computercraft.heldsperipherals.fireworkslighter");
        Objects.blockMulti1.setUnlocalizedName("HP.fireworksLighter").setCreativeTab(Objects.creativeTab);

        GameRegistry.addRecipe(new ItemStack(ModHeldsPeripherals.blockMulti1Id.getValue(), 1, 0), "hpG", "dDg", "ncf", 'h', new ItemStack(Item.skull, 1, OreDictionary.WILDCARD_VALUE), 'p', Item.paper, 'G', Item.gunpowder, 'd', Item.diamond, 'D', Block.dispenser, 'g', Item.glowstone, 'n', Item.goldNugget, 'c', Item.fireballCharge, 'f', Item.feather);
        GameRegistry.addRecipe(new ItemStack(ModHeldsPeripherals.blockMulti1Id.getValue(), 1, 4), "bbb", "rnr", "bBb", 'b', Block.brick, 'r', Item.redstone, 'n', Block.music, 'B', Block.bookShelf);

        if (thaumcraftInstalled) {
            Objects.log.info("Thaumcraft installed, adding recipes");

            ItemStack arcaneStoneBlock = ItemApi.getItem("blockInfusionWorkbench", 0);
            ItemStack goggles = ItemApi.getItem("itemGoggles", 0);

            ItemStack thaumometer = ItemApi.getItem("itemThaumometer", 0);
            ItemStack quicksilver = ItemApi.getItem("itemResource", 3);

            ItemStack thaumium = ItemApi.getItem("itemResource", 2);
            ItemStack visShard = ItemApi.getItem("itemShard", 4);

            ThaumcraftApi.addArcaneCraftingRecipe("", 5, new ItemStack(ModHeldsPeripherals.blockMulti1Id.getValue(), 1, 8), "SgS", "mqm", "isi", 'S', arcaneStoneBlock, 'g', goggles, 'm', thaumometer, 'q', quicksilver, 'i', thaumium, 's', visShard);
        }

        // Ender Charge/Ender Pearl Dust
        Objects.itemEnderCharge = new ItemEnderCharge(ModHeldsPeripherals.itemEnderChargeId.getValue());
        GameRegistry.registerItem(Objects.itemEnderCharge, "computercraft.heldsperipherals.endercharge");
        Objects.itemEnderCharge.setUnlocalizedName("item.HP.enderCharge.name").setCreativeTab(Objects.creativeTab);

        OreDictionary.registerOre("dustEnderCharge", Objects.itemEnderCharge);
        OreDictionary.registerOre("itemDustEnderCharge", Objects.itemEnderCharge);

        // Molten dyes
        //Objects.itemMoltenDye = new ItemMoltenDye(ModHeldsPeripherals.itemMoltenDyeId.getValue());
        //GameRegistry.registerItem(Objects.itemMoltenDye, "computercraft.heldsperipherals.moltendye");
        //Objects.itemMoltenDye.setUnlocalizedName("HP.moltenDye").setCreativeTab(Objects.creativeTab);

        ItemMoltenDye.registerFluids();

        // Etc...
        NetworkRegistry.instance().registerGuiHandler(ModHeldsPeripherals.instance, this);
        TileEntity.addMapping(TileEntityTransWorldModem.class, "CCtransWorldModem");
        TileEntity.addMapping(TileEntityFireworksLighter.class, "CCfireworksLighter");
        TileEntity.addMapping(TileEntityNoiseMaker.class, "CCnoiseMaker");
        TileEntity.addMapping(TileEntityThaumicScanner.class, "CCthaumicScanner");
        EntityRegistry.registerModEntity(EntityFireworkRocket.class, "CCFireworksRocketEntity", ModHeldsPeripherals.fireworksEntityId.getValue(), ModHeldsPeripherals.instance, 64, 10, true);

        try {
            TurtleAPI.registerUpgrade(new ElectricalFireworksLighterUpgrade());
        }
        catch (NoSuchMethodError e) {}
        GameRegistry.addRecipe(new RecipeEnderCharge());
        OreDictionary.registerOre("dustGunpowder", Item.gunpowder);

        Objects.creativeTab.displayStack = new ItemStack(Objects.blockTransWorldModem);
    }

    public void postInit(FMLPostInitializationEvent event) {
        renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderId, new BlockRendererHeldsPeripheral(renderId));

        ArrayList<ItemStack> dustEnderPearl = OreDictionary.getOres("dustEnderPearl");
        ArrayList<ItemStack> dustEnderEye = OreDictionary.getOres("dustEnderEye");

        HeldsPeripheralAPI.addCharges(dustEnderPearl, ModHeldsPeripherals.chargeYieldEnderPearlDust.getValue());
        HeldsPeripheralAPI.addCharges(dustEnderEye, ModHeldsPeripherals.chargeYieldEyeOfEnderDust.getValue());

        HeldsPeripheralAPI.addCharge(new ItemStack(Item.enderPearl), ModHeldsPeripherals.chargeYieldEnderPearl.getValue());
        HeldsPeripheralAPI.addCharge(new ItemStack(Item.eyeOfEnder), ModHeldsPeripherals.chargeYieldEyeOfEnder.getValue());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        try {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

            if (ID == 0) {
                if (tileEntity != null && (tileEntity instanceof TileEntityTransWorldModem)) {
                    return new ContainerTransWorldModem(player.inventory, ((TileEntityTransWorldModem) tileEntity));
                }
                else {
                    return null;
                }
            }

            if (ID == 1) {
                if (tileEntity != null && (tileEntity instanceof TileEntityFireworksLighter)) {
                    return new ContainerFireworksLauncher(player.inventory, ((TileEntityFireworksLighter) tileEntity));
                }
                else {
                    return null;
                }
            }

            return null;
        }
        catch (Exception ex) {
            System.out.println("Failed opening server GUI element.");

            ex.printStackTrace();

            return null;
        }
    }

    public static boolean doesItemHaveCharge(ItemStack stack) {
        int charge = ModHeldsPeripherals.getChargeDelivered(stack);
        if (charge > 0) {
            return true;
        }

        if (stack.itemID == Objects.itemEnderCharge.itemID) {
            return true;
        }

        return false;
    }

    public static boolean isItemOfType(ItemStack stack, String name) {
        if (stack == null || name == null) {
            return false;
        }

        int id = OreDictionary.getOreID(stack);
        int id2 = OreDictionary.getOreID(name);

        if (id == id2 && id > 0) {
            return true;
        }

        return false;
    }

    @ForgeSubscribe
    public void registerTextures(TextureStitchEvent.Pre event) {
        if (event.map.textureType == 0) {
            Objects.ICON_MOLTEN_DYE_STILL.icon = event.map.registerIcon("heldsperipherals:molten_dye_still");
            Objects.ICON_MOLTEN_DYE_FLOW.icon = event.map.registerIcon("heldsperipherals:molten_dye_flow");
        }
        else if (event.map.textureType == 1) {
            String[] icons = new String[] { "dust", "red", "green", "blue" };

            for (int i = 0; i < icons.length; i++) {
                ClientProxy.icons[i] = event.map.registerIcon("heldsperipherals:background_" + icons[i]);
            }
        }
    }

}
