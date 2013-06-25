
package me.heldplayer.mods.HeldsPeripherals.client.gui;

import java.util.ArrayList;
import java.util.List;

import me.heldplayer.mods.HeldsPeripherals.client.ClientProxy;
import me.heldplayer.mods.HeldsPeripherals.inventory.ContainerTransWorldModem;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityTransWorldModem;
import me.heldplayer.util.HeldCore.client.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.liquids.LiquidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTransWorldModem extends GuiContainer {
    private TileEntityTransWorldModem modem;
    private InventoryPlayer playerInv;

    public GuiTransWorldModem(EntityPlayer player, TileEntityTransWorldModem modem) {
        super(new ContainerTransWorldModem(player.inventory, modem));

        this.modem = modem;
        this.playerInv = player.inventory;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawScreen(x, y, partialTicks);

        RenderHelper.disableStandardItemLighting();

        if (x > 150 + this.guiLeft && x < 169 + this.guiLeft && y > 35 + this.guiTop && y < 71 + this.guiTop) {
            ArrayList<String> messages = new ArrayList<String>();

            LiquidStack stack = this.modem.getTanks(null)[0].getLiquid();

            if (stack != null) {
                ItemStack itemStack = stack.asItemStack();

                if (itemStack.getItem() != null) {
                    List tooltip = itemStack.getTooltip(null, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);

                    String suffix = "";

                    if (ClientProxy.renderItemId()) {
                        suffix = " " + itemStack.itemID;

                        if (itemStack.getItemDamage() != 0) {
                            suffix += ":" + itemStack.getItemDamage();
                        }
                    }

                    tooltip.set(0, tooltip.get(0) + suffix);

                    messages.addAll(tooltip);
                    messages.add(stack.amount + " / " + this.modem.getTanks(null)[0].getCapacity() + " mB");
                }
                else {
                    messages.add("Empty");
                    messages.add("0 / " + this.modem.getTanks(null)[0].getCapacity() + " mB");
                }
            }
            else {
                messages.add("Empty");
                messages.add("0 / " + this.modem.getTanks(null)[0].getCapacity() + " mB");
            }

            this.zLevel = 300.0F;
            GuiHelper.drawTooltip(messages, this.fontRenderer, x, y, this.guiTop, this.height);
            this.zLevel = 0.0F;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        this.fontRenderer.drawString(this.modem.isInvNameLocalized() ? this.modem.getInvName() : StatCollector.translateToLocal(this.modem.getInvName()), 8, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal(this.playerInv.getInvName()), 8, this.ySize - 96 + 2, 0xFF404040);

        this.fontRenderer.drawString("Remaining Sends: " + (this.modem.chargeCostSend == -1 ? "\u221E" : this.modem.chargeCostSend), 28, 17, 0xFF606060);
        this.fontRenderer.drawString("Remaining Transports: " + (this.modem.chargeCostTransport == -1 ? "\u221E" : this.modem.chargeCostTransport), 28, 27, 0xFF606060);
        this.fontRenderer.drawString("Remaining Liquid Tps: " + (this.modem.chargeCostTransportLiquid == -1 ? "\u221E" : this.modem.chargeCostTransportLiquid), 28, 37, 0xFF606060);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        this.mc.renderEngine.bindTexture(ClientProxy.textureLocation + "gui_modem.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int height = GuiHelper.getScaled(11, this.modem.charge, 120);
        this.drawTexturedModalRect(this.guiLeft + 28, this.guiTop + 67 - height, 176, 24 - height, 12, height);

        LiquidStack stack = this.modem.getTanks(null)[0].getLiquid();

        if (stack != null) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 4000);
            GuiHelper.drawLiquid(stack.itemID, stack.itemMeta, this.guiLeft + 152, this.guiTop + 37 + 32 - scaled, 16, scaled);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.renderEngine.bindTexture(ClientProxy.textureLocation + "gui_modem.png");

        this.drawTexturedModalRect(this.guiLeft + 151, this.guiTop + 36, this.xSize, 24, 18, 34);
    }

}
