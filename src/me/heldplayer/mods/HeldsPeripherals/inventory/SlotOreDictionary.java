
package me.heldplayer.mods.HeldsPeripherals.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlotOreDictionary extends Slot {

    private final String ore;
    @SideOnly(Side.CLIENT)
    public Icon icon;

    public SlotOreDictionary(IInventory inventory, int index, int xPos, int yPos, String ore) {
        super(inventory, index, xPos, yPos);

        this.ore = ore;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        int id = OreDictionary.getOreID(stack);
        int id2 = OreDictionary.getOreID(this.ore);

        if (id == id2 && id > 0) {
            return true;
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBackgroundIconIndex() {
        return this.icon;
    }

}
