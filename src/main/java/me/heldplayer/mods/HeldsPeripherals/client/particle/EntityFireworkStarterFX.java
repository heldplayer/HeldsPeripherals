
package me.heldplayer.mods.HeldsPeripherals.client.particle;

import java.util.ArrayList;

import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityFireworkStarterFX extends EntityFX {
    private int age = 0;
    private final EffectRenderer effectRenderer;
    private NBTTagList explosions;
    boolean flicker;
    private ArrayList<EntityFX> children;
    private boolean shouldBeDead;
    private boolean specials;
    private boolean silent;

    public EntityFireworkStarterFX(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, EffectRenderer effectRenderer, NBTTagCompound compound) {
        super(world, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.effectRenderer = effectRenderer;
        this.particleMaxAge = 8;
        if (ModHeldsPeripherals.enhancedFireworksEntity.getValue()) {
            this.children = new ArrayList<EntityFX>();
        }
        this.shouldBeDead = false;
        this.specials = true;
        this.silent = false;

        if (compound != null) {
            this.explosions = compound.getTagList("Explosions", 10);

            if (this.explosions != null && this.explosions.tagCount() == 0) {
                this.explosions = null;
            }
            else if (this.explosions != null) {
                this.particleMaxAge = this.explosions.tagCount() * 2 - 1;

                for (int var16 = 0; var16 < this.explosions.tagCount(); ++var16) {
                    NBTTagCompound explosion = this.explosions.getCompoundTagAt(var16);

                    if (explosion.getBoolean("Flicker")) {
                        this.flicker = true;
                        this.particleMaxAge += 15;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void renderParticle(Tessellator tess, float partialTicks, float cosYaw, float cosPitch, float sinYaw, float xsinPitch, float sinPitch) {
        if (this.children != null) {
            for (int i = 0; i < this.children.size(); i++) {
                EntityFX particle = this.children.get(i);
                if (!particle.isDead) {
                    particle.renderParticle(tess, partialTicks, cosYaw, cosPitch, sinYaw, xsinPitch, sinPitch);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.age == 0 && this.explosions != null) {
            boolean isFar = this.isFarAway();
            boolean large = false;

            NBTTagCompound explosion = this.explosions.getCompoundTagAt(0);
            byte type = explosion.getByte("Type");
            boolean hasTrail = explosion.getBoolean("Trail");
            boolean hasFlicker = explosion.getBoolean("Flicker");
            int[] colors = explosion.getIntArray("Colors");
            int[] fadeColors = explosion.getIntArray("FadeColors");
            NBTTagList arguments = explosion.getTagList("Arguments", 6);
            this.silent = explosion.getBoolean("Silent");

            int bloomMaxAge = 4;

            if (type == 1) {
                large = true;
            }
            if (type == 6) {
                large = true;
            }
            if (type == 5) {
                this.specials = false;
            }

            if (this.specials && !this.silent) {
                String sound = "fireworks." + (large ? "largeBlast" : "blast") + (isFar ? "_far" : "");
                this.worldObj.playSound(this.posX, this.posY, this.posZ, sound, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);
            }

            switch (type) {
            case 0:
            default:
                this.createBall(0.25D, 2, colors, fadeColors, hasTrail, hasFlicker);
            break;
            case 1:
                this.createBall(0.5D, 4, colors, fadeColors, hasTrail, hasFlicker);
            break;
            case 2:
                this.createMirroredPatern(0.5D, new double[][] { { 0.0D, 1.0D }, { 0.3455D, 0.309D }, { 0.9511D, 0.309D }, { 0.3795918367346939D, -0.12653061224489795D }, { 0.6122448979591837D, -0.8040816326530612D }, { 0.0D, -0.35918367346938773D } }, colors, fadeColors, hasTrail, hasFlicker, false);
            break;
            case 3:
                this.createMirroredPatern(0.5D, new double[][] { { 0.0D, 0.2D }, { 0.2D, 0.2D }, { 0.2D, 0.6D }, { 0.6D, 0.6D }, { 0.6D, 0.2D }, { 0.2D, 0.2D }, { 0.2D, 0.0D }, { 0.4D, 0.0D }, { 0.4D, -0.6D }, { 0.2D, -0.6D }, { 0.2D, -0.4D }, { 0.0D, -0.4D } }, colors, fadeColors, hasTrail, hasFlicker, true);
            break;
            case 4:
                this.createBurst(colors, fadeColors, hasTrail, hasFlicker);
            break;
            case 5:
                bloomMaxAge = 100;
            break;
            case 6:
                this.createBall(1.0D, 8, colors, fadeColors, hasTrail, hasFlicker);
            break;
            case 7:
                double[][] args = new double[arguments.tagCount() / 2][];

                for (int i = 0; i < arguments.tagCount() / 2; i++) {
                    args[i] = new double[2];

                    args[i][0] = arguments.func_150309_d(i * 2);
                    args[i][1] = arguments.func_150309_d(i * 2 + 1);
                }
                if (args.length > 0) {
                    this.createMirroredPatern(0.5D, args, colors, fadeColors, hasTrail, hasFlicker, true);
                }
            break;
            case 8:
                args = new double[arguments.tagCount() / 2][];

                for (int i = 0; i < arguments.tagCount() / 2; i++) {
                    args[i] = new double[2];

                    args[i][0] = arguments.func_150309_d(i * 2);
                    args[i][1] = arguments.func_150309_d(i * 2 + 1);
                }
                if (args.length > 0) {
                    this.createMirroredPatern(0.5D, args, colors, fadeColors, hasTrail, hasFlicker, false);
                }
            break;
            }

            int baseColor = colors[0];
            float baseR = (float) ((baseColor & 16711680) >> 16) / 255.0F;
            float baseG = (float) ((baseColor & 65280) >> 8) / 255.0F;
            float baseB = (float) ((baseColor & 255) >> 0) / 255.0F;
            EntityFireworkBloomFX bloom = new EntityFireworkBloomFX(this.worldObj, this.posX, this.posY, this.posZ, bloomMaxAge);
            bloom.setRBGColorF(baseR, baseG, baseB);
            //this.effectRenderer.addEffect(bloom);
            this.addChild(bloom);
        }

        boolean stillAlive = false;

        if (this.children != null) {
            for (int i = 0; i < this.children.size(); i++) {
                EntityFX particle = this.children.get(i);
                if (!particle.isDead) {
                    particle.onUpdate();
                    stillAlive = true;
                }
            }
        }

        ++this.age;

        if (this.age > this.particleMaxAge && !this.shouldBeDead) {
            if (this.flicker && this.specials && !this.silent) {
                boolean isFar = this.isFarAway();
                String sound = "fireworks." + (isFar ? "twinkle_far" : "twinkle");
                this.worldObj.playSound(this.posX, this.posY, this.posZ, sound, 20.0F, 0.9F + this.rand.nextFloat() * 0.15F, true);
            }

            this.shouldBeDead = true;
        }

        if (!stillAlive) {
            this.setDead();
        }
    }

    private boolean isFarAway() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc == null || mc.renderViewEntity == null || mc.renderViewEntity.getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0D;
    }

    private void createSpark(double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int[] colors, int[] fadeColors, boolean hasTrail, boolean hasFlicker) {
        EntityFireworkSparkFX spark = new EntityFireworkSparkFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ, this.effectRenderer, this);
        spark.setHasTrail(hasTrail);
        spark.setHasFlicker(hasFlicker);
        spark.setMainColor(colors[this.rand.nextInt(colors.length)]);

        if (fadeColors != null && fadeColors.length > 0) {
            spark.setFadeColor(fadeColors[this.rand.nextInt(fadeColors.length)]);
        }

        //this.effectRenderer.addEffect(spark);
        this.addChild(spark);
    }

    private void createBall(double motion, int size, int[] colors, int[] fadeColors, boolean hasTrail, boolean hasFlicker) {
        double posX = this.posX;
        double posY = this.posY;
        double posZ = this.posZ;

        for (int y = -size; y <= size; y++) {
            for (int x = -size; x <= size; x++) {
                for (int z = -size; z <= size; z++) {
                    double motionX = (double) x + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                    double motionY = (double) y + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                    double motionZ = (double) z + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                    double motionFactor = (double) MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ) / motion + this.rand.nextGaussian() * 0.05D;
                    this.createSpark(posX, posY, posZ, motionX / motionFactor, motionY / motionFactor, motionZ / motionFactor, colors, fadeColors, hasTrail, hasFlicker);

                    if (y != -size && y != size && x != -size && x != size) {
                        z += size * 2 - 1;
                    }
                }
            }
        }
    }

    private void createMirroredPatern(double motionMultiplier, double[][] targets, int[] colors, int[] fadeColors, boolean hasTrail, boolean hasFlicker, boolean rotateable) {
        double baseX = targets[0][0];
        double baseY = targets[0][1];
        this.createSpark(this.posX, this.posY, this.posZ, baseX * motionMultiplier, baseY * motionMultiplier, 0.0D, colors, fadeColors, hasTrail, hasFlicker);
        float baseRotation = this.rand.nextFloat() * (float) Math.PI;
        double rotateAmount = rotateable ? 0.034D : 0.34D;

        for (int iteration = 0; iteration < 3; ++iteration) {
            double rotation = (double) baseRotation + (double) ((float) iteration * (float) Math.PI) * rotateAmount;
            double currentX = baseX;
            double currentY = baseY;

            for (int index = 1; index < targets.length; ++index) {
                double nextX = targets[index][0];
                double nextY = targets[index][1];

                for (double steps = 0.25D; steps <= 1.0D; steps += 0.25D) {
                    double motionX = (currentX + (nextX - currentX) * steps) * motionMultiplier;
                    double motionY = (currentY + (nextY - currentY) * steps) * motionMultiplier;
                    double motionZ = motionX * Math.sin(rotation);
                    motionX *= Math.cos(rotation);

                    for (double pos = -1.0D; pos <= 1.0D; pos += 2.0D) {
                        this.createSpark(this.posX, this.posY, this.posZ, motionX * pos, motionY, motionZ * pos, colors, fadeColors, hasTrail, hasFlicker);
                    }
                }

                currentX = nextX;
                currentY = nextY;
            }
        }
    }

    private void createBurst(int[] colors, int[] fadeColors, boolean hasTrail, boolean hasFlicker) {
        double offsetX = this.rand.nextGaussian() * 0.05D;
        double offsetZ = this.rand.nextGaussian() * 0.05D;

        for (int var9 = 0; var9 < 70; ++var9) {
            double motionX = this.motionX * 0.5D + this.rand.nextGaussian() * 0.15D + offsetX;
            double motionZ = this.motionZ * 0.5D + this.rand.nextGaussian() * 0.15D + offsetZ;
            double motionY = this.motionY * 0.5D + this.rand.nextDouble() * 0.5D;
            this.createSpark(this.posX, this.posY, this.posZ, motionX, motionY, motionZ, colors, fadeColors, hasTrail, hasFlicker);
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

    protected void addChild(EntityFX entity) {
        if (this.children != null) {
            this.children.add(entity);
        }
        else {
            this.effectRenderer.addEffect(entity);
        }
    }

}
