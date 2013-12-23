
package me.heldplayer.mods.HeldsPeripherals.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNoInput extends Slot {

    public SlotNoInput(IInventory inventory, int index, int xPos, int yPos) {
        super(inventory, index, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

}
