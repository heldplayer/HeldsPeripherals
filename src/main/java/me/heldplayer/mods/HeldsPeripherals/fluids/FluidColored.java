package me.heldplayer.mods.HeldsPeripherals.fluids;

import net.minecraftforge.fluids.Fluid;

public class FluidColored extends Fluid {

    protected int color = 0xFFFFFF;

    public FluidColored(String fluidName) {
        super(fluidName);
    }

    @Override
    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
