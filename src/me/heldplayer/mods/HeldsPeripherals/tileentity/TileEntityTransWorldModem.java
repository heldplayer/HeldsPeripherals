
package me.heldplayer.mods.HeldsPeripherals.tileentity;

import me.heldplayer.mods.HeldsPeripherals.CommonProxy;
import me.heldplayer.mods.HeldsPeripherals.LogicHandler;
import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.api.ITransWorldModem;
import me.heldplayer.mods.HeldsPeripherals.network.Network;
import me.heldplayer.mods.HeldsPeripherals.network.Network.Modem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import dan200.computer.api.IComputerAccess;

public class TileEntityTransWorldModem extends TileEntityHeldsPeripheral implements ISidedInventory, ITransWorldModem, ITankContainer {

    private ItemStack[] inventory = new ItemStack[5];
    private LiquidTank[] tanks;
    private Modem modem;
    public int charge = 0;
    public int chargeCostSend;
    public int chargeCostTransport;
    public int chargeCostTransportLiquid;
    private String name;
    private static int[] slotsTop = new int[] { 3 };
    private static int[] slotsBottom = new int[] { 4 };
    private static int[] slotsSides = new int[] { 0, 1, 2 };

    public TileEntityTransWorldModem() {
        this.modem = Network.registerModem(this);
        this.tanks = new LiquidTank[] { new LiquidTank(4000) };
    }

    public void updateBlock() {
        int data = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) % 4;
        int origData = data;

        if (this.charge > 0) {
            data += 4;
        }

        for (int i = 0; i < 3; i++) {
            if (this.inventory[i] != null && this.inventory[i].stackSize > 0) {
                data += 8;
                break;
            }
        }

