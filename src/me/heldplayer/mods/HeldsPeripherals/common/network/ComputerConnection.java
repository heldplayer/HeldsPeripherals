
package me.heldplayer.mods.HeldsPeripherals.common.network;

import xfel.mods.cccable.api.IPeripheralCable;
import dan200.computer.api.IComputerAccess;

public class ComputerConnection {
    private final IComputerAccess computer;
    private final IPeripheralCable cable;
    private final boolean isDirect;
    private final int colorTag;

    public ComputerConnection(IComputerAccess computer) {
        this.isDirect = true;
        this.computer = computer;
        this.cable = null;
        this.colorTag = -1;
    }

    public ComputerConnection(IPeripheralCable cable, int colorTag) {
        this.isDirect = false;
        this.computer = null;
        this.cable = cable;
        this.colorTag = colorTag;
    }

    public boolean queueEvent(int target, String event, Object[] args) {
        if (this.isDirect) {
            if (this.computer.getID() == target) {
                this.computer.queueEvent(event, args);

                return true;
            }
        }
        else if (this.colorTag >= 0) {
            IComputerAccess[] computers = this.cable.getComputers();

            for (IComputerAccess computer : computers) {
                if (computer.getID() == target) {
                    computer.queueEvent(event, args);

                    return true;
                }
            }
        }

        return false;
    }

    public boolean isConnected(int id) {
        if (this.isDirect) {
            if (this.computer.getID() == id) {
                return true;
            }
        }
        else if (this.colorTag >= 0) {
            IComputerAccess[] computers = this.cable.getComputers();

            for (IComputerAccess computer : computers) {
                if (computer.getID() == id) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean matches(Object obj) {
        if (obj == this.computer || obj == this.cable) {
            return true;
        }

        return false;
    }
}
