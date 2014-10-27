package me.heldplayer.mods.HeldsPeripherals.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import net.specialattack.forge.core.packet.Attributes;
import net.specialattack.forge.core.packet.BlockPosition;

public class Packet1PlaySound extends HeldsPeripheralsPacket {

    public BlockPosition position;
    public String sound;
    public float volume;
    public float pitch;

    public Packet1PlaySound() {
        super(null);
    }

    public Packet1PlaySound(BlockPosition position, String sound, float volume, float pitch) {
        super(null);

        this.position = position;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.attr(Attributes.BLOCK_POSITION).set(this.position);
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {
        this.volume = in.readFloat();
        this.pitch = in.readFloat();

        int length = in.readInt();
        byte[] nameBytes = new byte[length];
        in.readBytes(nameBytes);
        this.sound = new String(nameBytes);
    }

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {
        byte[] nameBytes = this.sound.getBytes();

        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeInt(nameBytes.length);
        out.writeBytes(nameBytes);
    }

    @Override
    public void onData(ChannelHandlerContext context) {
        this.requireAttribute(Attributes.BLOCK_POSITION);

        BlockPosition position = context.attr(Attributes.BLOCK_POSITION).get();
        this.position.world.playSound(this.position.x, this.position.y, this.position.z, this.sound, this.volume, this.pitch, false);
    }

}