        if (origData != data) {
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, data, 3);
        }
    }

    public LiquidTank getTank() {
        return this.tanks[0];
    }

    // IInventory

    @Override
    public void onInventoryChanged() {
        if (this.worldObj.isRemote) {
            return;
        }

        this.updateBlock();

        super.onInventoryChanged();
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        if (this.inventory[index] != null) {
            ItemStack var3;

            if (this.inventory[index].stackSize <= amount) {
                var3 = this.inventory[index];
                this.inventory[index] = null;
                return var3;
            }
            else {
                var3 = this.inventory[index].splitStack(amount);

                if (this.inventory[index].stackSize == 0) {
                    this.inventory[index] = null;
                }

                return var3;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.inventory[index] != null) {
            ItemStack var2 = this.inventory[index];
            this.inventory[index] = null;
            return var2;
        }
        else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack newStack) {
        this.inventory[index] = newStack;

        if (newStack != null && newStack.stackSize > this.getInventoryStackLimit()) {
            newStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName() {
        return this.isInvNameLocalized() ? this.name : "tile.HP.transWorldModem.name";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    @Override
    public boolean isInvNameLocalized() {
        return this.name != null && this.name.length() > 0;
    }

    @Override
    public boolean isStackValidForSlot(int slot, ItemStack stack) {
        if (slot < 3) {
            return CommonProxy.doesItemHaveCharge(stack);
        }

        if (slot == 3) {
            return true;
        }

        return false;
    }

    // ISidedInventory

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 0) {
            return slotsBottom;
        }
        if (side == 1) {
            return slotsTop;
        }

        return slotsSides;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return this.isStackValidForSlot(slot, stack);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return true;
    }

    // ITankContainer

    @Override
    public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
        return this.tanks[0].fill(resource, doFill);
    }

    @Override
    public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
        return this.tanks[0].fill(resource, doFill);
    }

    @Override
    public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return this.tanks[0].drain(maxDrain, doDrain);
    }

    @Override
    public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
        return this.tanks[0].drain(maxDrain, doDrain);
    }

    @Override
    public ILiquidTank[] getTanks(ForgeDirection direction) {
        return new ILiquidTank[] { this.tanks[0] };
    }

    @Override
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
        return this.tanks[0];
    }

    // TileEntity

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList items = compound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound itemCompound = (NBTTagCompound) items.tagAt(i);
            byte slot = itemCompound.getByte("Slot");

            if (slot >= 0 && slot < this.inventory.length) {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
            }
        }

        NBTTagList tanks = compound.getTagList("Tanks");

        for (int i = 0; i < tanks.tagCount(); i++) {
            NBTTagCompound tankCompound = (NBTTagCompound) tanks.tagAt(i);
            byte index = tankCompound.getByte("Index");

            if (index >= 0 && index < this.tanks.length) {
                this.tanks[index].setLiquid(LiquidStack.loadLiquidStackFromNBT(tankCompound));
            }
        }

        this.charge = compound.getInteger("charge");

        if (compound.hasKey("CustomName")) {
            this.name = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList items = new NBTTagList();

        for (int slot = 0; slot < this.inventory.length; ++slot) {
            if (this.inventory[slot] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) slot);
                this.inventory[slot].writeToNBT(itemCompound);
                items.appendTag(itemCompound);
            }
        }

        compound.setTag("Items", items);

        NBTTagList tanks = new NBTTagList();

        for (int index = 0; index < this.tanks.length; index++) {
            if (this.tanks[index].getLiquid() != null) {
                NBTTagCompound tankCompound = new NBTTagCompound();
                tankCompound.setByte("Index", (byte) index);
                this.tanks[index].getLiquid().writeToNBT(tankCompound);
                tanks.appendTag(tankCompound);
            }
        }

        compound.setTag("Tanks", tanks);

        compound.setInteger("charge", this.charge);

        if (this.isInvNameLocalized()) {
            compound.setString("CustomName", this.name);
        }
    }

    // IPeripheral

    @Override
    public String getType() {
        return "TransWorldModem";
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "send", "getChargeLevel", "transport", "getInputOccupied", "getOutputOccupied", "transportLiquid", "getLiquidInfo" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
        return LogicHandler.callMethod(computer, method, arguments, this);
    }

    @Override
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer) {
        this.modem.attach(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        this.modem.detach(computer);
    }

    // IHeldsperipheral

    @Override
    public void update() {
        for (int i = 0; i < 3; i++) {
            if (this.inventory[i] != null && this.inventory[i].stackSize > 0) {
                ItemStack stack = this.inventory[i];

                if (stack.itemID == Objects.itemEnderCharge.itemID) {
                    int charge = stack.getItemDamage() + 1;

                    if (charge > 0) {
                        if (this.charge + charge <= 120) {
                            this.charge += charge;

                            stack.stackSize--;

                            if (stack.stackSize <= 0) {
                                this.inventory[i] = null;
                            }

                            break;
                        }
                    }
                }
                else {
                    int charge = ModHeldsPeripherals.getChargeDelivered(stack);

                    if (charge > 0) {
                        if (this.charge + charge <= 120) {
                            this.charge += charge;

                            stack.stackSize--;

                            if (stack.stackSize <= 0) {
                                this.inventory[i] = null;
                            }

                            break;
                        }
                    }
                }
            }
        }

        if (ModHeldsPeripherals.chargeCostSend.getValue() <= 0) {
            this.chargeCostSend = -1;
        }
        else {
            this.chargeCostSend = this.charge / ModHeldsPeripherals.chargeCostSend.getValue();
        }
        if (ModHeldsPeripherals.chargeCostTransport.getValue() <= 0) {
            this.chargeCostTransport = -1;
        }
        else {
            this.chargeCostTransport = this.charge / ModHeldsPeripherals.chargeCostTransport.getValue();
        }
        if (ModHeldsPeripherals.chargeCostostTransportLiquid.getValue() <= 0) {
            this.chargeCostTransportLiquid = -1;
        }
        else {
            this.chargeCostTransportLiquid = this.charge / ModHeldsPeripherals.chargeCostostTransportLiquid.getValue();
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    // ITransWorldModem

    @Override
    public int getChargeLevel() {
        return this.charge;
    }

    @Override
    public int getRemainingSends() {
        return this.chargeCostSend;
    }

    @Override
    public int getRemainingTransports() {
        return this.chargeCostTransport;
    }

    @Override
    public int getRemainingLiquidTransports() {
        return this.chargeCostTransportLiquid;
    }

    @Override
    public void decreaseCharge(int amount) {
        this.charge -= amount;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.inventory[slot] = stack;
    }

    @Override
    public LiquidTank getLiquidTank() {
        return this.tanks[0];
    }

}
