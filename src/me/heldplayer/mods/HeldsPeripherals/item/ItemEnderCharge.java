
package me.heldplayer.mods.HeldsPeripherals.item;

import java.util.List;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEnderCharge extends Item {

    @SideOnly(Side.CLIENT)
    private Icon icon;

    public ItemEnderCharge(int par1) {
        super(par1);

        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int damage = stack.getItemDamage();

        int red = 0x25;
        int green = 0x44 + 0x40 * 65 / (96 - damage);
        int blue = 0x44 + 0x30 * 65 / (96 - damage);

        return (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);

        if (stack.getItemDamage() > 0) {
            list.add("Contains " + (stack.getItemDamage() + 1) + " charges");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String name = "item.HP.enderCharge";

        if (stack.getItemDamage() > 0) {
            name = name + ".compact";
        }

        return name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icon = register.registerIcon("sugar");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        return this.icon;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemId, 1, 0));
        if (Objects.creativeTab.equals(tab)) {
            list.add(new ItemStack(itemId, 1, 1));
            list.add(new ItemStack(itemId, 1, 3));
            list.add(new ItemStack(itemId, 1, 4));
            list.add(new ItemStack(itemId, 1, 7));
            list.add(new ItemStack(itemId, 1, 9));
            list.add(new ItemStack(itemId, 1, 15));
            list.add(new ItemStack(itemId, 1, 19));
            list.add(new ItemStack(itemId, 1, 29));
            list.add(new ItemStack(itemId, 1, 31));
            list.add(new ItemStack(itemId, 1, 39));
            list.add(new ItemStack(itemId, 1, 49));
            list.add(new ItemStack(itemId, 1, 59));
            list.add(new ItemStack(itemId, 1, 63));
        }
    }

}
