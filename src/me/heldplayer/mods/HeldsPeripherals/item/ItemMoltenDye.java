
package me.heldplayer.mods.HeldsPeripherals.item;

import java.util.List;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.fluids.FluidColored;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMoltenDye extends Item {

    @SideOnly(Side.CLIENT)
    private Icon icon;

    private static int[] colors = new int[] { 0xFFFFFF, 0xFF7F00, 0xFF00FF, 0x7F7FFF, 0xFFFF00, 0x00FF00, 0xFF7FFF, 0x7F7F7F, 0xBEBEBE, 0x007F7F, 0x8000FF, 0x00007F, 0x7F3F00, 0x007F00, 0xFF0000, 0x3F3F3F };
    private static String[] color_names = new String[] { "white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "gray", "lightGray", "cyan", "purple", "blue", "brown", "green", "red", "black" };

    public static void registerFluids() {
        for (int i = 0; i < colors.length && i < color_names.length; i++) {
            FluidColored fluid = new FluidColored("molten " + color_names[i] + " dye");
            fluid.setUnlocalizedName("dye.molten." + color_names[i]);
            fluid.setIcons(Objects.ICON_MOLTEN_DYE_STILL, Objects.ICON_MOLTEN_DYE_FLOW);
            fluid.setColor(colors[i]);
            FluidRegistry.registerFluid(fluid);
        }
    }

    public ItemMoltenDye(int par1) {
        super(par1);

        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();

        if (meta < 0 || meta > 16) {
            return "item.HP.moltenDye";
        }

        return "item.HP.moltenDye." + color_names[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icon = register.registerIcon("heldsperipherals:molten_dye_still");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        return this.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return colors[stack.getItemDamage() % 16];
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        if (Objects.creativeTab.equals(CreativeTabs.tabRedstone)) {
            return new CreativeTabs[] { Objects.creativeTab };
        }

        return new CreativeTabs[] { Objects.creativeTab, CreativeTabs.tabRedstone };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab() {
        return Objects.creativeTab;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs tabs, List list) {
        for (int i = 0; i < colors.length; i++) {
            list.add(new ItemStack(itemId, 1, i));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        list.add("Very hot and sticky, what a mess..");
        super.addInformation(stack, player, list, advanced);
    }

}
