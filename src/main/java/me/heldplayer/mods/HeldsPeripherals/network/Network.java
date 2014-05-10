
package me.heldplayer.mods.HeldsPeripherals.network;

import java.util.Iterator;
import java.util.LinkedList;

import me.heldplayer.mods.HeldsPeripherals.api.IEnderModem;
import me.heldplayer.mods.HeldsPeripherals.api.IModem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

// FIXME: check for world leaking, as it most likely happens
public class Network {

    private static LinkedList<IModem> modems = new LinkedList<IModem>();

    public static void clearModems() {
        modems.clear();
    }

    public static IModem registerModem(IEnderModem modemTile) {
        Modem modem = new Modem(modemTile);

        modems.add(modem);

        return modem;
    }

    public static void transmit(int origin, int please, Object data) {

    }

    public static boolean transmitSecure(int senderId, int dimension, int target, Object data) {
        Iterator<IModem> i = modems.iterator();
        while (i.hasNext()) {
            IModem modem = i.next();

            if (modem != null) {
                if (modem.transmitSecure(senderId, dimension, target, data)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ItemStack transportItem(int senderId, int dimension, ItemStack origStack, int target) {
        if (origStack != null && origStack.stackSize > 0 && origStack.getItem() != null && origStack.getItem() != Item.getItemFromBlock(Blocks.air)) {
            ItemStack newStack = origStack.copy();
            ItemStack sentStack = origStack.copy();

            Iterator<IModem> i = modems.iterator();
            while (i.hasNext()) {
                IModem modem = i.next();

                if (modem != null) {
                    if (modem.transportItem(senderId, dimension, sentStack, newStack, target)) {
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

    public static FluidStack transportFluid(int senderId, int dimension, FluidStack origStack, int target) {
        if (origStack != null && origStack.amount > 0) {
            FluidStack newStack = origStack.copy();
            FluidStack sentStack = origStack.copy();

            Iterator<IModem> i = modems.iterator();
            while (i.hasNext()) {
                IModem modem = i.next();

                if (modem != null) {
                    if (modem.transportFluid(senderId, dimension, sentStack, newStack, target)) {
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

}
