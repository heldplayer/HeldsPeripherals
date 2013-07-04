
package me.heldplayer.mods.HeldsPeripherals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public final class CCInjector {

    public static void inject(FMLPreInitializationEvent event) {
        File apiFolder = (new File(event.getModConfigurationDirectory().getParent(), "mods/ComputerCraft/lua/rom/apis")).getAbsoluteFile();
        if (!apiFolder.exists()) {
            apiFolder.mkdirs();
        }

        File api = new File(apiFolder, "endernet");
        if (!api.exists()) {
            InputStream data = null;
            OutputStream output = null;
            try {
                api.createNewFile();
                data = ModHeldsPeripherals.class.getResourceAsStream("/me/heldplayer/lua/apis/endernet.lua");
                output = new FileOutputStream(api);

                while (data.available() > 0) {
                    output.write(data.read());
                }
            }
            catch (IOException e) {
                Objects.log.log(Level.WARNING, "Failed injecting endernet API to ComputerCraft", e);
            }
            finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (data != null) {
                        data.close();
                    }
                }
                catch (IOException e) {
                    Objects.log.log(Level.WARNING, "Failed injecting endernet API to ComputerCraft", e);
                }
            }
        }

        File helpFolder = (new File(event.getModConfigurationDirectory().getParent(), "mods/ComputerCraft/lua/rom/help")).getAbsoluteFile();
        if (!helpFolder.exists()) {
            helpFolder.mkdirs();
        }

        File helpFile = new File(helpFolder, "endernet");
        if (!helpFile.exists()) {
            InputStream data = null;
            OutputStream output = null;
            try {
                helpFile.createNewFile();
                data = ModHeldsPeripherals.class.getResourceAsStream("/me/heldplayer/lua/help/endernet");
                output = new FileOutputStream(helpFile);

                while (data.available() > 0) {
                    output.write(data.read());
                }
            }
            catch (IOException e) {
                Objects.log.log(Level.WARNING, "Failed injecting endernet help file to ComputerCraft", e);
            }
            finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (data != null) {
                        data.close();
                    }
                }
                catch (IOException e) {
                    Objects.log.log(Level.WARNING, "Failed injecting endernet help file to ComputerCraft", e);
                }
            }
        }
    }

}
