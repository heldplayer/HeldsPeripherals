
package me.heldplayer.mods.HeldsPeripherals.packet;

import me.heldplayer.mods.HeldsPeripherals.Objects;

public class PacketHandler extends me.heldplayer.util.HeldCore.packet.PacketHandler {

    public static PacketHandler instance;

    public PacketHandler() {
        super(Objects.MOD_CHANNEL);
        this.registerPacket(1, Packet1PlaySound.class);
        instance = this;
    }

}
