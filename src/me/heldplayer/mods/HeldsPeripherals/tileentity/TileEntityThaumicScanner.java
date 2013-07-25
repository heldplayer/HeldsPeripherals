
package me.heldplayer.mods.HeldsPeripherals.tileentity;

import me.heldplayer.mods.HeldsPeripherals.LogicHandler;
import me.heldplayer.mods.HeldsPeripherals.api.IThaumicScanner;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;

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
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
        return LogicHandler.callMethod(computer, context, method, arguments, this);
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
