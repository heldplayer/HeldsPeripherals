
package me.heldplayer.mods.HeldsPeripherals.common.tileentity;

import java.util.Random;

import me.heldplayer.mods.HeldsPeripherals.api.IElectricalFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.common.LogicHandler;
import me.heldplayer.mods.HeldsPeripherals.common.entity.EntityFireworkRocket;
import me.heldplayer.mods.HeldsPeripherals.common.inventory.RestrictedLiquidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import net.minecraftforge.oredict.OreDictionary;
import dan200.computer.api.IComputerAccess;

public class TileEntityFireworksLighter extends TileEntityHeldsPeripheral implements IInventory, IElectricalFireworksLighter, ITankContainer {

    private ItemStack[] inventory = new ItemStack[4];
    private RestrictedLiquidTank[] tanks = new RestrictedLiquidTank[3];
    public int timer = 0;
    public boolean coolDown = false;
    private boolean easterEgg = false;
    private Random rand = new Random();
    private String name;

    public TileEntityFireworksLighter() {
        for (int i = 0; i < tanks.length; i++) {
            tanks[i] = new RestrictedLiquidTank(null, 2000, 0, 0, this);
        }

        tanks[0].setAllowedType(LiquidDictionary.getLiquid("Molten Red Dye", 1000));
        tanks[0].setLiquid(LiquidDictionary.getLiquid("Molten Red Dye", 0));
        tanks[1].setAllowedType(LiquidDictionary.getLiquid("Molten Green Dye", 1000));
        tanks[1].setLiquid(LiquidDictionary.getLiquid("Molten Green Dye", 0));
        tanks[2].setAllowedType(LiquidDictionary.getLiquid("Molten Blue Dye", 1000));
        tanks[2].setLiquid(LiquidDictionary.getLiquid("Molten Blue Dye", 0));
    }

    private LiquidTank getTank(LiquidStack resource) {
        for (RestrictedLiquidTank tank : tanks) {
            if (tank != null) {
                if (tank.isAllowed(resource)) {
                    return tank;
                }
            }
        }

        return null;
    }

    public LiquidTank getTank(int index) {
        return this.tanks[index];
    }

    public int getAmount(int index) {
        if (this.tanks[index].getLiquid() != null) {
            return this.tanks[index].getLiquid().amount;
        }

        return 0;
    }

    public void setAmount(int index, int amount) {
        if (this.tanks[index].getLiquid() == null) {
            LiquidStack newStack = this.tanks[index].getAllowedType().copy();

            newStack.amount = amount;

            this.tanks[index].setLiquid(newStack);

            return;
        }

        this.tanks[index].getLiquid().amount = amount;
    }

    private boolean canInsert(int index, LiquidStack resource) {
        return this.tanks[index].isAllowed(resource);
    }

    // IInventory

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
        return this.isInvNameLocalized() ? this.name : "tile.HP.fireworksLighter.name";
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
        String type = "";

        switch (slot) {
        case 0:
            type = "dustGunpowder";
        break;
        case 1:
            type = "dyeRed";
        break;
        case 2:
            type = "dyeGreen";
        break;
        case 3:
            type = "dyeBlue";
        break;
        }

        if (type.isEmpty()) {
            return false;
        }

        int id = OreDictionary.getOreID(stack);
        int id2 = OreDictionary.getOreID(type);

        if (id == id2 && id > 0) {
            return true;
        }

