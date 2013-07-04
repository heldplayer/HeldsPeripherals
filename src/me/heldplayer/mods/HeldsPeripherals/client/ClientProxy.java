
package me.heldplayer.mods.HeldsPeripherals.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import me.heldplayer.mods.HeldsPeripherals.CommonProxy;
import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.client.gui.GuiFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.client.gui.GuiTransWorldModem;
import me.heldplayer.mods.HeldsPeripherals.inventory.SlotOreDictionary;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityTransWorldModem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static Icon[] icons = new Icon[4];
    public static Icon fireworksUpgrade = null;
    public static final String textureLocation = "/me/heldplayer/textures/HeldsPeripherals/";

    @SuppressWarnings("unchecked")
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        Render render = new RenderSnowball(Item.firework);
        render.setRenderManager(RenderManager.instance);
        RenderManager.instance.entityRenderMap.put(me.heldplayer.mods.HeldsPeripherals.entity.EntityFireworkRocket.class, render);

        if (ModHeldsPeripherals.enhancedEnderChargeRenderer.getValue()) {
            MinecraftForgeClient.registerItemRenderer(Objects.itemEnderCharge.itemID, new ItemRendererEnderCharge());
        }
    }

    public static SlotOreDictionary setIcon(SlotOreDictionary slot, int icon) {
        slot.icon = icons[icon];

        return slot;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        try {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

            if (ID == 0) {
                if (tileEntity != null && (tileEntity instanceof TileEntityTransWorldModem)) {
                    return new GuiTransWorldModem(player, ((TileEntityTransWorldModem) tileEntity));
                }
                else {
                    return null;
                }
            }
            if (ID == 1) {
                if (tileEntity != null && (tileEntity instanceof TileEntityFireworksLighter)) {
                    return new GuiFireworksLighter(player, ((TileEntityFireworksLighter) tileEntity));
                }
                else {
                    return null;
                }
            }

            return null;
        }
        catch (Exception ex) {
            System.out.println("Failed opening client GUI element.");

            ex.printStackTrace();

            return null;
        }
    }

    public static boolean renderItemId() {
        if (Loader.isModLoaded("NotEnoughItems")) {
            return codechicken.nei.NEIClientConfig.showIDs();
        }
        return false;
    }

    @ForgeSubscribe
    public void onSoundLoaded(SoundLoadEvent event) {
        File resources = new File(Minecraft.getMinecraft().mcDataDir, "assets");
        for (String sound : Objects.SOUNDS) {
            try {
                File file = new File(resources, Objects.SOUND_RESOURCE_PATH + sound + ".ogg");

                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();

                    InputStream in = ClientProxy.class.getResourceAsStream(Objects.SOUND_RESOURCE_LOCATION + sound + ".ogg");
                    OutputStream out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];
                    int bytes = 0;
                    int size = 0;

                    while ((bytes = in.read(buffer)) > 0) {
                        out.write(buffer, 0, bytes);
                        size += bytes;
                    }

                    out.close();
                    in.close();

                    if (size == 0) {
                        file.delete();
                    }
                }

                if (!file.exists()) {
                    throw new RuntimeException("Sound file missing");
                }
                else {
                    //Objects.log.log(Level.INFO, "Added sound '" + Objects.PREFIX + sound + ".ogg' as " + file.getPath());
                    Objects.log.log(Level.WARNING, "Couldn't add sound '" + Objects.PREFIX + sound + ".ogg'");
                    //event.manager.addSound((Objects.PREFIX + sound).replace(".", "/") + ".ogg", file);
                }
            }
            catch (Exception e) {
                Objects.log.log(Level.WARNING, "Failed loading sound " + sound + ".ogg", e);
            }
        }
    }

}
