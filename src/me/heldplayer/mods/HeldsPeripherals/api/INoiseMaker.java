
package me.heldplayer.mods.HeldsPeripherals.api;

/**
 * Interface for Noise Makers.
 * 
 * @see IHeldsPeripheral
 * @author heldplayer
 * 
 */
public interface INoiseMaker extends IHeldsPeripheral {

    /**
     * Gets the amount of cooldown ticks remaining on this noise maker
     * 
     * @return The remaining cooldown ticks.
     */
    public abstract int getCoolingTime();

    /**
     * Sets this noise maker to cool down.
     */
    public abstract void setCoolingDown();

    /**
     * Gets whether this noise maker is cooling down.
     * 
     * @return True if cooling down, false otherwise.
     */
    public abstract boolean isCoolingDown();

    /**
     * Increases the cooling time, called when making a noise
     */
    public abstract void increaseCoolingTime();

    /**
     * Sets the remaining ticks this noise maker should cool down.
     * 
     * @param time
     *        The new amount of ticks
     */
    public abstract void setCoolingTime(int time);

}
