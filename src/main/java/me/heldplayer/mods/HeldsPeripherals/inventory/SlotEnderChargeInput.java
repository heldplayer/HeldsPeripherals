package me.heldplayer.mods.HeldsPeripherals.inventory;

import me.heldplayer.mods.HeldsPeripherals.CommonProxy;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnderChargeInput extends Slot {

    public SlotEnderChargeInput(IInventory inventory, int index, int xPos, int yPos) {
        super(inventory, index, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return CommonProxy.doesItemHaveCharge(stack);
    }

}
