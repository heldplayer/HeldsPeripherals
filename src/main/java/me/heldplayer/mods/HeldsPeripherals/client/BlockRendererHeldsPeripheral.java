package me.heldplayer.mods.HeldsPeripherals.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import me.heldplayer.mods.HeldsPeripherals.block.BlockHeldsPeripheral;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.specialattack.forge.core.client.RenderHelper;
import org.lwjgl.opengl.GL11;

public class BlockRendererHeldsPeripheral implements ISimpleBlockRenderingHandler {

    public final int renderId;

    public BlockRendererHeldsPeripheral(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block theBlock, int metadata, int modelID, RenderBlocks renderer) {
        BlockHeldsPeripheral block = (BlockHeldsPeripheral) theBlock;

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        int meta = metadata & 0x3;

        if (meta == 0) {
            renderer.uvRotateTop = 0;
            renderer.uvRotateBottom = 0;
        }
        if (meta == 1) {
            renderer.uvRotateTop = 1;
            renderer.uvRotateBottom = 2;
        }
        if (meta == 2) {
            renderer.uvRotateTop = 3;
            renderer.uvRotateBottom = 3;
        }
        if (meta == 3) {
            renderer.uvRotateTop = 2;
            renderer.uvRotateBottom = 1;
        }

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(0, metadata), false));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(1, metadata), false));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(5, metadata), false));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(4, metadata), false));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(2, metadata), false));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0, 0, 0, RenderHelper.getIconSafe(block.getIcon(3, metadata), false));

        tessellator.draw();

        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z) & 0x3;

        if (meta == 0) {
            renderer.uvRotateTop = 0;
            renderer.uvRotateBottom = 0;
        }
        if (meta == 1) {
            renderer.uvRotateTop = 1;
            renderer.uvRotateBottom = 2;
        }
        if (meta == 2) {
            renderer.uvRotateTop = 3;
            renderer.uvRotateBottom = 3;
        }
        if (meta == 3) {
            renderer.uvRotateTop = 2;
            renderer.uvRotateBottom = 1;
        }

        boolean rendered = renderer.renderStandardBlock(block, x, y, z);

        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;

        return rendered;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

}
