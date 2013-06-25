
package me.heldplayer.mods.HeldsPeripherals.client;

import java.lang.reflect.Constructor;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRendererEnderCharge implements IItemRenderer {

    private FontRenderer font;

    public ItemRendererEnderCharge() {
        try {
            Class<FontRenderer> clazz = FontRenderer.class;

            Constructor<FontRenderer> constructor = clazz.getDeclaredConstructor();

            constructor.setAccessible(true);

            this.font = (FontRenderer) constructor.newInstance();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (item.itemID == Objects.itemEnderCharge.itemID && type == ItemRenderType.INVENTORY) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.INVENTORY && item.itemID == Objects.itemEnderCharge.itemID) {
            Icon icon = item.getIconIndex();

            Tessellator tes = Tessellator.instance;
            tes.startDrawingQuads();
            tes.addVertexWithUV(0.0D, 16.0D, 0.0D, icon.getMinU(), icon.getMaxV());
            tes.addVertexWithUV(16.0D, 16.0D, 0.0D, icon.getMaxU(), icon.getMaxV());
            tes.addVertexWithUV(16.0D, 0.0D, 0.0D, icon.getMaxU(), icon.getMinV());
            tes.addVertexWithUV(0.0D, 0.0D, 0.0D, icon.getMinU(), icon.getMinV());
            tes.draw();

            if (item.getItemDamage() > 0 && this.font != null) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);

                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("" + (item.getItemDamage() + 1), 2, 2, 0xFFFFFF);
            }

        }
    }

}
