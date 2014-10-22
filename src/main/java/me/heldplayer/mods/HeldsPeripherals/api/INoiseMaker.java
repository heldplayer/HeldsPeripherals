package me.heldplayer.mods.HeldsPeripherals.api;

/**
 * Interface for Noise Makers.
 *
 * @author heldplayer
 * @see IHeldsPeripheral
 */
public interface INoiseMaker extends IHeldsPeripheral {

    /**
     * Gets the amount of cooldown ticks remaining on this noise maker
     *
     * @return The remaining cooldown ticks.
     */
    int getCoolingTime();

    /**
     * Sets the remaining ticks this noise maker should cool down.
     *
     * @param time
     *         The new amount of ticks
     */
    void setCoolingTime(int time);

    /**
     * Sets this noise maker to cool down.
     */
    void setCoolingDown();

    /**
     * Gets whether this noise maker is cooling down.
     *
     * @return True if cooling down, false otherwise.
     */
    boolean isCoolingDown();

    /**
     * Increases the cooling time, called when making a noise
     */
    void increaseCoolingTime();

}
