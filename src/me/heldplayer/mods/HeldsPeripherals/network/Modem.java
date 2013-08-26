
package me.heldplayer.mods.HeldsPeripherals.network;

import java.util.Iterator;
import java.util.LinkedList;

import me.heldplayer.mods.HeldsPeripherals.api.IEnderModem;
import me.heldplayer.mods.HeldsPeripherals.api.IModem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import dan200.computer.api.IComputerAccess;

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
        Iterator<ComputerConnection> i = connections.iterator();
        while (i.hasNext()) {
            ComputerConnection connection = i.next();

            if (connection.matches(computer)) {
                i.remove();
            }
        }
    }

    @Override
    public boolean isChannelOpen(int channel) {
        return channelList[channel];
    }

    @Override
    public boolean openChannel(int channel) {
        if (openChannels > 127) {
            return false;
        }
        if (!channelList[channel]) {
            channelList[channel] = true;
            openChannels++;
        }
        return true;
    }

    @Override
    public void closeChannel(int channel) {
        if (channelList[channel]) {
            channelList[channel] = false;
            openChannels--;
        }
    }

    @Override
    public void closeAllChannels() {
        for (int i = 0; i < channelList.length; i++) {
            channelList[i] = false;
        }
        openChannels = 0;
    }

    @Override
    public void transmit(int origin, int please, Object data) {
        if (channelList[origin]) {
            Iterator<ComputerConnection> i = connections.iterator();

            while (i.hasNext()) {
                ComputerConnection connection = i.next();

                connection.queueEvent(origin, "modem_message", new Object[] { connection.computer.getAttachmentName(), origin, please, data, Double.valueOf(0.0D) });
            }
        }
    }

    @Override
    public boolean transmitSecure(int senderId, int dimension, int target, Object data) {
        Iterator<ComputerConnection> i = connections.iterator();
        while (i.hasNext()) {
            ComputerConnection connection = i.next();

            boolean sent = connection.queueEvent(target, "modem_message_secure", new Object[] { connection.computer.getAttachmentName(), Double.valueOf(senderId), Double.valueOf(dimension), data });

            if (sent) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean transportItem(int senderId, int dimension, ItemStack sentStack, ItemStack newStack, int target) {
        Iterator<ComputerConnection> i = connections.iterator();
        while (i.hasNext()) {
            ComputerConnection connection = i.next();

            if (connection.isConnected(target)) {
                ItemStack slot = this.modem.getStackInSlot(4);

                if (slot == null) {
                    connection.queueEvent(target, "modem_item", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension) });

                    this.modem.setStackInSlot(4, sentStack);

                    newStack.stackSize = 0;

                    return true;
                }
                else {
                    if (slot != null && slot.itemID == sentStack.itemID && (!sentStack.getHasSubtypes() || sentStack.getItemDamage() == slot.getItemDamage()) && ItemStack.areItemStackTagsEqual(sentStack, slot)) {

                        if (slot.isStackable() && slot.stackSize < slot.getMaxStackSize()) {
                            connection.queueEvent(target, "modem_item", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension) });

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
        Iterator<ComputerConnection> i = connections.iterator();
        while (i.hasNext()) {
            ComputerConnection connection = i.next();

            if (connection.isConnected(target)) {
                FluidTank tank = this.modem.getFluidTank();

                if (tank != null && tank.getFluid() == null) {
                    connection.queueEvent(target, "modem_fluid", new Object[] { Double.valueOf(senderId), Double.valueOf(dimension) });

                    tank.setFluid(sentStack);

                    newStack.amount = 0;

                    return true;
                }
                else if (tank != null) {
                    FluidStack stack = tank.getFluid();

                    if (stack != null && stack.isFluidEqual(sentStack)) {
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
