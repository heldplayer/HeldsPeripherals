
package me.heldplayer.mods.HeldsPeripherals.client.gui;

import java.util.ArrayList;
import java.util.List;

import me.heldplayer.mods.HeldsPeripherals.client.ClientProxy;
import me.heldplayer.mods.HeldsPeripherals.common.inventory.ContainerFireworksLauncher;
import me.heldplayer.mods.HeldsPeripherals.common.tileentity.TileEntityFireworksLighter;
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
public class GuiFireworksLighter extends GuiContainer {
    private TileEntityFireworksLighter lighter;
    private InventoryPlayer playerInv;

    public GuiFireworksLighter(EntityPlayer player, TileEntityFireworksLighter lighter) {
        super(new ContainerFireworksLauncher(player.inventory, lighter));

        this.lighter = lighter;
        this.playerInv = player.inventory;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawScreen(x, y, partialTicks);

        RenderHelper.disableStandardItemLighting();

        if (x > 60 + this.guiLeft && x < 79 + this.guiLeft && y > 13 + this.guiTop && y < 49 + this.guiTop) {
            ArrayList<String> messages = new ArrayList<String>();

            LiquidStack stack = this.lighter.getTank(0).getLiquid();

            if (stack != null) {
                ItemStack itemStack = stack.asItemStack();

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
                messages.add(stack.amount + " / " + this.lighter.getTank(1).getCapacity() + " mB");
            }
            else {
                messages.add("Empty");
                messages.add("0 / " + this.lighter.getTank(0).getCapacity() + " mB");
            }

            GuiHelper.drawTooltip(messages, this.fontRenderer, x, y, this.guiTop, this.height);
        }

        if (x > 96 + this.guiLeft && x < 115 + this.guiLeft && y > 13 + this.guiTop && y < 49 + this.guiTop) {
            ArrayList<String> messages = new ArrayList<String>();

            LiquidStack stack = this.lighter.getTank(1).getLiquid();

            if (stack != null) {
                ItemStack itemStack = stack.asItemStack();

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
                messages.add(stack.amount + " / " + this.lighter.getTank(1).getCapacity() + " mB");
            }
            else {
                messages.add("Empty");
                messages.add("0 / " + this.lighter.getTank(1).getCapacity() + " mB");
            }

            GuiHelper.drawTooltip(messages, this.fontRenderer, x, y, this.guiTop, this.height);
        }

        if (x > 132 + this.guiLeft && x < 151 + this.guiLeft && y > 13 + this.guiTop && y < 49 + this.guiTop) {
            ArrayList<String> messages = new ArrayList<String>();

            LiquidStack stack = this.lighter.getTank(2).getLiquid();

            if (stack != null) {
                ItemStack itemStack = stack.asItemStack();

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
                messages.add(stack.amount + " / " + this.lighter.getTank(1).getCapacity() + " mB");
            }
            else {
                messages.add("Empty");
                messages.add("0 / " + this.lighter.getTank(2).getCapacity() + " mB");
            }

            GuiHelper.drawTooltip(messages, this.fontRenderer, x, y, this.guiTop, this.height);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        this.fontRenderer.drawString(this.lighter.isInvNameLocalized() ? this.lighter.getInvName() : StatCollector.translateToLocal(this.lighter.getInvName()), 8, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal(this.playerInv.getInvName()), 8, this.ySize - 96 + 2, 4210752);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.renderEngine.bindTexture(ClientProxy.textureLocation + "gui_fireworks.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        LiquidStack stack = this.lighter.getTank(0).getLiquid();
        if (stack != null && stack.amount > 0) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 2000);
            GuiHelper.drawLiquid(stack.itemID, stack.itemMeta, this.guiLeft + 62, this.guiTop + 15 + 32 - scaled, 16, scaled);
        }

        stack = this.lighter.getTank(1).getLiquid();
        if (stack != null && stack.amount > 0) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 2000);
            GuiHelper.drawLiquid(stack.itemID, stack.itemMeta, this.guiLeft + 98, this.guiTop + 15 + 32 - scaled, 16, scaled);
        }

        stack = this.lighter.getTank(2).getLiquid();
        if (stack != null && stack.amount > 0) {
            int scaled = GuiHelper.getScaled(32, stack.amount, 2000);
            GuiHelper.drawLiquid(stack.itemID, stack.itemMeta, this.guiLeft + 134, this.guiTop + 15 + 32 - scaled, 16, scaled);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.renderEngine.bindTexture(ClientProxy.textureLocation + "gui_fireworks.png");

        this.drawTexturedModalRect(this.guiLeft + 61, this.guiTop + 14, this.xSize, 0, 18, 34);
        this.drawTexturedModalRect(this.guiLeft + 97, this.guiTop + 14, this.xSize, 0, 18, 34);
        this.drawTexturedModalRect(this.guiLeft + 133, this.guiTop + 14, this.xSize, 0, 18, 34);
    }

}
