
package me.heldplayer.mods.HeldsPeripherals.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import dan200.computer.api.IComputerAccess;

public interface IModem {

    void attach(IComputerAccess computer);

    void detach(IComputerAccess computer);

    boolean isChannelOpen(int channel);

    boolean openChannel(int channel);

    void closeChannel(int channel);

    void closeAllChannels();

    void transmit(int origin, int please, Object data);

    boolean transmitSecure(int senderId, int dimension, int target, Object data);

    boolean transportItem(int senderId, int dimension, ItemStack sentStack, ItemStack newStack, int target);

    boolean transportFluid(int senderId, int dimension, FluidStack sentStack, FluidStack newStack, int target);

}
