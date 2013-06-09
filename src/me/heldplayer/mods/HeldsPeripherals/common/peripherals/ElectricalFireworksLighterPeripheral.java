
package me.heldplayer.mods.HeldsPeripherals.common.peripherals;

import me.heldplayer.mods.HeldsPeripherals.api.IElectricalFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.common.LogicHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class ElectricalFireworksLighterPeripheral implements IHostedPeripheral, IElectricalFireworksLighter {

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

    @Override
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

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {}

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {}

    // IHeldsPeripheral

    @Override
    public World getWorld() {
        return this.turtle.getWorld();
    }

    @Override
    public int getX() {
        return (int) this.turtle.getPosition().xCoord;
    }

    @Override
    public int getY() {
        return (int) this.turtle.getPosition().yCoord;
    }

    @Override
    public int getZ() {
        return (int) this.turtle.getPosition().zCoord;
    }

    @Override
    public void setName(String name) {}

    // IElectricalFireworksLighter

    @Override
    public int getCoolingTime() {
        return this.launchCount;
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
        this.launchCount += 2;
    }

    @Override
    public void setCoolingTime(int time) {
        this.launchCount = time;
    }

    @Override
    public int getLiquidLevel(int tankId) {
        return 1000;
    }

    @Override
    public void setLiquidLevel(int tankId, int amount) {}

    @Override
    public ItemStack getStack(int slot) {
        return this.turtle.getSlotContents(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.turtle.setSlotContents(slot, stack);
    }

    @Override
    public int getOredictStack(String name) {
        int oreName = OreDictionary.getOreID(name);

        for (int i = 0; i < 16; i++) {
            ItemStack stack = this.turtle.getSlotContents(i);

            int stackName = OreDictionary.getOreID(stack);

            if (stackName == oreName && stackName > 0) {
                return i;
            }
        }

        return 0;
    }

}
