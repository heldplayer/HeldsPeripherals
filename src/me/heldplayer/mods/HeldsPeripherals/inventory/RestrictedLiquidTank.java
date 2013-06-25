
package me.heldplayer.mods.HeldsPeripherals.inventory;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class RestrictedLiquidTank extends LiquidTank {

    private LiquidStack allowedType = null;

    public RestrictedLiquidTank(LiquidStack liquid, int capacity, int pressure, int count, TileEntity tile) {
        super(null, capacity, tile);

        LiquidStack newStack = null;

        if (liquid != null) {
            newStack = liquid.copy();
        }

        if (newStack != null) {
            newStack.amount = count;
        }

        this.setTankPressure(pressure);
        this.setLiquid(liquid);
    }

    @Override
    public void setLiquid(LiquidStack liquid) {
        if (this.isAllowed(liquid)) {
            super.setLiquid(liquid);
        }
    }

    @Override
    public int fill(LiquidStack resource, boolean doFill) {
        if (this.isAllowed(resource)) {
            return super.fill(resource, doFill);
        }
        return 0;
    }

    public void setAllowedType(LiquidStack stack) {
        this.allowedType = stack;
    }

    public LiquidStack getAllowedType() {
        return this.allowedType;
    }

    public boolean isAllowed(LiquidStack stack) {
        if (stack == null || this.allowedType == null) {
            return false;
        }
        return stack.isLiquidEqual(this.allowedType);
    }

}
