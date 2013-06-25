
package me.heldplayer.mods.HeldsPeripherals.inventory;

import java.util.Iterator;

import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityTransWorldModem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;

public class ContainerTransWorldModem extends Container {
    private TileEntityTransWorldModem modem;
    private EntityPlayer player;
    private int prevCharge;
    private int prevChargeCostSend;
    private int prevChargeCostTransport;
    private int prevChargeCostTransportLiquid;
    private int prevAmount;
    private int prevItemId;
    private int prevItemMeta;

    public ContainerTransWorldModem(IInventory playerInventory, IInventory tileInventory) {
        this.modem = (TileEntityTransWorldModem) tileInventory;
        this.player = ((InventoryPlayer) playerInventory).player;

        this.layoutContainer();

        this.modem.onInventoryChanged();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.modem.isUseableByPlayer(player);
    }

    private void layoutContainer() {
        this.addSlotToContainer(new SlotEnderChargeInput(this.modem, 0, 8, 17));
        this.addSlotToContainer(new SlotEnderChargeInput(this.modem, 1, 8, 35));
        this.addSlotToContainer(new SlotEnderChargeInput(this.modem, 2, 8, 53));
        this.addSlotToContainer(new Slot(this.modem, 3, 62, 53));
        this.addSlotToContainer(new SlotNoInput(this.modem, 4, 98, 53));

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
    public void addCraftingToCrafters(ICrafting crafter) {
        this.modem.getStackInSlot(0);

        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, this.modem.charge);
        crafter.sendProgressBarUpdate(this, 1, this.modem.chargeCostSend);
        crafter.sendProgressBarUpdate(this, 2, this.modem.chargeCostTransport);
        crafter.sendProgressBarUpdate(this, 3, this.modem.chargeCostTransportLiquid);

        LiquidStack stack = this.modem.getTank().getLiquid();

        crafter.sendProgressBarUpdate(this, 4, stack != null ? stack.amount : 0);
        crafter.sendProgressBarUpdate(this, 5, stack != null ? stack.itemID : 0);
        crafter.sendProgressBarUpdate(this, 6, stack != null ? stack.itemMeta : 0);
    }

    @Override
    public void updateProgressBar(int barId, int barValue) {
        LiquidStack stack = this.modem.getTank().getLiquid();

        switch (barId) {
        case 0:
            this.modem.charge = this.prevCharge = barValue;
        break;
        case 1:
            this.modem.chargeCostSend = this.prevChargeCostSend = barValue;
        break;
        case 2:
            this.modem.chargeCostTransport = this.prevChargeCostTransport = barValue;
        break;
        case 3:
            this.modem.chargeCostTransportLiquid = this.prevChargeCostTransportLiquid = barValue;
        break;
        case 4:
            if (barValue <= 0) {
                this.modem.getTank().setLiquid(null);
            }
            else if (stack != null) {
                stack.amount = barValue;
            }
            else {
                this.modem.getTank().setLiquid(new LiquidStack(0, barValue));
            }

            this.prevAmount = barValue;
        break;
        case 5:
            if (barValue <= 0) {
                this.modem.getTank().setLiquid(null);
            }
            else if (stack != null) {
                int amount = stack.amount;
                int itemId = barValue;
                int meta = stack.itemMeta;
                this.modem.getTank().setLiquid(new LiquidStack(amount, itemId, meta));
            }
            else {
                this.modem.getTank().setLiquid(new LiquidStack(barValue, 0));
            }

            this.prevItemId = barValue;
        break;
        case 6:
            if (stack != null) {
                int amount = stack.amount;
                int itemId = stack.itemID;
                int meta = barValue;
                this.modem.getTank().setLiquid(new LiquidStack(amount, itemId, meta));
            }
            else {
                this.modem.getTank().setLiquid(new LiquidStack(0, 0, barValue));
            }

            this.prevItemMeta = barValue;
        break;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        Iterator iterator = this.crafters.iterator();

        LiquidStack stack = this.modem.getTank().getLiquid();

        while (iterator.hasNext()) {
            ICrafting crafter = (ICrafting) iterator.next();

            if (this.prevCharge != this.modem.charge) {
                crafter.sendProgressBarUpdate(this, 0, this.modem.charge);
            }
            if (this.prevChargeCostSend != this.modem.chargeCostSend) {
                crafter.sendProgressBarUpdate(this, 1, this.modem.chargeCostSend);
            }
            if (this.prevChargeCostTransport != this.modem.chargeCostTransport) {
                crafter.sendProgressBarUpdate(this, 2, this.modem.chargeCostTransport);
            }
            if (this.prevChargeCostTransportLiquid != this.modem.chargeCostTransportLiquid) {
                crafter.sendProgressBarUpdate(this, 3, this.modem.chargeCostTransportLiquid);
            }

            if (stack != null) {
                if (this.prevAmount != stack.amount) {
                    crafter.sendProgressBarUpdate(this, 4, stack.amount);
                }
                if (this.prevItemId != stack.itemID) {
                    crafter.sendProgressBarUpdate(this, 5, stack.itemID);
                }
                if (this.prevItemMeta != stack.itemMeta) {
                    crafter.sendProgressBarUpdate(this, 6, stack.itemMeta);
                }
            }
            else {
                if (this.prevAmount > 0 || this.prevItemId > 0) {
                    crafter.sendProgressBarUpdate(this, 4, 0);
                    crafter.sendProgressBarUpdate(this, 5, 0);
                    crafter.sendProgressBarUpdate(this, 6, 0);
                }
            }
        }

        this.prevCharge = this.modem.charge;
        this.prevChargeCostSend = this.modem.chargeCostSend;
        this.prevChargeCostTransport = this.modem.chargeCostTransport;
        this.prevChargeCostTransportLiquid = this.modem.chargeCostTransportLiquid;

        if (stack != null) {
            this.prevAmount = stack.amount;
            this.prevItemId = stack.itemID;
            this.prevItemMeta = stack.itemMeta;
        }
        else {
            this.prevAmount = 0;
            this.prevItemId = 0;
            this.prevItemMeta = 0;
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack stack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (slotId < 5) {
                if (!this.mergeItemStack(slotStack, 5, 41, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, stack);
            }
            else {
                if (((Slot) this.inventorySlots.get(0)).isItemValid(slotStack) && this.mergeItemStack(slotStack, 0, 3, false)) {
                    slot.onSlotChange(slotStack, stack);
                }
                else if (this.mergeItemStack(slotStack, 3, 4, false)) {
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
