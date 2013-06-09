
package me.heldplayer.mods.HeldsPeripherals.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {

    public ItemStack displayStack;

    public CreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return this.displayStack;
    }

}
