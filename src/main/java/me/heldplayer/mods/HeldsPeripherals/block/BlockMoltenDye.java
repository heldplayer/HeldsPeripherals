
package me.heldplayer.mods.HeldsPeripherals.block;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.fluids.FluidColored;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMoltenDye extends BlockFluidClassic {

    public BlockMoltenDye(int id, Fluid fluid, Material material) {
        super(id, fluid, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? this.getFluid().getStillIcon() : this.getFluid().getFlowingIcon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return this.getFluid().getColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return this.getFluid().getColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return this.getFluid().getColor();
    }

    public static int[] colors = new int[] { 0xFFFFFF, 0xFF7F00, 0xFF00FF, 0x7F7FFF, 0xFFFF00, 0x00FF00, 0xFF7FFF, 0x7F7F7F, 0xBEBEBE, 0x007F7F, 0x8000FF, 0x00007F, 0x7F3F00, 0x007F00, 0xFF0000, 0x3F3F3F };
    public static String[] color_names = new String[] { "white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "gray", "lightGray", "cyan", "purple", "blue", "brown", "green", "red", "black" };

    public static FluidColored[] registerFluids() {
        FluidColored[] result = new FluidColored[colors.length];
        for (int i = 0; i < colors.length && i < color_names.length; i++) {
            FluidColored fluid = new FluidColored("molten " + color_names[i] + " dye");
            fluid.setUnlocalizedName("dye.molten." + color_names[i]);
            fluid.setIcons(Objects.ICON_MOLTEN_DYE_STILL, Objects.ICON_MOLTEN_DYE_FLOW);
            fluid.setColor(colors[i]);
            FluidRegistry.registerFluid(fluid);

            result[i] = fluid;
        }

        return result;
    }

}
