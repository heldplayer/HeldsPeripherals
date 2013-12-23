
package me.heldplayer.mods.HeldsPeripherals.client.particle;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityFireworkSparkFX extends EntityFX {
    private int particleIndex = 160;
    private boolean hasTrail;
    private boolean hasFlicker;
    private final EffectRenderer effectRenderer;
    private float fadeRed;
    private float fadeGreen;
    private float fadeBlue;
    private boolean hasFadeColor;
    private final EntityFireworkStarterFX parent;

    public EntityFireworkSparkFX(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, EffectRenderer effectRenderer, EntityFireworkStarterFX parent) {
        super(world, posX, posY, posZ);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.effectRenderer = effectRenderer;
        this.particleScale *= 0.75F;
        this.particleMaxAge = 48 + this.rand.nextInt(12);
        this.noClip = false;
        this.parent = parent;
    }

    public void setHasTrail(boolean value) {
        this.hasTrail = value;
    }

    public void setHasFlicker(boolean value) {
        this.hasFlicker = value;
    }

    public void setMainColor(int color) {
        float red = (float) ((color & 16711680) >> 16) / 255.0F;
        float green = (float) ((color & 65280) >> 8) / 255.0F;
        float blue = (float) ((color & 255) >> 0) / 255.0F;
        float brightness = 1.0F;
        this.setRBGColorF(red * brightness, green * brightness, blue * brightness);
    }

    public void setFadeColor(int color) {
        this.fadeRed = (float) ((color & 16711680) >> 16) / 255.0F;
        this.fadeGreen = (float) ((color & 65280) >> 8) / 255.0F;
        this.fadeBlue = (float) ((color & 255) >> 0) / 255.0F;
        this.hasFadeColor = true;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void renderParticle(Tessellator tess, float partialTicks, float cosYaw, float cosPitch, float sinYaw, float xsinPitch, float sinPitch) {
        if (!this.hasFlicker || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
            super.renderParticle(tess, partialTicks, cosYaw, cosPitch, sinYaw, xsinPitch, sinPitch);
        }
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }

        if (this.particleAge > this.particleMaxAge / 2) {
            this.setAlphaF(1.0F - ((float) this.particleAge - (float) (this.particleMaxAge / 2)) / (float) this.particleMaxAge);

            if (this.hasFadeColor) {
                this.particleRed += (this.fadeRed - this.particleRed) * 0.2F;
                this.particleGreen += (this.fadeGreen - this.particleGreen) * 0.2F;
                this.particleBlue += (this.fadeBlue - this.particleBlue) * 0.2F;
            }
        }

        this.setParticleTextureIndex(this.particleIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY -= 0.004D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9100000262260437D;
        this.motionY *= 0.9100000262260437D;
        this.motionZ *= 0.9100000262260437D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        if (this.hasTrail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
            EntityFireworkSparkFX trail = new EntityFireworkSparkFX(this.worldObj, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, this.effectRenderer, this.parent);
            trail.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
            trail.particleAge = trail.particleMaxAge / 2;

            if (this.hasFadeColor) {
                trail.hasFadeColor = true;
                trail.fadeRed = this.fadeRed;
                trail.fadeGreen = this.fadeGreen;
                trail.fadeBlue = this.fadeBlue;
            }

            trail.hasFlicker = this.hasFlicker;

            this.parent.addChild(trail);
            //this.effectRenderer.addEffect(trail);
        }
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }

    @Override
    public float getBrightness(float partialTicks) {
        return 1.0F;
    }
}
