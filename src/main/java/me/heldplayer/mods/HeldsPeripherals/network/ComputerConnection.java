package me.heldplayer.mods.HeldsPeripherals.network;

import dan200.computercraft.api.peripheral.IComputerAccess;

public class ComputerConnection {

    protected final IComputerAccess computer;

    public ComputerConnection(IComputerAccess computer) {
        this.computer = computer;
    }

    public boolean queueEvent(int target, String event, Object[] args) {
        if (this.computer.getID() == target) {
            this.computer.queueEvent(event, args);

            return true;
        }

        return false;
    }

    public boolean isConnected(int id) {
        return this.computer.getID() == id;
    }

    public boolean matches(Object obj) {
        return obj == this.computer;
    }
}
