
package me.heldplayer.mods.HeldsPeripherals.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import me.heldplayer.util.HeldCore.packet.HeldCorePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class Packet1PlaySound extends HeldCorePacket {

    public double posX;
    public double posY;
    public double posZ;
    public String sound;
    public float volume;
    public float pitch;

    public Packet1PlaySound(int packetId) {
        super(packetId, null);
    }

    public Packet1PlaySound(double posX, double posY, double posZ, String sound, float volume, float pitch) {
        super(1, null);

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
    public void read(ByteArrayDataInput in) throws IOException {
        this.posX = in.readDouble();
        this.posY = in.readDouble();
        this.posZ = in.readDouble();
        this.volume = in.readFloat();
        this.pitch = in.readFloat();

        int length = in.readInt();
        byte[] nameBytes = new byte[length];
        in.readFully(nameBytes);
        this.sound = new String(nameBytes);
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        byte[] nameBytes = this.sound.getBytes();

        out.writeDouble(this.posX);
        out.writeDouble(this.posY);
        out.writeDouble(this.posZ);
        out.writeFloat(this.volume);
        out.writeFloat(this.pitch);
        out.writeInt(nameBytes.length);
        out.write(nameBytes);
    }

    @Override
    public void onData(INetworkManager manager, EntityPlayer player) {
        World world = player.worldObj;
        world.playSound(posX, posY, posZ, sound, volume, pitch, false);
    }

}
