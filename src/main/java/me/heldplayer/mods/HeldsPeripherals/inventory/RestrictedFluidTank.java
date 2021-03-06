package me.heldplayer.mods.HeldsPeripherals.inventory;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class RestrictedFluidTank extends FluidTank {

    private FluidStack allowedType = null;

    public RestrictedFluidTank(FluidStack fluid, int capacity, int count, TileEntity tile) {
        super(capacity);

        FluidStack newStack = null;

        if (fluid != null) {
            newStack = fluid.copy();
        }

        if (newStack != null) {
            newStack.amount = count;
        }

        this.setFluid(fluid);
    }

    @Override
    public void setFluid(FluidStack fluid) {
        if (this.isAllowed(fluid)) {
            super.setFluid(fluid);
        }
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (this.isAllowed(resource)) {
            return super.fill(resource, doFill);
        }
        return 0;
    }

    public boolean isAllowed(FluidStack stack) {
        return !(stack == null || this.allowedType == null) && stack.isFluidEqual(this.allowedType);
    }

    public FluidStack getAllowedType() {
        return this.allowedType;
    }

    public void setAllowedType(FluidStack stack) {
        this.allowedType = stack;
    }

    public boolean isAllowed(Fluid fluid) {
        return !(fluid == null || this.allowedType == null) && this.allowedType.fluidID == fluid.getID();
    }

}
