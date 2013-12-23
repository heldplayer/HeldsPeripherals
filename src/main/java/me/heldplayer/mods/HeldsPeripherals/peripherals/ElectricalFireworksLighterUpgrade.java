
package me.heldplayer.mods.HeldsPeripherals.peripherals;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.client.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class ElectricalFireworksLighterUpgrade implements ITurtleUpgrade {

    @Override
    public int getUpgradeID() {
        return 205;
    }

    @Override
    public String getAdjective() {
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
    public boolean isSecret() {
        return false;
    }

    @Override
    public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new ElectricalFireworksLighterPeripheral(turtle);
    }

    @Override
    public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
        return false;
    }

    @Override
    public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
        return ClientProxy.fireworksUpgrade;
    }

}
