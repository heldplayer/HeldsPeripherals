package me.heldplayer.mods.HeldsPeripherals.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import me.heldplayer.mods.HeldsPeripherals.Assets;
import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.api.IHeldsPeripheral;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityNoiseMaker;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockMulti1 extends BlockHeldsPeripheral {

    private Random rnd = new Random();

    private IIcon top[];
    private IIcon bottom[];
    private IIcon front[];
    private IIcon back[];
    private IIcon left[];
    private IIcon right[];

    public BlockMulti1() {
        super();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int meta = world.getBlockMetadata(x, y, z);

        int rotation = ((MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3) + 2) % 4;

        world.setBlockMetadataWithNotify(x, y, z, rotation + (meta & 0xC), 3);

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);

            if (tileEntity != null && (tileEntity instanceof IHeldsPeripheral)) {
                ((IHeldsPeripheral) tileEntity).setName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        int type = (metadata >> 2 & 0x3);

        return type == 0 || type == 1;
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        int type = (metadata >> 2 & 0x3) % 2;

        if ((metadata & 0x3) == 0) {
            switch (side) {
                case 0:
                    return this.bottom[type];
                case 1:
                    return this.top[type];
                case 2:
                    return this.back[type];
                case 3:
                    return this.front[type];
                case 4:
                    return this.left[type];
                case 5:
                    return this.right[type];
            }
        }
        if ((metadata & 0x3) == 1) {
            switch (side) {
                case 0:
                    return this.bottom[type];
                case 1:
                    return this.top[type];
                case 2:
                    return this.left[type];
                case 3:
                    return this.right[type];
                case 4:
                    return this.front[type];
                case 5:
                    return this.back[type];
            }
        }
        if ((metadata & 0x3) == 2) {
            switch (side) {
                case 0:
                    return this.bottom[type];
                case 1:
                    return this.top[type];
                case 2:
                    return this.front[type];
                case 3:
                    return this.back[type];
                case 4:
                    return this.right[type];
                case 5:
                    return this.left[type];
            }
        }
        switch (side) {
            case 0:
                return this.bottom[type];
            case 1:
                return this.top[type];
            case 2:
                return this.right[type];
            case 3:
                return this.left[type];
            case 4:
                return this.back[type];
            case 5:
                return this.front[type];
        }

        return this.front[1];
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        if (world.isRemote) {
            return;
        }

        TileEntityFireworksLighter lighter;

        try {
            lighter = (TileEntityFireworksLighter) world.getTileEntity(x, y, z);
        } catch (ClassCastException ex) {
            return;
        }

        if (lighter != null) {
            for (int i = 0; i < lighter.getSizeInventory(); i++) {
                ItemStack stack = lighter.getStackInSlot(i);

                if (stack != null) {
                    float xMotion = this.rnd.nextFloat() * 0.8F + 0.1F;
                    float yMotion = this.rnd.nextFloat() * 0.8F + 0.1F;
                    float zMotion = this.rnd.nextFloat() * 0.8F + 0.1F;

                    while (stack.stackSize > 0) {
                        int size = this.rnd.nextInt(21) + 10;

                        if (size > stack.stackSize) {
                            size = stack.stackSize;
                        }

                        stack.stackSize -= size;
                        EntityItem item = new EntityItem(world, (x + xMotion), (y + yMotion), (z + zMotion), new ItemStack(stack.getItem(), size, stack.getItemDamage()));

                        if (stack.hasTagCompound()) {
                            item.setEntityItemStack(stack);
                        }

                        item.motionX = ((float) this.rnd.nextGaussian() * 0.05F);
                        item.motionY = ((float) this.rnd.nextGaussian() * 0.05F + 0.2F);
                        item.motionZ = ((float) this.rnd.nextGaussian() * 0.05F);
                        world.spawnEntityInWorld(item);
                    }
                }
            }
        }

        if (world.getTileEntity(x, y, z) != null) {
            world.removeTileEntity(x, y, z);
        }

        super.onBlockDestroyedByPlayer(world, x, y, z, meta);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata & 0xC;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        int meta = world.getBlockMetadata(x, y, z);

        if ((meta >> 2 & 0x3) != 0) {
            return false;
        }

        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(x, y, z);

            if (tileEntity instanceof TileEntityFireworksLighter) {
                if (player.isSneaking()) {
                    ((TileEntityFireworksLighter) tileEntity).toggleEasterEgg();

                    return true;
                }

                player.openGui(ModHeldsPeripherals.instance, 1, world, x, y, z);

                return true;
            }

            return false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        String[] prefixes = new String[] { "fireworkslighter", "noisemaker" };
        this.top = new IIcon[prefixes.length];
        this.bottom = new IIcon[prefixes.length];
        this.front = new IIcon[prefixes.length];
        this.back = new IIcon[prefixes.length];
        this.left = new IIcon[prefixes.length];
        this.right = new IIcon[prefixes.length];

        for (int i = 0; i < prefixes.length; i++) {
            this.top[i] = register.registerIcon(Assets.DOMAIN + prefixes[i] + "_top");
            this.bottom[i] = register.registerIcon(Assets.DOMAIN + prefixes[i] + "_bottom");
            this.front[i] = register.registerIcon(Assets.DOMAIN + prefixes[i] + "_front");
            this.back[i] = register.registerIcon(Assets.DOMAIN + prefixes[i] + "_back");
            this.left[i] = register.registerIcon(Assets.DOMAIN + prefixes[i] + "_left");
            this.right[i] = register.registerIcon(Assets.DOMAIN + prefixes[i] + "_right");
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        int type = (metadata >> 2 & 0x3);

        if (type == 0) {
            return new TileEntityFireworksLighter();
        } else if (type == 1) {
            return new TileEntityNoiseMaker();
        }

        return null;
    }

}
