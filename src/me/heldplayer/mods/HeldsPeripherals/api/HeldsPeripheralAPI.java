
package me.heldplayer.mods.HeldsPeripherals.api;

import java.lang.reflect.Method;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Bridge class for the HeldsPeripherals mod. Methods in this class must be
 * during or after {@link cpw.mods.fml.common.Mod.Init @Init}
 * 
 * @author heldplayer
 * 
 */
public class HeldsPeripheralAPI {

    /**
     * Convenience method for adding multiple items with the same charge level.
     * 
     * @param items
     *        The items to be added.
     * @param value
     *        The charge level the items will give. Must be > 0.
     */
    public static void addCharges(ArrayList<ItemStack> items, Integer value) {
        if (value <= 0) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            addCharge(items.get(i), value);
        }
    }

    /**
     * Method for adding a charge level to an item. Only checks for same itemID
     * and damage value when trying to consume.
     * 
     * @param item
     *        The item to be added.
     * @param value
     *        The charge level the item will give. Must be > 0.
     */
    public static void addCharge(ItemStack item, Integer value) {
        tryInit();

        if (addCharge != null) {
            try {
                addCharge.invoke(null, item, value);
            }
            catch (Exception e) {
                System.err.println("Failed calling HeldsPeripherals method 'addCharge(ItemStack, Integer)' through API'");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for obtaining a {@link net.minecraft.block.Block Block} instance
     * owned by HeldsPeripherals.
     * 
     * @param id
     *        The id of the block.<br>
     *        Current ids:
     *        <ul>
     *        <li>1: BlockTransWorldModem</li>
     *        <li>2: BlockMulti1</li>
     *        </ul>
     * @return The {@link net.minecraft.block.Block Block} instance or null
     */
    public static Block getBlock(Integer id) {
        tryInit();

        if (getBlock != null) {
            try {
                return (Block) getBlock.invoke(null, id);
            }
            catch (Exception e) {
                System.err.println("Failed calling HeldsPeripherals method 'getBlock(Integer)' through API'");
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Method for obtaining a {@link net.minecraft.item.Item Item} instance
     * owned by HeldsPeripherals.
     * 
     * @param id
     *        The id of the item.<br>
     *        Current ids:
     *        <ul>
     *        <li>1: ItemEnderCharge</li>
     *        <li>2: ItemMoltenDye</li>
     *        </ul>
     * @return The {@link net.minecraft.item.Item Item} instance or null
     */
    public static Item getItem(Integer id) {
        tryInit();

        if (getItem != null) {
            try {
                return (Item) getItem.invoke(null, id);
            }
            catch (Exception e) {
                System.err.println("Failed calling HeldsPeripherals method 'getItem(Integer)' through API'");
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Method for creating a link between the API and the mod itself
     */
    @SuppressWarnings("unchecked")
    private static void tryInit() {
        if (!APIInitialized) {
            try {
                heldsPeripherals = Class.forName("me.heldplayer.mods.HeldsPeripherals.common.ModHeldsPeripherals");
                addCharge = heldsPeripherals.getMethod("addCharge", new Class[] { ItemStack.class, Integer.class });
                getBlock = heldsPeripherals.getMethod("getBlock", new Class[] { Integer.class });
                getItem = heldsPeripherals.getMethod("getItem", new Class[] { Integer.class });
            }
            catch (ClassNotFoundException e) {
                System.out.println("Could not find HeldsPeripherals");
            }
            catch (SecurityException e) {
                System.err.println("Failed initializing API methods");
            }
            catch (NoSuchMethodException e) {
                System.err.println("Failed initializing APi methods");
            }

            APIInitialized = true;
        }
    }

    private static boolean APIInitialized = false;
    @SuppressWarnings("rawtypes")
    private static Class heldsPeripherals = null;
    private static Method addCharge;
    private static Method getBlock;
    private static Method getItem;
}
