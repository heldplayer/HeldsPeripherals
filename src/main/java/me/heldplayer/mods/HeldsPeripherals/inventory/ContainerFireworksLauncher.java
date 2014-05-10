
package me.heldplayer.mods.HeldsPeripherals.inventory;

import java.util.Iterator;

import me.heldplayer.mods.HeldsPeripherals.client.ClientProxy;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerFireworksLauncher extends Container {
    private TileEntityFireworksLighter lighter;
    private EntityPlayer player;
    private int prevRedAmount;
    private int prevGreenAmount;
    private int prevBlueAmount;

    public ContainerFireworksLauncher(IInventory playerInventory, IInventory tileInventory) {
        this.lighter = (TileEntityFireworksLighter) tileInventory;
        this.player = ((InventoryPlayer) playerInventory).player;

        this.layoutContainer();

        this.lighter.markDirty();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.lighter.isUseableByPlayer(player);
    }

    private void layoutContainer() {
        this.addSlotToContainer(ClientProxy.setIcon(new SlotOreDictionary(this.lighter, 0, 26, 54, "dustGunpowder"), 0));
        this.addSlotToContainer(ClientProxy.setIcon(new SlotOreDictionary(this.lighter, 1, 62, 54, "dyeRed"), 1));
        this.addSlotToContainer(ClientProxy.setIcon(new SlotOreDictionary(this.lighter, 2, 98, 54, "dyeGreen"), 2));
        this.addSlotToContainer(ClientProxy.setIcon(new SlotOreDictionary(this.lighter, 3, 134, 54, "dyeBlue"), 3));

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(this.player.inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(this.player.inventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting player) {
        super.addCraftingToCrafters(player);
        player.sendProgressBarUpdate(this, 0, this.lighter.getAmount(0));
        player.sendProgressBarUpdate(this, 1, this.lighter.getAmount(1));
        player.sendProgressBarUpdate(this, 2, this.lighter.getAmount(2));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int barId, int barValue) {
        this.lighter.setAmount(barId, barValue);
        switch (barId) {
        case 0:
            this.prevRedAmount = barValue;
        break;
        case 1:
            this.prevGreenAmount = barValue;
        break;
        case 2:
            this.prevBlueAmount = barValue;
        break;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        Iterator iterator = this.crafters.iterator();

        while (iterator.hasNext()) {
            ICrafting crafter = (ICrafting) iterator.next();

            if (this.prevRedAmount != this.lighter.getAmount(0)) {
                crafter.sendProgressBarUpdate(this, 0, this.lighter.getAmount(0));
            }
            if (this.prevGreenAmount != this.lighter.getAmount(1)) {
                crafter.sendProgressBarUpdate(this, 1, this.lighter.getAmount(1));
            }
            if (this.prevBlueAmount != this.lighter.getAmount(2)) {
                crafter.sendProgressBarUpdate(this, 2, this.lighter.getAmount(2));
            }
        }

        this.prevRedAmount = this.lighter.getAmount(0);
        this.prevGreenAmount = this.lighter.getAmount(1);
        this.prevBlueAmount = this.lighter.getAmount(2);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack stack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (slotId < 4) {
                if (!this.mergeItemStack(slotStack, 4, 40, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, stack);
            }
            else {
                if (((Slot) this.inventorySlots.get(0)).isItemValid(slotStack) && this.mergeItemStack(slotStack, 0, 1, false)) {
                    slot.onSlotChange(slotStack, stack);
                }
                else if (((Slot) this.inventorySlots.get(1)).isItemValid(slotStack) && this.mergeItemStack(slotStack, 1, 2, false)) {
                    slot.onSlotChange(slotStack, stack);
                }
                else if (((Slot) this.inventorySlots.get(2)).isItemValid(slotStack) && this.mergeItemStack(slotStack, 2, 3, false)) {
                    slot.onSlotChange(slotStack, stack);
                }
                else if (((Slot) this.inventorySlots.get(3)).isItemValid(slotStack) && this.mergeItemStack(slotStack, 3, 4, false)) {
                    slot.onSlotChange(slotStack, stack);
                }
                else {
                    return null;
                }
            }

            if (slotStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            }
            else {
                slot.onSlotChanged();
            }

            if (slotStack.stackSize == stack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, slotStack);
        }

        return stack;
    }

}
