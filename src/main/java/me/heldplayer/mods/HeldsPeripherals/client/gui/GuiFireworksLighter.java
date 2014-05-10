
package me.heldplayer.mods.HeldsPeripherals.client.gui;

import me.heldplayer.mods.HeldsPeripherals.inventory.ContainerFireworksLauncher;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.specialattack.forge.core.client.GuiHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFireworksLighter extends GuiContainer {
    private TileEntityFireworksLighter lighter;
    private InventoryPlayer playerInv;

    public static final ResourceLocation background = new ResourceLocation("heldsperipherals:textures/gui/container/gui_fireworks.png");

    public GuiFireworksLighter(EntityPlayer player, TileEntityFireworksLighter lighter) {
        super(new ContainerFireworksLauncher(player.inventory, lighter));

        this.lighter = lighter;
        this.playerInv = player.inventory;
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawScreen(x, y, partialTicks);

        RenderHelper.disableStandardItemLighting();

        if (x > 60 + this.guiLeft && x < 79 + this.guiLeft && y > 13 + this.guiTop && y < 49 + this.guiTop) {
            GuiHelper.drawTooltip(GuiHelper.getFluidString(this.lighter.getTank(0)), this.fontRendererObj, x, y, this.guiTop, this.height);
        }

        if (x > 96 + this.guiLeft && x < 115 + this.guiLeft && y > 13 + this.guiTop && y < 49 + this.guiTop) {
            GuiHelper.drawTooltip(GuiHelper.getFluidString(this.lighter.getTank(1)), this.fontRendererObj, x, y, this.guiTop, this.height);
        }

        if (x > 132 + this.guiLeft && x < 151 + this.guiLeft && y > 13 + this.guiTop && y < 49 + this.guiTop) {
            GuiHelper.drawTooltip(GuiHelper.getFluidString(this.lighter.getTank(2)), this.fontRendererObj, x, y, this.guiTop, this.height);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        this.fontRendererObj.drawString(this.lighter.hasCustomInventoryName() ? this.lighter.getInventoryName() : StatCollector.translateToLocal(this.lighter.getInventoryName()), 8, 6, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal(this.playerInv.getInventoryName()), 8, this.ySize - 96 + 2, 4210752);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.renderEngine.bindTexture(background);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        FluidStack stack = this.lighter.getTank(0).getFluid();
        if (stack != null && stack.amount > 0) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 2000);
            GuiHelper.drawFluid(stack.getFluid(), this.guiLeft + 62, this.guiTop + 15 + 32 - scaled, 16, scaled);
        }

        stack = this.lighter.getTank(1).getFluid();
        if (stack != null && stack.amount > 0) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 2000);
            GuiHelper.drawFluid(stack.getFluid(), this.guiLeft + 98, this.guiTop + 15 + 32 - scaled, 16, scaled);
        }

        stack = this.lighter.getTank(2).getFluid();
        if (stack != null && stack.amount > 0) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 2000);
            GuiHelper.drawFluid(stack.getFluid(), this.guiLeft + 134, this.guiTop + 15 + 32 - scaled, 16, scaled);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.renderEngine.bindTexture(background);

        this.drawTexturedModalRect(this.guiLeft + 61, this.guiTop + 14, this.xSize, 0, 18, 34);
        this.drawTexturedModalRect(this.guiLeft + 97, this.guiTop + 14, this.xSize, 0, 18, 34);
        this.drawTexturedModalRect(this.guiLeft + 133, this.guiTop + 14, this.xSize, 0, 18, 34);
    }

}
