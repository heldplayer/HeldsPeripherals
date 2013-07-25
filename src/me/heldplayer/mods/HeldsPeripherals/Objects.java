
package me.heldplayer.mods.HeldsPeripherals;

import java.util.logging.Logger;

import me.heldplayer.mods.HeldsPeripherals.block.BlockMoltenDye;
import me.heldplayer.mods.HeldsPeripherals.block.BlockMulti1;
import me.heldplayer.mods.HeldsPeripherals.block.BlockEnderModem;
import me.heldplayer.mods.HeldsPeripherals.client.IconReference;
import me.heldplayer.mods.HeldsPeripherals.client.gui.CreativeTab;
import me.heldplayer.mods.HeldsPeripherals.item.ItemEnderCharge;
import me.heldplayer.util.HeldCore.ModInfo;

/**
 * HeldsPeripherals mod Objects
 * 
 */
public class Objects {

    public static final String MOD_ID = "CCheldsPeripherals";
    public static final String MOD_NAME = "CC Held's Peripherals";
    public static final String MOD_VERSION = "1.10.01.01";
    public static final String MOD_DEPENCIES = "after:Thaumcraft";
    public static final String MOD_CHANNEL = "CCHPChannel";
    public static final String CLIENT_PROXY = "me.heldplayer.mods.HeldsPeripherals.client.ClientProxy";
    public static final String SERVER_PROXY = "me.heldplayer.mods.HeldsPeripherals.common.CommonProxy";

    public static final ModInfo MOD_INFO = new ModInfo(MOD_ID, MOD_NAME, MOD_VERSION);

    public static BlockMulti1 blockMulti1;
    public static BlockEnderModem blockEnderModem;
    public static BlockMoltenDye[] blocksMoltenDye;
    public static ItemEnderCharge itemEnderCharge;
    public static CreativeTab creativeTab;

    public static final String CLOISTER_BELL = "heldsperipherals:bell/cloister-bell";
    public static final String SIREN = "heldsperipherals:bell/siren";
    public static final String BELL = "heldsperipherals:bell/bell";
    public static final String[] SOUNDS = new String[] { CLOISTER_BELL, SIREN, BELL };
    public static Logger log;

    public static final IconReference ICON_MOLTEN_DYE_STILL = new IconReference();
    public static final IconReference ICON_MOLTEN_DYE_FLOW = new IconReference();

}
