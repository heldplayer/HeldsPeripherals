
package me.heldplayer.mods.HeldsPeripherals.tileentity;

import me.heldplayer.mods.HeldsPeripherals.api.IHeldsPeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dan200.computer.api.IPeripheral;

public abstract class TileEntityHeldsPeripheral extends TileEntity implements IPeripheral, IHeldsPeripheral {

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            return;
        }

        this.update();
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public int getX() {
        return this.xCoord;
    }

    @Override
    public int getY() {
        return this.yCoord;
    }

    @Override
    public int getZ() {
        return this.zCoord;
    }

}