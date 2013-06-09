
package me.heldplayer.mods.HeldsPeripherals.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidTank;

/**
 * Interface for Trans World Modems
 * 
 * @author Mitchel
 * 
 */
public interface ITransWorldModem extends IHeldsPeripheral {

    /**
     * Gets the remaining charge left in the modem.
     * 
     * @return The remaining charge left in the modem.
     */
    public abstract int getChargeLevel();

    /**
     * Decreases the charge in this modem with specified amount.
     * 
     * @param amount
     *        The amount of charge to decrease with.
     */
    public abstract void decreaseCharge(int amount);

    /**
     * Gets the remaining sends for this modem.
     * 
     * @return The remaining sends in this modem.
     */
    public abstract int getRemainingSends();

    /**
     * Gets the remaining transports for this modem.
     * 
     * @return The remaining transports in this modem.
     */
    public abstract int getRemainingTransports();

    /**
     * Gets the remaining liquid transports for this modem.
     * 
     * @return The remaining liquid transports in this modem.
     */
    public abstract int getRemainingLiquidTransports();

    /**
     * Returns the direct stack in the specified slot.
     * 
     * @param slot
     *        The slot, ranging from 0 to 3.
     * @return The ItemStack in the specified slot.
     */
    public abstract ItemStack getStackInSlot(int slot);

    /**
     * Sets the stack in the specified slot.
     * 
     * @param slot
     *        The slot, ranging from 0 to 3.
     * @param stack
     *        The stack to set the slot to.
     */
    public abstract void setStackInSlot(int slot, ItemStack stack);

    /**
     * Returns the LiquidStack in the peripheral.
     * 
     * @return The LiquidStack in the peripheral.
     */
    public abstract LiquidTank getLiquidTank();

}
