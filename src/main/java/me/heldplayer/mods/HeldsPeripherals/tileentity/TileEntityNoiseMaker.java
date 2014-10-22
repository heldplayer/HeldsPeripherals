package me.heldplayer.mods.HeldsPeripherals.tileentity;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.heldplayer.mods.HeldsPeripherals.LogicHandler;
import me.heldplayer.mods.HeldsPeripherals.api.INoiseMaker;

public class TileEntityNoiseMaker extends TileEntityHeldsPeripheral implements INoiseMaker {

    public int timer = 0;
    public boolean coolDown = false;

    // IPeripheral

    @Override
    public String getType() {
        return "NoiseMaker";
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "makeNoise" };
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
    public boolean equals(IPeripheral obj) {
        return this == obj; // TODO: Look into this
    }

    // IHeldsPeripheral

    @Override
    public void update() {
        if (this.timer > 0) {
            this.timer--;
        }

        if (this.timer == 0 && this.coolDown) {
            this.coolDown = false;
        }
    }

    @Override
    public void setName(String name) {
    }

    // INoiseMaker

    @Override
    public int getCoolingTime() {
        return this.timer;
    }

    @Override
    public void setCoolingDown() {
        this.coolDown = true;
    }

    @Override
    public boolean isCoolingDown() {
        return this.coolDown;
    }

    @Override
    public void increaseCoolingTime() {
        this.timer += 2;
    }

    @Override
    public void setCoolingTime(int time) {
        this.timer = time;
    }

}
