
package me.heldplayer.mods.HeldsPeripherals.item;

import java.util.List;

import me.heldplayer.mods.HeldsPeripherals.Assets;
import me.heldplayer.mods.HeldsPeripherals.Objects;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEnderCharge extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public ItemEnderCharge() {
        super();

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
            list.add(StatCollector.translateToLocalFormatted("item." + Assets.DOMAIN + "enderCharge.compact.text", stack.getItemDamage() + 1));
            //list.add("Contains " + (stack.getItemDamage() + 1) + " charges");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String name = "item." + Assets.DOMAIN + "enderCharge";

        if (stack.getItemDamage() > 0) {
            name = name + ".compact";
        }

        return name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icon = register.registerIcon("sugar");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
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
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        if (Objects.creativeTab.equals(tab)) {
            list.add(new ItemStack(item, 1, 1));
            list.add(new ItemStack(item, 1, 3));
            list.add(new ItemStack(item, 1, 4));
            list.add(new ItemStack(item, 1, 7));
            list.add(new ItemStack(item, 1, 9));
            list.add(new ItemStack(item, 1, 15));
            list.add(new ItemStack(item, 1, 19));
            list.add(new ItemStack(item, 1, 29));
            list.add(new ItemStack(item, 1, 31));
            list.add(new ItemStack(item, 1, 39));
            list.add(new ItemStack(item, 1, 49));
            list.add(new ItemStack(item, 1, 59));
            list.add(new ItemStack(item, 1, 63));
        }
    }

}
