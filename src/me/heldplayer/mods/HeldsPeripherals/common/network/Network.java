
package me.heldplayer.mods.HeldsPeripherals.common.network;

import java.util.ArrayList;

import me.heldplayer.mods.HeldsPeripherals.api.ITransWorldModem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import dan200.computer.api.IComputerAccess;

public class Network {
    private static ArrayList<Modem> modems = new ArrayList<Modem>();

    public static void clearModems() {
        modems.clear();

        modems.trimToSize();
    }

    public static Modem registerModem(ITransWorldModem modemTile) {
        Modem modem = new Modem(modemTile);

        modems.add(modem);

        return modem;
    }

    public static boolean send(int senderId, int dimension, int target, String args) {
        for (int i = 0; i < modems.size(); i++) {
            Modem modem = modems.get(i);

            if (modem != null) {
                if (modem.send(senderId, dimension, target, args)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ItemStack transport(int senderId, int dimension, ItemStack origStack, int target) {
        if (origStack != null && origStack.stackSize > 0 && origStack.itemID > 0) {
            ItemStack newStack = origStack.copy();
            ItemStack sentStack = origStack.copy();

            for (int i = 0; i < modems.size(); i++) {
                Modem modem = modems.get(i);

                if (modem != null) {
                    if (modem.transport(senderId, dimension, sentStack, newStack, target)) {
                        if (newStack.stackSize <= 0) {
                            newStack = null;
                        }

                        return newStack;
                    }
                }
            }
        }

        return origStack;
    }

    public static LiquidStack transport(int senderId, int dimension, LiquidStack origStack, int target) {
        if (origStack != null && origStack.amount > 0 && origStack.itemID > 0) {
            LiquidStack newStack = origStack.copy();
            LiquidStack sentStack = origStack.copy();

            for (int i = 0; i < modems.size(); i++) {
                Modem modem = modems.get(i);

                if (modem != null) {
                    if (modem.transport(senderId, dimension, sentStack, newStack, target)) {
                        if (newStack.amount <= 0) {
                            newStack = null;
                        }

                        return newStack;
                    }
                }
            }
        }

        return origStack;
    }

    public static class Modem {
        private final ArrayList<ComputerConnection> connections = new ArrayList<ComputerConnection>(6);
        private final ITransWorldModem modem;

        Modem(ITransWorldModem modem) {
            this.modem = modem;
        }

        public void attach(IComputerAccess computer) {
            ComputerConnection connection = new ComputerConnection(computer);

            this.connections.add(connection);
        }

        public void detach(IComputerAccess computer) {
            for (int i = 0; i < this.connections.size(); i++) {
                if (this.connections.get(i).matches(computer)) {
                    this.connections.remove(i);
                }
            }

            this.connections.remove(computer);
        }

        public boolean send(int senderId, int dimension, int target, String args) {
            for (int i = 0; i < this.connections.size(); i++) {
                ComputerConnection connection = this.connections.get(i);

                boolean sent = connection.queueEvent(target, "transworld_receive", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension), args });

                if (sent) {
                    return true;
                }
            }

            return false;
        }

        public boolean transport(int senderId, int dimension, ItemStack sentStack, ItemStack newStack, int target) {
            for (int i = 0; i < this.connections.size(); i++) {
                ComputerConnection connection = this.connections.get(i);

                if (connection.isConnected(target)) {
                    ItemStack slot = this.modem.getStackInSlot(4);

                    if (slot == null) {
                        connection.queueEvent(target, "transworld_item", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension) });

                        this.modem.setStackInSlot(4, sentStack);

                        newStack.stackSize = 0;

                        return true;
                    }
                    else {
                        if (slot != null && slot.itemID == sentStack.itemID && (!sentStack.getHasSubtypes() || sentStack.getItemDamage() == slot.getItemDamage()) && ItemStack.areItemStackTagsEqual(sentStack, slot)) {

                            if (slot.isStackable() && slot.stackSize < slot.getMaxStackSize()) {
                                connection.queueEvent(target, "transworld_item", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension) });

                                while (slot.stackSize < slot.getMaxStackSize() && newStack.stackSize > 0) {
                                    slot.stackSize++;
                                    newStack.stackSize--;
                                }

                                return true;
                            }

                            return false;
                        }
                    }
                }
            }

            return false;
        }

        public boolean transport(int senderId, int dimension, LiquidStack sentStack, LiquidStack newStack, int target) {
            for (int i = 0; i < this.connections.size(); i++) {
                ComputerConnection connection = this.connections.get(i);

                if (connection.isConnected(target)) {
                    LiquidTank tank = this.modem.getLiquidTank();

                    if (tank != null && tank.getLiquid() == null) {
                        connection.queueEvent(target, "transworld_liquid", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension) });

                        //this.modem

                        tank.setLiquid(sentStack);

                        newStack.amount = 0;

                        return true;
                    }
                    else if (tank != null) {
                        LiquidStack stack = tank.getLiquid();

                        if (stack != null && stack.isLiquidEqual(sentStack)) {
                            int amount = tank.fill(sentStack, false);

                            if (amount > 0) {
                                connection.queueEvent(target, "transworld_liquid", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension) });

                                tank.fill(sentStack, true);

                                newStack.amount -= amount;

                                if (newStack.amount < 0) {
                                    newStack.amount = 0;
                                }

                                return true;
                            }

                            return false;
                        }
                    }
                }
            }

            return false;
        }
    }
}
