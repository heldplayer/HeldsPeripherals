
package me.heldplayer.mods.HeldsPeripherals;

import java.util.logging.Logger;

import me.heldplayer.mods.HeldsPeripherals.block.BlockMulti1;
import me.heldplayer.mods.HeldsPeripherals.block.BlockTransWorldModem;
import me.heldplayer.mods.HeldsPeripherals.client.gui.CreativeTab;
import me.heldplayer.mods.HeldsPeripherals.item.ItemEnderCharge;
import me.heldplayer.mods.HeldsPeripherals.item.ItemMoltenDye;

/**
 * HeldsPeripherals mod Objects
 * 
 */
public class Objects {

    public static final String MOD_ID = "CCheldsPeripherals";
    public static final String MOD_NAME = "CC Held's Peripherals";
    public static final String MOD_VERSION = "1.09.03.02";
    public static final String MOD_DEPENCIES = "after:Thaumcraft";
    public static final String MOD_CHANNEL = "CCHPChannel";
    public static final String CLIENT_PROXY = "me.heldplayer.mods.HeldsPeripherals.client.ClientProxy";
    public static final String SERVER_PROXY = "me.heldplayer.mods.HeldsPeripherals.common.CommonProxy";

    public static BlockMulti1 blockMulti1;
    public static BlockTransWorldModem blockTransWorldModem;
    public static ItemMoltenDye itemMoltenDye;
    public static ItemEnderCharge itemEnderCharge;
    public static CreativeTab creativeTab;

    public static final String SOUND_RESOURCE_LOCATION = "/me/heldplayer/audio/";
    public static final String SOUND_RESOURCE_PATH = "mods/Smartestone/audio/";
    public static final String[] SOUNDS = new String[] { "cloister-bell", "siren", "bell" };
    public static final String PREFIX = "CC.HP.";
    public static final String CLOISTER_BELL = PREFIX + "cloister-bell";
    public static final String SIREN = PREFIX + "siren";
    public static final String BELL = PREFIX + "bell";
    public static Logger log;

}
