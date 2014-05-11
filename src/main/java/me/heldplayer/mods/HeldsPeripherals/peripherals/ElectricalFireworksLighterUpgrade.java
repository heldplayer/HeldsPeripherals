
package me.heldplayer.mods.HeldsPeripherals.peripherals;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.client.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;

public class ElectricalFireworksLighterUpgrade implements ITurtleUpgrade {

    @Override
    public int getUpgradeID() {
        return 205;
    }

    @Override
    public String getUnlocalisedAdjective() {
        return "Fireworks";
    }

    @Override
    public TurtleUpgradeType getType() {
        return TurtleUpgradeType.Peripheral;
    }

    @Override
    public ItemStack getCraftingItem() {
        return new ItemStack(Objects.blockMulti1);
    }

    @Override
    public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new ElectricalFireworksLighterPeripheral(turtle);
    }

    @Override
    public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
        return TurtleCommandResult.failure();
    }

    @Override
    public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
        return ClientProxy.fireworksUpgrade;
    }

    @Override
    public void update(ITurtleAccess turtle, TurtleSide side) {
        // TODO Auto-generated method stub

    }

}
