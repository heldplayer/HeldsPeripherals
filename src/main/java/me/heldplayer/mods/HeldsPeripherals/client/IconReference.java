
package me.heldplayer.mods.HeldsPeripherals.client;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconReference implements IIcon {

    public IIcon icon;

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconHeight() {
        return this.icon.getIconHeight();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconWidth() {
        return this.icon.getIconWidth();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinU() {
        return this.icon.getMinU();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxU() {
        return this.icon.getMaxU();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedU(double step) {
        return this.icon.getInterpolatedU(step);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinV() {
        return this.icon.getMinV();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxV() {
        return this.icon.getMaxV();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedV(double step) {
        return this.icon.getInterpolatedV(step);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getIconName() {
        return this.icon.getIconName();
    }

}
