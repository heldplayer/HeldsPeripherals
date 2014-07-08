package me.heldplayer.mods.HeldsPeripherals.inventory;

import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityEnderModem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Iterator;

public class ContainerEnderModem extends Container {

    private TileEntityEnderModem modem;
    private EntityPlayer player;
    private int prevCharge;
    private int prevChargeCostSend;
    private int prevChargeCostTransport;
    private int prevChargeCostTransportFluid;
    private int prevAmount;
    private int prevFluidId;

    public ContainerEnderModem(IInventory playerInventory, IInventory tileInventory) {
        this.modem = (TileEntityEnderModem) tileInventory;
        this.player = ((InventoryPlayer) playerInventory).player;

        this.layoutContainer();

        this.modem.markDirty();
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
        crafter.sendProgressBarUpdate(this, 3, this.modem.chargeCostTransportFluid);

        FluidStack stack = this.modem.getTank().getFluid();

        crafter.sendProgressBarUpdate(this, 4, stack != null ? stack.amount : 0);
        crafter.sendProgressBarUpdate(this, 5, stack != null ? stack.getFluid().getID() : 0);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        Iterator iterator = this.crafters.iterator();

        FluidStack stack = this.modem.getTank().getFluid();

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
            if (this.prevChargeCostTransportFluid != this.modem.chargeCostTransportFluid) {
                crafter.sendProgressBarUpdate(this, 3, this.modem.chargeCostTransportFluid);
            }

            if (stack != null) {
                if (this.prevAmount != stack.amount) {
                    crafter.sendProgressBarUpdate(this, 4, stack.amount);
                }
                if (this.prevFluidId != stack.fluidID) {
                    crafter.sendProgressBarUpdate(this, 5, stack.fluidID);
                }
            } else {
                if (this.prevAmount > 0 || this.prevFluidId > 0) {
                    crafter.sendProgressBarUpdate(this, 4, 0);
                    crafter.sendProgressBarUpdate(this, 5, 0);
                }
            }
        }

        this.prevCharge = this.modem.charge;
        this.prevChargeCostSend = this.modem.chargeCostSend;
        this.prevChargeCostTransport = this.modem.chargeCostTransport;
        this.prevChargeCostTransportFluid = this.modem.chargeCostTransportFluid;

        if (stack != null) {
            this.prevAmount = stack.amount;
            this.prevFluidId = stack.fluidID;
        } else {
            this.prevAmount = 0;
            this.prevFluidId = 0;
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
            } else {
                if (((Slot) this.inventorySlots.get(0)).isItemValid(slotStack) && this.mergeItemStack(slotStack, 0, 3, false)) {
                    slot.onSlotChange(slotStack, stack);
                } else if (this.mergeItemStack(slotStack, 3, 4, false)) {
                    slot.onSlotChange(slotStack, stack);
                } else {
                    return null;
                }
            }

            if (slotStack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.stackSize == stack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, slotStack);
        }

        return stack;
    }

    @Override
    public void updateProgressBar(int barId, int barValue) {
        FluidStack stack = this.modem.getTank().getFluid();

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
                this.modem.chargeCostTransportFluid = this.prevChargeCostTransportFluid = barValue;
                break;
            case 4:
                if (barValue <= 0) {
                    this.modem.getTank().setFluid(null);
                } else if (stack != null) {
                    stack.amount = barValue;
                } else {
                    this.modem.getTank().setFluid(new FluidStack(0, barValue));
                }

                this.prevAmount = barValue;
                break;
            case 5:
                if (barValue <= 0) {
                    this.modem.getTank().setFluid(null);
                } else if (stack != null) {
                    int amount = stack.amount;
                    int fluidID = barValue;
                    this.modem.getTank().setFluid(new FluidStack(amount, fluidID));
                } else {
                    this.modem.getTank().setFluid(new FluidStack(barValue, 0));
                }

                this.prevFluidId = barValue;
                break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.modem.isUseableByPlayer(player);
    }

}
