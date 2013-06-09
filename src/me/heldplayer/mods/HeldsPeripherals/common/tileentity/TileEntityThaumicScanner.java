
package me.heldplayer.mods.HeldsPeripherals.common.tileentity;

import me.heldplayer.mods.HeldsPeripherals.api.IThaumicScanner;
import me.heldplayer.mods.HeldsPeripherals.common.LogicHandler;
import dan200.computer.api.IComputerAccess;

public class TileEntityThaumicScanner extends TileEntityHeldsPeripheral implements IThaumicScanner {

    // IPeripheral

    @Override
    public String getType() {
        return "ThaumicScanner";
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "getNearestNode" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
        return LogicHandler.callMethod(computer, method, arguments, this);
    }

    @Override
    public boolean canAttachToSide(int side) {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer) {}

    @Override
    public void detach(IComputerAccess computer) {}

    // IHeldsPeripheral

    @Override
    public void update() {}

    @Override
    public void setName(String name) {}

}
