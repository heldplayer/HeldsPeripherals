package me.heldplayer.mods.HeldsPeripherals.packet;

import net.minecraft.world.World;
import net.specialattack.forge.core.packet.SpACorePacket;

public abstract class HeldsPeripheralsPacket extends SpACorePacket {

    public HeldsPeripheralsPacket(World world) {
        super(world);
    }

}
