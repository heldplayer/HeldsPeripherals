
package me.heldplayer.mods.HeldsPeripherals;

import net.minecraft.nbt.NBTTagCompound;

public class FireworkStarPattern {

    public byte type;
    public boolean hasTrail;
    public boolean hasFlicker;
    public int[] primaryColors;
    public int[] secondaryColors;

    public NBTTagCompound getSavedCompound() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setByte("Type", this.type);
        compound.setBoolean("Trail", this.hasTrail);
        compound.setBoolean("Flicker", this.hasFlicker);
        compound.setIntArray("Colors", this.primaryColors);
        compound.setIntArray("FadeColors", this.secondaryColors);

        return compound;
    }

    public static FireworkStarPattern getFromCompound(NBTTagCompound compound) {
        FireworkStarPattern pattern = new FireworkStarPattern();

        pattern.type = compound.getByte("Type");
        pattern.hasTrail = compound.getBoolean("Trail");
        pattern.hasFlicker = compound.getBoolean("Flicker");
        pattern.primaryColors = compound.getIntArray("Colors");
        pattern.secondaryColors = compound.getIntArray("FadeColors");

        return pattern;
    }
}
