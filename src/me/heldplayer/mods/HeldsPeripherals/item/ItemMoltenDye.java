
package me.heldplayer.mods.HeldsPeripherals.item;

import java.util.List;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.client.ClientProxy;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMoltenDye extends Item {

    @SideOnly(Side.CLIENT)
    private Icon icon;

    private static int[] colors = new int[] { 0xFFFFFF, 0xFF7F00, 0xFF00FF, 0x7F7FFF, 0xFFFF00, 0x00FF00, 0xFF7FFF, 0x3F3F3F, 0x7F7F7F, 0x007F7F, 0x8000FF, 0x00007F, 0x7F3F00, 0x007F00, 0xFF0000, 0x000000 };

    public ItemMoltenDye(int par1) {
        super(par1);

        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        switch (stack.getItemDamage()) {
        case 11:
            return "item.HP.moltenDye.blue";
        case 13:
            return "item.HP.moltenDye.green";
        case 14:
            return "item.HP.moltenDye.red";
        }

        return "item.HP.moltenDye";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icon = register.registerIcon("heldsperipherals:HP-molten-dye");

        String[] icons = new String[] { "gunpowder", "red", "green", "blue" };

        for (int i = 0; i < icons.length; i++) {
            ClientProxy.icons[i] = register.registerIcon("heldsperipherals:HP-back-" + icons[i]);
        }
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
        list.add(new ItemStack(itemId, 1, 11));
        list.add(new ItemStack(itemId, 1, 13));
        list.add(new ItemStack(itemId, 1, 14));
    }

}