        return false;
    }

    // ITankContainer

    @Override
    public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
        LiquidTank tank = this.getTank(resource);

        if (tank != null) {
            if (tank.getLiquid() == null) {
                return tank.fill(resource, doFill);
            }

            int remaining = tank.getCapacity() - tank.getLiquid().amount;

            if (remaining > 0) {
                int inserted = remaining > resource.amount ? resource.amount : remaining;

                LiquidStack insertedStack = resource.copy();

                insertedStack.amount = inserted;

                tank.fill(insertedStack, doFill);

                return inserted;
            }
        }

        return 0;
    }

    @Override
    public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
        if (canInsert(tankIndex, resource)) {
            LiquidTank tank = this.tanks[tankIndex];

            if (tank.getLiquid() == null) {
                return tank.fill(resource, doFill);
            }

            int remaining = tank.getCapacity() - tank.getLiquid().amount;

            if (remaining > 0) {
                int inserted = remaining > resource.amount ? resource.amount : remaining;

                LiquidStack insertedStack = resource.copy();

                insertedStack.amount = inserted;

                tank.fill(insertedStack, doFill);

                return inserted;
            }
        }

        return 0;
    }

    @Override
    public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        for (int i = 0; i < this.tanks.length; i++) {
            LiquidTank tank = this.tanks[i];

            LiquidStack stack = tank.getLiquid();

            if (stack != null && stack.amount > 0) {
                return tank.drain(maxDrain, doDrain);
            }
        }

        return null;
    }

    @Override
    public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
        LiquidTank tank = this.tanks[tankIndex];

        LiquidStack stack = tank.getLiquid();

        if (stack != null && stack.amount > 0) {
            return tank.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public ILiquidTank[] getTanks(ForgeDirection direction) {
        return this.tanks;
    }

    @Override
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
        return this.getTank(type);
    }

    // TileEntity

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList items = compound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < items.tagCount(); i++) {
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

        if (compound.hasKey("CustomName")) {
            this.name = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList items = new NBTTagList();

        for (int slot = 0; slot < this.inventory.length; slot++) {
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

        if (this.isInvNameLocalized()) {
            compound.setString("CustomName", this.name);
        }
    }

    // IPeripheral

    @Override
    public String getType() {
        return "FireworksLighter";
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "launchFirework" };
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
    public void attach(IComputerAccess computer) {}

    @Override
    public void detach(IComputerAccess computer) {}

    // IHeldsPeripheral

    @Override
    public void update() {
        if (this.timer > 0) {
            this.timer--;
        }

        if (this.timer == 0 && this.coolDown) {
            this.coolDown = false;
        }

        if (this.coolDown) {
            return;
        }

        ItemStack stack = this.inventory[1];

        if (stack != null) {
            if (this.tanks[0].getLiquid() == null) {
                tanks[0].setLiquid(LiquidDictionary.getLiquid("Molten Red Dye", 0));
            }

            if (this.tanks[0].getLiquid().amount <= 1800 && OreDictionary.getOreName(OreDictionary.getOreID(stack)).equalsIgnoreCase("dyeRed")) {
                this.tanks[0].getLiquid().amount += 200;
                stack.stackSize--;

                if (stack.stackSize <= 0) {
                    this.inventory[1] = null;
                }
            }
        }

        stack = this.inventory[2];

        if (stack != null) {
            if (this.tanks[1].getLiquid() == null) {
                tanks[1].setLiquid(LiquidDictionary.getLiquid("Molten Green Dye", 0));
            }

            if (this.tanks[1].getLiquid().amount <= 1800 && OreDictionary.getOreName(OreDictionary.getOreID(stack)).equalsIgnoreCase("dyeGreen")) {
                this.tanks[1].getLiquid().amount += 200;
                stack.stackSize--;

                if (stack.stackSize <= 0) {
                    this.inventory[2] = null;
                }
            }
        }

        stack = this.inventory[3];

        if (stack != null) {
            if (this.tanks[2].getLiquid() == null) {
                tanks[2].setLiquid(LiquidDictionary.getLiquid("Molten Blue Dye", 0));
            }

            if (this.tanks[2].getLiquid().amount <= 1800 && OreDictionary.getOreName(OreDictionary.getOreID(stack)).equalsIgnoreCase("dyeBlue")) {
                this.tanks[2].getLiquid().amount += 200;
                stack.stackSize--;

                if (stack.stackSize <= 0) {
                    this.inventory[3] = null;
                }
            }
        }

        if (this.easterEgg && this.rand.nextInt(10) == 0) {
            NBTTagCompound fireworks = new NBTTagCompound("Fireworks");
            NBTTagList list = new NBTTagList("Explosions");

            NBTTagCompound explosion = new NBTTagCompound();
            explosion.setByte("Type", (byte) 4);
            explosion.setBoolean("Trail", this.rand.nextBoolean());
            explosion.setBoolean("Flicker", this.rand.nextBoolean());
            explosion.setIntArray("Colors", new int[] { this.rand.nextInt(0xFFFFFF), this.rand.nextInt(0xFFFFFF) });
            explosion.setIntArray("FadeColors", new int[] { this.rand.nextInt(0xFFFFFF), this.rand.nextInt(0xFFFFFF) });

            list.appendTag(explosion);
            fireworks.setTag("Explosions", list);
            fireworks.setShort("Flight", (short) 20);

            stack = new ItemStack(Item.firework);

            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("Fireworks", fireworks);
            stack.setTagCompound(compound);

            EntityFireworkRocket entity = new EntityFireworkRocket(this.worldObj, this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, stack);
            this.worldObj.spawnEntityInWorld(entity);
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    // IElectricalFireworksLighter

    @Override
    public int getCoolingTime() {
        return this.timer;
    }

    @Override
    public boolean isCoolingDown() {
        return this.coolDown;
    }

    @Override
    public void setCoolingDown() {
        this.coolDown = true;
    }

    @Override
    public void increaseCoolingTime() {
        this.timer += 2;
    }

    @Override
    public void setCoolingTime(int time) {
        this.timer = time;
    }

    @Override
    public int getLiquidLevel(int tankId) {
        if (tankId < 0 || tankId > 3) {
            return 0;
        }
        if (this.tanks[tankId].getLiquid() != null) {
            return this.tanks[tankId].getLiquid().amount;
        }
        return 0;
    }

    @Override
    public void setLiquidLevel(int tankId, int amount) {
        if (tankId < 0 || tankId > 3) {
            return;
        }
        if (this.tanks[tankId].getLiquid() == null) {
            return;
        }

        this.tanks[tankId].getLiquid().amount = amount;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory[slot] = stack;
    }

    @Override
    public ItemStack getStack(int index) {
        return this.inventory[index];
    }

    @Override
    public int getOredictStack(String name) {
        if (name.equals("dyeRed")) {
            return 1;
        }
        if (name.equals("dyeGreen")) {
            return 2;
        }
        if (name.equals("dyeBlue")) {
            return 3;
        }

        return 0;
    }

    // TileEntityFireworksLighter

    public void toggleEasterEgg() {
        this.easterEgg = !this.easterEgg;
    }

}
