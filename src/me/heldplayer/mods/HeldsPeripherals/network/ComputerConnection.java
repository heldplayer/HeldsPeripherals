
package me.heldplayer.mods.HeldsPeripherals.network;

import dan200.computer.api.IComputerAccess;

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
        if (this.computer.getID() == id) {
            return true;
        }

        return false;
    }

    public boolean matches(Object obj) {
        if (obj == this.computer) {
            return true;
        }

        return false;
    }
}
