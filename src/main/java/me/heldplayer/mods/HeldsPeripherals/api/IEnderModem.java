package me.heldplayer.mods.HeldsPeripherals.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * Interface for Trans World Modems
 *
 * @author Mitchel
 */
public interface IEnderModem extends IHeldsPeripheral {

    /**
     * Gets the remaining charge left in the modem.
     *
     * @return The remaining charge left in the modem.
     */
    int getChargeLevel();

    /**
     * Decreases the charge in this modem with specified amount.
     *
     * @param amount The amount of charge to decrease with.
     */
    void decreaseCharge(int amount);

    /**
     * Gets the remaining sends for this modem.
     *
     * @return The remaining sends in this modem.
     */
    int getRemainingSends();

    /**
     * Gets the remaining transports for this modem.
     *
     * @return The remaining transports in this modem.
     */
    int getRemainingTransports();

    /**
     * Gets the remaining fluid transports for this modem.
     *
     * @return The remaining fluid transports in this modem.
     */
    int getRemainingFluidTransports();

    /**
     * Returns the direct stack in the specified slot.
     *
     * @param slot The slot, ranging from 0 to 3.
     * @return The ItemStack in the specified slot.
     */
    ItemStack getStackInSlot(int slot);

    /**
     * Sets the stack in the specified slot.
     *
     * @param slot  The slot, ranging from 0 to 3.
     * @param stack The stack to set the slot to.
     */
    void setStackInSlot(int slot, ItemStack stack);

    /**
     * Returns the FluidTank in the peripheral.
     *
     * @return The FluidTank in the peripheral.
     */
    FluidTank getFluidTank();

    IModem getModem();

}
