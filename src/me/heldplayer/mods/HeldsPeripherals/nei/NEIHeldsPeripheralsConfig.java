
package me.heldplayer.mods.HeldsPeripherals.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NEIHeldsPeripheralsConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        EnderChargeRecipeHandler handler = new EnderChargeRecipeHandler();

        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }

    @Override
    public String getName() {
        return "Held's Peripherals NEI Addon";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

}
