
package me.heldplayer.mods.HeldsPeripherals.api;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
            HeldsPeripheralAPI.addCharge(items.get(i), value);
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
        HeldsPeripheralAPI.tryInit();

        if (HeldsPeripheralAPI.addCharge != null) {
            try {
                HeldsPeripheralAPI.addCharge.invoke(null, item, value);
            }
            catch (Exception e) {
                System.err.println("Failed calling HeldsPeripherals method 'addCharge(ItemStack, Integer)' through API'");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for creating a link between the API and the mod itself
     */
    @SuppressWarnings("unchecked")
    private static void tryInit() {
        if (!HeldsPeripheralAPI.APIInitialized) {
            try {
                HeldsPeripheralAPI.heldsPeripherals = Class.forName("me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals");
                HeldsPeripheralAPI.addCharge = HeldsPeripheralAPI.heldsPeripherals.getMethod("addCharge", new Class[] { ItemStack.class, Integer.class });
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

            HeldsPeripheralAPI.APIInitialized = true;
        }
    }

    private static boolean APIInitialized = false;
    @SuppressWarnings("rawtypes")
    private static Class heldsPeripherals = null;
    private static Method addCharge;
}
