
package me.heldplayer.mods.HeldsPeripherals.nei;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import me.heldplayer.mods.HeldsPeripherals.common.Objects;
import me.heldplayer.mods.HeldsPeripherals.common.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.common.RecipeEnderCharge;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import codechicken.nei.InventoryCraftingDummy;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EnderChargeRecipeHandler extends ShapelessRecipeHandler {
    public static Random rnd = new Random();

    public class CachedEnderChargeRecipe extends CachedShapelessRecipe {

        protected LinkedList<Object> itemList = new LinkedList<Object>();

        public CachedEnderChargeRecipe(ItemStack result) {
            super(result);

            this.cycle();
        }

        public void cycle() {
            this.itemList.clear();

            this.itemList.add(new ItemStack(Objects.itemEnderCharge, 1, rnd.nextInt(62) + 1));

            this.setIngredients(this.itemList);

            ArrayList<PositionedStack> ingreds = this.getIngredients();
            for (int i = 0; i < 9; i++) {
                EnderChargeRecipeHandler.this.inventoryCrafting.setInventorySlotContents(i, i < ingreds.size() ? ingreds.get(i).item : null);
            }

            if (!EnderChargeRecipeHandler.this.recipe.matches(EnderChargeRecipeHandler.this.inventoryCrafting, null)) {
                throw new RuntimeException("Invalid Recipe?");
            }

            this.setResult(EnderChargeRecipeHandler.this.recipe.getCraftingResult(null));
        }
    }

    public class CachedEnderChargeCombineRecipe extends CachedEnderChargeRecipe {

        protected int charge = -1;;

        public CachedEnderChargeCombineRecipe(ItemStack result, int charge) {
            super(result);

            this.charge = charge;
        }

        @Override
        public void cycle() {
            if (this.charge <= 0) {
                return;
            }

            this.itemList.clear();

            int currentCharge = Math.min(rnd.nextInt(this.charge - 1), 63) + 1;

            this.itemList.add(new ItemStack(Objects.itemEnderCharge, 1, currentCharge - 1));

            for (int i = 1; i < 8; i++) {
                if (currentCharge >= this.charge) {
                    break;
                }

                int added = Math.min(rnd.nextInt(this.charge - currentCharge), 63) + 1;

                this.itemList.add(new ItemStack(Objects.itemEnderCharge, 1, added - 1));

                currentCharge += added;
            }

            if (currentCharge < this.charge) {
                this.itemList.add(new ItemStack(Objects.itemEnderCharge, 1, this.charge - currentCharge - 1));
            }

            this.setIngredients(this.itemList);

            ArrayList<PositionedStack> ingreds = this.getIngredients();
            for (int i = 0; i < 9; i++) {
                EnderChargeRecipeHandler.this.inventoryCrafting.setInventorySlotContents(i, i < ingreds.size() ? ingreds.get(i).item : null);
            }

            if (!EnderChargeRecipeHandler.this.recipe.matches(EnderChargeRecipeHandler.this.inventoryCrafting, null)) {
                throw new RuntimeException("Invalid Recipe?");
            }

            this.setResult(EnderChargeRecipeHandler.this.recipe.getCraftingResult(null));
        }

    }

    private InventoryCrafting inventoryCrafting = new InventoryCraftingDummy();
    private RecipeEnderCharge recipe = new RecipeEnderCharge();

    public ArrayList<CachedEnderChargeRecipe> recipes = new ArrayList<CachedEnderChargeRecipe>();

    public EnderChargeRecipeHandler() {
        super();
        this.stackorder = new int[][] { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } };
        this.loadRecipes();
    }

    private void loadRecipes() {
        this.recipes.add(new CachedEnderChargeRecipe(new ItemStack(Objects.itemEnderCharge, 1, 0)));

        for (int i = 1; i < 64; i++) {
            ItemStack result = ModHeldsPeripherals.getItemForCharge(i + 1);

            if (result == null) {
                result = new ItemStack(Objects.itemEnderCharge, 1, i);
            }

            this.recipes.add(new CachedEnderChargeCombineRecipe(result, i + 1));
        }
    }

    public void loadCraftingRecipes(ItemStack result) {
        for (CachedEnderChargeRecipe recipe : this.recipes) {
            //if (recipe.result.item.itemID == result.itemID && recipe.result.item.getItemDamage() == result.getItemDamage()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.result.item, result)) {
                recipe.cycle();
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && this.getClass() == EnderChargeRecipeHandler.class) {
            for (int i = 0; i < this.recipes.size(); i++) {
                CachedEnderChargeRecipe recipe = this.recipes.get(i);
                recipe.cycle();
                this.arecipes.add(recipe);
            }
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        for (CachedEnderChargeRecipe recipe : this.recipes) {
            //if (recipe.contains(recipe.ingredients, ingredient)) {
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                ItemStack[] items = recipe.ingredients.get(i).items;

                for (ItemStack item : items) {
                    if (ingredient.itemID == Objects.itemEnderCharge.itemID && item.itemID == ingredient.itemID) {
                        recipe.cycle();
                        this.arecipes.add(recipe);
                    }
                    else if (NEIServerUtils.areStacksSameTypeCrafting(item, ingredient)) {
                        recipe.cycle();
                        this.arecipes.add(recipe);
                    }
                }
            }
        }
    }

    public void onUpdate() {
        if (!NEIClientUtils.shiftKey()) {
            this.cycleticks++;
            if (this.cycleticks % 20 == 0) {
                for (CachedRecipe crecipe : this.arecipes) {
                    ((CachedEnderChargeRecipe) crecipe).cycle();
                }
            }
        }
    }

    public String getRecipeName() {
        return "Ender Charges";
    }
}
