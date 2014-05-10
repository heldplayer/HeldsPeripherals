
package me.heldplayer.mods.HeldsPeripherals.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Basic interface for any Peripheral added by HeldsPeripherals. Implemented by
 * {@link TileEntity} and/or {@link IHostedPeripheral}.
 * 
 * @author heldplayer
 * 
 */
public interface IHeldsPeripheral {

    /**
     * Gets the world this peripheral is in.
     * 
     * @return The world this peripheral is in.
     */
    World getWorld();

    /**
     * Gets the X position of this peripheral.
     * 
     * @return The X position of this peripheral.
     */
    int getX();

    /**
     * Gets the Y position of this peripheral.
     * 
     * @return The Y position of this peripheral.
     */
    int getY();

    /**
     * Gets the Z position of this peripheral.
     * 
     * @return The Z position of this peripheral.
     */
    int getZ();

    /**
     * Method used for updating the peripheral.
     * Called during {@link TileEntity#updateEntity()} or
     * {@link IHostedPeripheral#update()}.
     */
    void update();

    /**
     * Sets the name of a HeldsPeripheral, drawn instead of the standard GUI
     * title.
     * 
     * @param name
     *        The new name of the peripheral
     */
    void setName(String name);

}
