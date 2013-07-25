
package me.heldplayer.mods.HeldsPeripherals.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockMoltenDye extends ItemBlock {

    public ItemBlockMoltenDye(int id) {
        super(id);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return Block.blocksList[this.itemID].getBlockColor();
    }

}
