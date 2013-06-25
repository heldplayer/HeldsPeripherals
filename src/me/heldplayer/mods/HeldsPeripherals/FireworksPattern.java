
package me.heldplayer.mods.HeldsPeripherals;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class FireworksPattern {
    public short flight = 0;
    public FireworkStarPattern star = null;
    public NBTTagList arguments = null;
    public boolean hideRocketTrail = false;
    public boolean silent = false;

    public void writeToNBT(NBTTagCompound compound) {
        compound.setShort("Flight", this.flight);
        NBTTagList patterns = new NBTTagList("Explosions");

        patterns.appendTag(this.star.getSavedCompound());

        compound.setTag("Explosions", patterns);
    }

    public ItemStack getItemStack() {
        NBTTagCompound fireworks = new NBTTagCompound("Fireworks");
        NBTTagList list = new NBTTagList("Explosions");

        if (this.star != null) {
            NBTTagCompound explosion = new NBTTagCompound();
            explosion.setByte("Type", this.star.type);
            explosion.setBoolean("Trail", this.star.hasTrail);
            explosion.setBoolean("Flicker", this.star.hasFlicker);
            explosion.setIntArray("Colors", this.star.primaryColors);
            explosion.setIntArray("FadeColors", this.star.secondaryColors);
            explosion.setTag("Arguments", this.arguments);
            explosion.setBoolean("Silent", this.silent);

            list.appendTag(explosion);
        }

        fireworks.setTag("Explosions", list);
        fireworks.setShort("Flight", this.flight);

        ItemStack stack = new ItemStack(Item.firework);

        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("Fireworks", fireworks);

        compound.setBoolean("HideTrail", this.hideRocketTrail);
        stack.setTagCompound(compound);

        return stack;
    }

    public static FireworksPattern readFromNBT(NBTTagCompound compound) {
        FireworksPattern pattern = new FireworksPattern();

        pattern.flight = compound.getShort("Flight");

        NBTTagList list = compound.getTagList("Explosions");

        pattern.star = FireworkStarPattern.getFromCompound((NBTTagCompound) list.tagAt(0));

        return pattern;
    }
}
