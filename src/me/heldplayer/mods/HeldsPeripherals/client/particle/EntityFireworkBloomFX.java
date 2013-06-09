
package me.heldplayer.mods.HeldsPeripherals.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityFireworkBloomFX extends EntityFX {
    public EntityFireworkBloomFX(World world, double posX, double posY, double posZ) {
        super(world, posX, posY, posZ);
        this.particleMaxAge = 4;
    }

    public EntityFireworkBloomFX(World world, double posX, double posY, double posZ, int maxAge) {
        super(world, posX, posY, posZ);
        this.particleMaxAge = maxAge;
    }

    @Override
    public void renderParticle(Tessellator tess, float partialTicks, float cosYaw, float cosPitch, float sinYaw, float xsinPitch, float sinPitch) {
        float startU = 0.25F;
        float endU = startU + 0.25F;
        float startV = 0.125F;
        float endV = startV + 0.25F;
        float size = 5.0F;
        this.particleAlpha = 0.6F - ((float) this.particleAge + partialTicks - 1.0F) * 0.25F * 0.5F * 4.0F / (float) this.particleMaxAge;
        if (this.particleMaxAge <= 4) {
            size = 7.1F * MathHelper.sin(((float) this.particleAge + partialTicks - 1.0F) * 0.25F * (float) Math.PI);
            this.particleAlpha = 0.6F - ((float) this.particleAge + partialTicks - 1.0F) * 0.25F * 0.5F;
        }
        float posX = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float posY = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float posZ = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        tess.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tess.addVertexWithUV((double) (posX - cosYaw * size - xsinPitch * size), (double) (posY - cosPitch * size), (double) (posZ - sinYaw * size - sinPitch * size), (double) endU, (double) endV);
        tess.addVertexWithUV((double) (posX - cosYaw * size + xsinPitch * size), (double) (posY + cosPitch * size), (double) (posZ - sinYaw * size + sinPitch * size), (double) endU, (double) startV);
        tess.addVertexWithUV((double) (posX + cosYaw * size + xsinPitch * size), (double) (posY + cosPitch * size), (double) (posZ + sinYaw * size + sinPitch * size), (double) startU, (double) startV);
        tess.addVertexWithUV((double) (posX + cosYaw * size - xsinPitch * size), (double) (posY - cosPitch * size), (double) (posZ + sinYaw * size - sinPitch * size), (double) startU, (double) endV);
    }
}
