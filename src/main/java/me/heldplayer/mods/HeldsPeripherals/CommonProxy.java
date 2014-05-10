
package me.heldplayer.mods.HeldsPeripherals;

import java.util.ArrayList;
import java.util.HashMap;

import me.heldplayer.mods.HeldsPeripherals.api.HeldsPeripheralAPI;
import me.heldplayer.mods.HeldsPeripherals.block.BlockEnderModem;
import me.heldplayer.mods.HeldsPeripherals.block.BlockMoltenDye;
import me.heldplayer.mods.HeldsPeripherals.block.BlockMulti1;
import me.heldplayer.mods.HeldsPeripherals.client.gui.CreativeTab;
import me.heldplayer.mods.HeldsPeripherals.entity.EntityFireworkRocket;
import me.heldplayer.mods.HeldsPeripherals.fluids.FluidColored;
import me.heldplayer.mods.HeldsPeripherals.inventory.ContainerEnderModem;
import me.heldplayer.mods.HeldsPeripherals.inventory.ContainerFireworksLauncher;
import me.heldplayer.mods.HeldsPeripherals.item.ItemBlockMoltenDye;
import me.heldplayer.mods.HeldsPeripherals.item.ItemBlockMulti1;
import me.heldplayer.mods.HeldsPeripherals.item.ItemEnderCharge;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityEnderModem;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityNoiseMaker;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.specialattack.forge.core.SpACoreProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy extends SpACoreProxy implements IGuiHandler {

    public static HashMap<ItemStack, Integer> enderCharges;

    public static int renderId;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        enderCharges = new HashMap<ItemStack, Integer>();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        Objects.creativeTab = new CreativeTab("CCHeldsPeripherals");

        // Trans World Modem
        Objects.blockEnderModem = new BlockEnderModem();
        GameRegistry.registerBlock(Objects.blockEnderModem, Assets.DOMAIN + "transworldmodem");
        Objects.blockEnderModem.setBlockName(Assets.DOMAIN + "transWorldModem").setCreativeTab(Objects.creativeTab);

        GameRegistry.addRecipe(new ItemStack(Objects.blockEnderModem, 1, 0), "STS", "IPG", "SRS", 'S', Blocks.stone, 'T', Blocks.redstone_torch, 'I', Items.iron_ingot, 'P', Items.ender_pearl, 'G', Items.gold_ingot, 'R', Items.redstone);

        // Electrical Fireworks Lighter + Noise Maker + Thaumic Scanner
        Objects.blockMulti1 = new BlockMulti1();
        GameRegistry.registerBlock(Objects.blockMulti1, ItemBlockMulti1.class, Assets.DOMAIN + "fireworkslighter");
        Objects.blockMulti1.setBlockName("HP.fireworksLighter").setCreativeTab(Objects.creativeTab);

        GameRegistry.addRecipe(new ItemStack(Objects.blockMulti1, 1, 0), "hpG", "dDg", "ncf", 'h', new ItemStack(Items.skull, 1, OreDictionary.WILDCARD_VALUE), 'p', Items.paper, 'G', Items.gunpowder, 'd', Items.diamond, 'D', Blocks.dispenser, 'g', Items.glowstone_dust, 'n', Items.gold_nugget, 'c', Items.fire_charge, 'f', Items.feather);
        GameRegistry.addRecipe(new ItemStack(Objects.blockMulti1, 1, 4), "bbb", "rnr", "bBb", 'b', Blocks.brick_block, 'r', Items.redstone, 'n', Blocks.noteblock, 'B', Blocks.bookshelf);

        // Ender Charge/Ender Pearl Dust
        Objects.itemEnderCharge = new ItemEnderCharge();
        GameRegistry.registerItem(Objects.itemEnderCharge, Assets.DOMAIN + "endercharge");
        Objects.itemEnderCharge.setUnlocalizedName("item.HP.enderCharge.name").setCreativeTab(Objects.creativeTab);

        OreDictionary.registerOre("dustEnderCharge", Objects.itemEnderCharge);

        // Molten dyes
        FluidColored[] fluids = BlockMoltenDye.registerFluids();

        Objects.blocksMoltenDye = new BlockMoltenDye[fluids.length];
        for (int i = 0; i < fluids.length; i++) {
            Objects.blocksMoltenDye[i] = new BlockMoltenDye(fluids[i], Material.water);
            GameRegistry.registerBlock(Objects.blocksMoltenDye[i], ItemBlockMoltenDye.class, Assets.DOMAIN + "moltenDye" + i);
            Objects.blocksMoltenDye[i].setTickRate(5).setDensity(1).setCreativeTab(Objects.creativeTab);
            Objects.blocksMoltenDye[i].setBlockName(fluids[i].getUnlocalizedName());

            fluids[i].setBlock(Objects.blocksMoltenDye[i]);
        }

        // Etc...
        NetworkRegistry.INSTANCE.registerGuiHandler(ModHeldsPeripherals.instance, this);
        TileEntity.addMapping(TileEntityEnderModem.class, "CCtransWorldModem");
        TileEntity.addMapping(TileEntityFireworksLighter.class, "CCfireworksLighter");
        TileEntity.addMapping(TileEntityNoiseMaker.class, "CCnoiseMaker");
        EntityRegistry.registerModEntity(EntityFireworkRocket.class, "CCFireworksRocketEntity", ModHeldsPeripherals.fireworksEntityId.getValue(), ModHeldsPeripherals.instance, 64, 10, true);

        try {
            // FIXME: TurtleAPI.registerUpgrade(new ElectricalFireworksLighterUpgrade());
        }
        catch (NoSuchMethodError e) {}
        GameRegistry.addRecipe(new RecipeEnderCharge());
        OreDictionary.registerOre("dustGunpowder", Items.gunpowder);

        Objects.creativeTab.displayItem = Item.getItemFromBlock(Objects.blockEnderModem);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        ArrayList<ItemStack> dustEnderPearl = OreDictionary.getOres("dustEnderPearl");
        ArrayList<ItemStack> dustEnderEye = OreDictionary.getOres("dustEnderEye");

        HeldsPeripheralAPI.addCharges(dustEnderPearl, ModHeldsPeripherals.chargeYieldEnderPearlDust.getValue());
        HeldsPeripheralAPI.addCharges(dustEnderEye, ModHeldsPeripherals.chargeYieldEyeOfEnderDust.getValue());

        HeldsPeripheralAPI.addCharge(new ItemStack(Items.ender_pearl), ModHeldsPeripherals.chargeYieldEnderPearl.getValue());
        HeldsPeripheralAPI.addCharge(new ItemStack(Items.ender_eye), ModHeldsPeripherals.chargeYieldEyeOfEnder.getValue());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        try {
            TileEntity tileEntity = world.getTileEntity(x, y, z);

            if (ID == 0) {
                if (tileEntity != null && (tileEntity instanceof TileEntityEnderModem)) {
                    return new ContainerEnderModem(player.inventory, ((TileEntityEnderModem) tileEntity));
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

        if (stack.getItem() == Objects.itemEnderCharge) {
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

}
