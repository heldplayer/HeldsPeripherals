package me.heldplayer.mods.HeldsPeripherals.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.IOException;

public class Packet1PlaySound extends HeldsPeripheralsPacket {

    public double posX;
    public double posY;
    public double posZ;
    public String sound;
    public float volume;
    public float pitch;

    public Packet1PlaySound() {
        super(null);
    }

    public Packet1PlaySound(double posX, double posY, double posZ, String sound, float volume, float pitch) {
        super(null);

        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public Side getSendingSide() {
        return Side.SERVER;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {
        this.posX = in.readDouble();
        this.posY = in.readDouble();
        this.posZ = in.readDouble();
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

        out.writeDouble(this.posX);
        out.writeDouble(this.posY);
        out.writeDouble(this.posZ);
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeInt(nameBytes.length);
        out.writeBytes(nameBytes);
    }

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        World world = player.worldObj;
        world.playSound(this.posX, this.posY, this.posZ, this.sound, this.volume, this.pitch, false);
    }

}
