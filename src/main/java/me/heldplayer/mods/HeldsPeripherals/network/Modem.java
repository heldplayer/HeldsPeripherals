package me.heldplayer.mods.HeldsPeripherals.network;

import dan200.computercraft.api.peripheral.IComputerAccess;
import java.util.Iterator;
import java.util.LinkedList;
import me.heldplayer.mods.HeldsPeripherals.api.IEnderModem;
import me.heldplayer.mods.HeldsPeripherals.api.IModem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class Modem implements IModem {

    private final LinkedList<ComputerConnection> connections = new LinkedList<ComputerConnection>();
    private final IEnderModem modem;
    private boolean[] channelList = new boolean[65536];
    private int openChannels = 0;

    Modem(IEnderModem modem) {
        this.modem = modem;
    }

    @Override
    public void attach(IComputerAccess computer) {
        ComputerConnection connection = new ComputerConnection(computer);

        this.connections.add(connection);
    }

    @Override
    public void detach(IComputerAccess computer) {
        Iterator<ComputerConnection> i = this.connections.iterator();
        while (i.hasNext()) {
            ComputerConnection connection = i.next();

            if (connection.matches(computer)) {
                i.remove();
            }
        }
    }

    @Override
    public boolean isChannelOpen(int channel) {
        return this.channelList[channel];
    }

    @Override
    public boolean openChannel(int channel) {
        if (this.openChannels > 127) {
            return false;
        }
        if (!this.channelList[channel]) {
            this.channelList[channel] = true;
            this.openChannels++;
        }
        return true;
    }

    @Override
    public void closeChannel(int channel) {
        if (this.channelList[channel]) {
            this.channelList[channel] = false;
            this.openChannels--;
        }
    }

    @Override
    public void closeAllChannels() {
        for (int i = 0; i < this.channelList.length; i++) {
            this.channelList[i] = false;
        }
        this.openChannels = 0;
    }

    @Override
    public void transmit(int origin, int please, Object data) {
        if (this.channelList[origin]) {

            for (ComputerConnection connection : this.connections) {
                connection.queueEvent(origin, "modem_message", new Object[] { connection.computer.getAttachmentName(), origin, please, data, 0.0D });
            }
        }
    }

    @Override
    public boolean transmitSecure(int senderId, int dimension, int target, Object data) {
        for (ComputerConnection connection : this.connections) {
            boolean sent = connection.queueEvent(target, "modem_message_secure", new Object[] { connection.computer.getAttachmentName(), (double) senderId, (double) dimension, data });

            if (sent) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean transportItem(int senderId, int dimension, ItemStack sentStack, ItemStack newStack, int target) {
        for (ComputerConnection connection : this.connections) {
            if (connection.isConnected(target)) {
                ItemStack slot = this.modem.getStackInSlot(4);

                if (slot == null) {
                    connection.queueEvent(target, "modem_item", new Object[] { (double) senderId, (double) dimension });

                    this.modem.setStackInSlot(4, sentStack);

                    newStack.stackSize = 0;

                    return true;
                } else {
                    if (slot.getItem() == sentStack.getItem() && (!sentStack.getHasSubtypes() || sentStack.getItemDamage() == slot.getItemDamage()) && ItemStack.areItemStackTagsEqual(sentStack, slot)) {

                        if (slot.isStackable() && slot.stackSize < slot.getMaxStackSize()) {
                            connection.queueEvent(target, "modem_item", new Object[] { (double) senderId, (double) dimension });

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

    @Override
    public boolean transportFluid(int senderId, int dimension, FluidStack sentStack, FluidStack newStack, int target) {
        for (ComputerConnection connection : this.connections) {
            if (connection.isConnected(target)) {
                FluidTank tank = this.modem.getFluidTank();

                if (tank != null && tank.getFluid() == null) {
                    connection.queueEvent(target, "modem_fluid", new Object[] { (double) senderId, (double) dimension });

                    tank.setFluid(sentStack);

                    newStack.amount = 0;

                    return true;
                } else if (tank != null) {
                    FluidStack stack = tank.getFluid();

                    if (stack.isFluidEqual(sentStack)) {
                        int amount = tank.fill(sentStack, false);

                        if (amount > 0) {
                            connection.queueEvent(target, "transworld_liquid", new Object[] { (double) senderId, (double) dimension });

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
