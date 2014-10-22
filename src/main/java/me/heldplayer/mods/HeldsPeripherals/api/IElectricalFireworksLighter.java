package me.heldplayer.mods.HeldsPeripherals.api;

import net.minecraft.item.ItemStack;

/**
 * Interface for Electrical Fireworks Lighters.
 *
 * @author heldplayer
 * @see IHeldsPeripheral
 */
public interface IElectricalFireworksLighter extends IHeldsPeripheral {

    /**
     * Gets the amount of ticks remaining on this fireworks lighter.
     *
     * @return The remaining cooldown ticks.
     */
    int getCoolingTime();

    /**
     * Sets the remaining ticks this fireworks lighter should cool down.
     *
     * @param time
     *         The new amount of ticks
     */
    void setCoolingTime(int time);

    /**
     * Sets this fireworks lighter to cool down.
     */
    void setCoolingDown();

    /**
     * Gets whether this fireworks lighter is cooling down.
     *
     * @return True if cooling down, false otherwise.
     */
    boolean isCoolingDown();

    /**
     * Increases the cooling time, called when firing 1 firework.
     */
    void increaseCoolingTime();

    /**
     * Gets the fluid level in the specified tank.<br/>
     * Tank IDs:
     * <ol>
     * <li>Red dye tank</li>
     * <li>Green dye tank</li>
     * <li>Blue dye tank</li>
     * </ol>
     *
     * @param tankId
     *         The ID of the tank.
     *
     * @return The fluid level in the specified tank.
     */
    int getFluidLevel(int tankId);

    /**
     * Sets the fluid level in the specified tank.
     *
     * @param tankId
     *         The ID of the tank.
     * @param amount
     *         The new amount of fluid in the tank.
     *
     * @see #getFluidLevel(int)
     */
    void setFluidLevel(int tankId, int amount);

    /**
     * Returns the direct stack in the specified slot.
     *
     * @param slot
     *         The slot, ranging from 0 to 3 on a regular block and ranging from
     *         0 to 15 on a turtle.
     *
     * @return The ItemStack in the specified slot.
     */
    ItemStack getStack(int slot);

    /**
     * Sets the stack in the specified slot.
     *
     * @param slot
     *         The slot, ranging from 0 to 3.
     * @param stack
     *         The stack to set the slot to.
     */
    void setStack(int slot, ItemStack stack);

    /**
     * Returns the first slot with an item containing the specified ore
     * dictionary name. Used for turtles.
     *
     * @param name
     *         The name of the to be searched ore dictionary object.
     *
     * @return The first slot with an item containing the specified ore
     * dictionary name.
     */
    int getOredictStack(String name);

}
