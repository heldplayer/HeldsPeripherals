package me.heldplayer.mods.HeldsPeripherals.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.Objects;
import net.minecraft.client.gui.GuiScreen;

public class GuiConfiguration extends GuiConfig {

    public GuiConfiguration(GuiScreen parent) {
        super(parent, ModHeldsPeripherals.instance.config.getConfigElements(), Objects.MOD_ID, false, false, "Held's Peripherals Configuration");
    }

}
