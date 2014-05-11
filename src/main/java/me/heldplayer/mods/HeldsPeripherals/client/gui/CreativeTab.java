
package me.heldplayer.mods.HeldsPeripherals.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs {

    public Item displayItem;
    public int displayMeta;

    public CreativeTab(String label) {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return this.displayItem;
    }

    // Get icon meta
    @Override
    @SideOnly(Side.CLIENT)
    public int func_151243_f() {
        return this.displayMeta;
    }

}
