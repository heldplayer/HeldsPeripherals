
package me.heldplayer.mods.HeldsPeripherals;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeEnderCharge implements IRecipe {

    private ItemStack result = null;

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int charge = -1;
        int filledSlots = 0;
        this.result = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.itemID == Objects.itemEnderCharge.itemID) {
                charge += stack.getItemDamage() + 1;
                filledSlots++;
            }
            else if (stack != null) {
                return false;
            }
        }

        ItemStack item = ModHeldsPeripherals.getItemForCharge(charge + 1);

        if (item != null) {
            this.result = item;
        }
        else if (charge > 0) {
            if (charge >= 64) {
                return false;
            }

            if (filledSlots == 1) {
                this.result = new ItemStack(Objects.itemEnderCharge, charge + 1, 0);
            }
            else {
                this.result = new ItemStack(Objects.itemEnderCharge, 1, charge);
            }
        }
        else {
            return false;
        }

        return filledSlots > 0;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.result.copy();
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

}
