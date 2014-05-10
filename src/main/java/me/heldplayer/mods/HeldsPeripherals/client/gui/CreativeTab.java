
package me.heldplayer.mods.HeldsPeripherals.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab extends CreativeTabs {

    public Item displayItem;
    public int displayMeta;

    public CreativeTab(String label) {
        super(label);
    }

    @Override
    public Item getTabIconItem() {
        return this.displayItem;
    }

    // Get icon meta
    @Override
    public int func_151243_f() {
        return this.displayMeta;
    }

}
