
package me.heldplayer.mods.HeldsPeripherals.client;

import java.util.logging.Level;

import me.heldplayer.mods.HeldsPeripherals.CommonProxy;
import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.client.gui.GuiFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.client.gui.GuiEnderModem;
import me.heldplayer.mods.HeldsPeripherals.inventory.SlotOreDictionary;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityEnderModem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
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
                if (tileEntity != null && (tileEntity instanceof TileEntityEnderModem)) {
                    return new GuiEnderModem(player, ((TileEntityEnderModem) tileEntity));
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

    @ForgeSubscribe
    public void onSoundLoaded(SoundLoadEvent event) {
        for (String sound : Objects.SOUNDS) {
            event.manager.addSound(sound + ".ogg");
            Objects.log.log(Level.INFO, "Adding sound " + sound + ".ogg");
        }
    }

    @ForgeSubscribe
    public void registerTextures(TextureStitchEvent.Pre event) {
        if (event.map.textureType == 0) {
            Objects.ICON_MOLTEN_DYE_STILL.icon = event.map.registerIcon("heldsperipherals:molten_dye_still");
            Objects.ICON_MOLTEN_DYE_FLOW.icon = event.map.registerIcon("heldsperipherals:molten_dye_flow");

            ClientProxy.fireworksUpgrade = event.map.registerIcon("heldsperipherals:fireworkslighter_peripheral");
        }
        else if (event.map.textureType == 1) {
            String[] icons = new String[] { "dust", "red", "green", "blue" };

            for (int i = 0; i < icons.length; i++) {
                ClientProxy.icons[i] = event.map.registerIcon("heldsperipherals:background_" + icons[i]);
            }
        }
    }

}
