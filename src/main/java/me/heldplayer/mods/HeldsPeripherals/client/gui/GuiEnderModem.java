package me.heldplayer.mods.HeldsPeripherals.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.heldplayer.mods.HeldsPeripherals.Assets;
import me.heldplayer.mods.HeldsPeripherals.inventory.ContainerEnderModem;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityEnderModem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.specialattack.forge.core.client.GuiHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiEnderModem extends GuiContainer {

    private TileEntityEnderModem modem;
    private InventoryPlayer playerInv;

    public GuiEnderModem(EntityPlayer player, TileEntityEnderModem modem) {
        super(new ContainerEnderModem(player.inventory, modem));

        this.modem = modem;
        this.playerInv = player.inventory;
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawScreen(x, y, partialTicks);

        RenderHelper.disableStandardItemLighting();

        if (x > 150 + this.guiLeft && x < 169 + this.guiLeft && y > 35 + this.guiTop && y < 71 + this.guiTop) {
            GuiHelper.drawTooltip(GuiHelper.getFluidString(this.modem.getFluidTank()), this.fontRendererObj, x, y, this.guiTop, this.height);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        this.fontRendererObj.drawString(this.modem.hasCustomInventoryName() ? this.modem.getInventoryName() : StatCollector.translateToLocal(this.modem.getInventoryName()), 8, 6, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal(this.playerInv.getInventoryName()), 8, this.ySize - 96 + 2, 0xFF404040);

        this.fontRendererObj.drawString("Remaining Sends: " + (this.modem.chargeCostSend == -1 ? "\u221E" : this.modem.chargeCostSend), 28, 17, 0xFF606060);
        this.fontRendererObj.drawString("Remaining Transports: " + (this.modem.chargeCostTransport == -1 ? "\u221E" : this.modem.chargeCostTransport), 28, 27, 0xFF606060);
        this.fontRendererObj.drawString("Remaining Fluid Tps: " + (this.modem.chargeCostTransportFluid == -1 ? "\u221E" : this.modem.chargeCostTransportFluid), 28, 37, 0xFF606060);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        this.mc.renderEngine.bindTexture(Assets.GUI_MODEM);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int height = GuiHelper.getScaled(11, this.modem.charge, 120);
        this.drawTexturedModalRect(this.guiLeft + 28, this.guiTop + 67 - height, 176, 24 - height, 12, height);

        FluidStack stack = this.modem.getFluidTank().getFluid();

        if (stack != null) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 4000);
            GuiHelper.drawFluid(stack.getFluid(), this.guiLeft + 152, this.guiTop + 37 + 32 - scaled, 16, scaled);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.renderEngine.bindTexture(Assets.GUI_MODEM);

        this.drawTexturedModalRect(this.guiLeft + 151, this.guiTop + 36, this.xSize, 24, 18, 34);
    }

}
