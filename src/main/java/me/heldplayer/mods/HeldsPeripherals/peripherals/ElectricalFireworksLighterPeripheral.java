package me.heldplayer.mods.HeldsPeripherals.peripherals;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import me.heldplayer.mods.HeldsPeripherals.LogicHandler;
import me.heldplayer.mods.HeldsPeripherals.api.IElectricalFireworksLighter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ElectricalFireworksLighterPeripheral implements IPeripheral, IElectricalFireworksLighter {

    public int launchCount = 0;
    public boolean coolDown = false;
    private ITurtleAccess turtle;

    public ElectricalFireworksLighterPeripheral(ITurtleAccess turtle) {
        this.launchCount = 0;
        this.coolDown = false;
        this.turtle = turtle;
    }

    // IHostedPeripheral

    @Override
    public String getType() {
        return "FireworksLighter";
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "launchFirework" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        return LogicHandler.callMethod(computer, context, method, arguments, this);
    }

    //@Override
    //public boolean canAttachToSide(int side) {
    //return true;
    //}

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    @Override
    public boolean equals(IPeripheral other) {
        // TODO Auto-generated method stub
        return this == other;
    }    @Override
    public void update() {
        if (this.getWorld().isRemote) {
            return;
        }

        if (this.launchCount > 0) {
            this.launchCount--;
        }

        if (this.launchCount == 0 && this.coolDown) {
            this.coolDown = false;
        }

    }

    // IHeldsPeripheral

    @Override
    public int getCoolingTime() {
        return this.launchCount;
    }    @Override
    public World getWorld() {
        return this.turtle.getWorld();
    }

    @Override
    public void setCoolingDown() {
        this.coolDown = true;
    }    @Override
    public int getX() {
        return this.turtle.getPosition().posX;
    }

    @Override
    public boolean isCoolingDown() {
        return this.coolDown;
    }    @Override
    public int getY() {
        return this.turtle.getPosition().posY;
    }

    @Override
    public void increaseCoolingTime() {
        this.launchCount += 2;
    }    @Override
    public int getZ() {
        return this.turtle.getPosition().posZ;
    }

    @Override
    public void setCoolingTime(int time) {
        this.launchCount = time;
    }    @Override
    public void setName(String name) {
    }

    // IElectricalFireworksLighter

    @Override
    public int getFluidLevel(int tankId) {
        return 1000;
    }

    @Override
    public void setFluidLevel(int tankId, int amount) {
    }

    @Override
    public ItemStack getStack(int slot) {
        IInventory inventory = this.turtle.getInventory();
        return inventory.getStackInSlot(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        IInventory inventory = this.turtle.getInventory();
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public int getOredictStack(String name) {
        int oreName = OreDictionary.getOreID(name);

        IInventory inventory = this.turtle.getInventory();

        for (int i = 0; i < 16; i++) {
            ItemStack stack = inventory.getStackInSlot(i);

            int stackName = OreDictionary.getOreID(stack);

            if (stackName == oreName && stackName > 0) {
                return i;
            }
        }

        return 0;
    }













}
