
package me.heldplayer.mods.HeldsPeripherals.common.entity;

import me.heldplayer.mods.HeldsPeripherals.client.particle.EntityFireworkStarterFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityFireworkRocket extends Entity {
    private int age;
    private int maxAge;

    public EntityFireworkRocket(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    public EntityFireworkRocket(World world, double posX, double posY, double posZ, ItemStack stack) {
        super(world);
        this.age = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(posX, posY, posZ);
        this.yOffset = 0.0F;
        int length = 1;

        if (stack != null && stack.hasTagCompound()) {
            this.dataWatcher.updateObject(8, stack);
            NBTTagCompound compound = stack.getTagCompound();
            NBTTagCompound dataCompound = compound.getCompoundTag("Fireworks");

            if (dataCompound != null) {
                length += dataCompound.getShort("Flight");
            }

            boolean hideTrail = compound.getBoolean("HideTrail");

            this.dataWatcher.updateObject(9, Byte.valueOf((byte) (hideTrail ? 1 : 0)));
        }

        this.motionX = this.rand.nextGaussian() * 0.001D;
        this.motionZ = this.rand.nextGaussian() * 0.001D;
        this.motionY = 0.05D;
        this.maxAge = length;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(8, new ItemStack(0, 0, 0));
        this.dataWatcher.addObject(9, Byte.valueOf((byte) 0));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 4096.0D;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setVelocity(double par1, double par3, double par5) {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) var7) * 180.0D / Math.PI);
        }
    }

    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        //this.motionX *= 1.15D;
        //this.motionZ *= 1.15D;
        this.motionY += 0.04D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        boolean hideTrail = this.dataWatcher.getWatchableObjectByte(9) == 1;

        if (this.age == 0 && !hideTrail) {
            this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
        }

        ++this.age;

        if (this.worldObj.isRemote && this.age % 2 < 2 && !hideTrail) {
            this.worldObj.spawnParticle("fireworksSpark", this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D);
        }

        if (!this.worldObj.isRemote && this.age > this.maxAge) {
            this.worldObj.setEntityState(this, (byte) 17);
            this.setDead();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleHealthUpdate(byte health) {
        if (health == 17 && this.worldObj.isRemote) {
            ItemStack stack = this.dataWatcher.getWatchableObjectItemStack(8);
            NBTTagCompound component = null;

            if (stack != null && stack.hasTagCompound()) {
                component = stack.getTagCompound().getCompoundTag("Fireworks");
            }

            EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
            effectRenderer.addEffect(new EntityFireworkStarterFX(this.worldObj, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, effectRenderer, component));
        }

        super.handleHealthUpdate(health);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Life", this.age);
        compound.setInteger("LifeTime", this.maxAge);
        compound.setByte("HideTrail", this.dataWatcher.getWatchableObjectByte(9));
        ItemStack stack = this.dataWatcher.getWatchableObjectItemStack(8);

        if (stack != null) {
            NBTTagCompound component = new NBTTagCompound();
            stack.writeToNBT(component);
            compound.setCompoundTag("FireworksItem", component);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.age = compound.getInteger("Life");
        this.maxAge = compound.getInteger("LifeTime");
        this.dataWatcher.updateObject(9, Byte.valueOf(compound.getByte("HideTrail")));
        NBTTagCompound component = compound.getCompoundTag("FireworksItem");

        if (component != null) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(component);

            if (stack != null) {
                this.dataWatcher.updateObject(8, stack);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    public float getBrightness(float partialTicks) {
        return super.getBrightness(partialTicks);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender(float partialTicks) {
        return super.getBrightnessForRender(partialTicks);
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}
