
package me.heldplayer.mods.HeldsPeripherals.block;

import java.util.Random;

import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.api.IHeldsPeripheral;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityNoiseMaker;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMulti1 extends BlockHeldsPeripheral {

    private Random rnd = new Random();

    private Icon top[];
    private Icon bottom[];
    private Icon front[];
    private Icon back[];
    private Icon left[];
    private Icon right[];

    public BlockMulti1(int blockId) {
        super(blockId);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int meta = world.getBlockMetadata(x, y, z);

        int rotation = ((MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3) + 2) % 4;

        world.setBlockMetadataWithNotify(x, y, z, rotation + (meta & 0xC), 3);

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

            if (tileEntity != null && (tileEntity instanceof IHeldsPeripheral)) {
                ((IHeldsPeripheral) tileEntity).setName(stack.getDisplayName());
            }
        }
    }

    @Override
    public Icon getIcon(int side, int metadata) {
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
    public boolean hasTileEntity(int metadata) {
        int type = (metadata >> 2 & 0x3);

        if (type == 0 || type == 1) {
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        int type = (metadata >> 2 & 0x3);

        if (type == 0) {
            return new TileEntityFireworksLighter();
        }
        else if (type == 1) {
            return new TileEntityNoiseMaker();
        }

        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        int meta = world.getBlockMetadata(x, y, z);

        if ((meta >> 2 & 0x3) != 0) {
            return false;
        }

        if (world.isRemote) {
            return true;
        }
        else {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

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
    public int damageDropped(int metadata) {
        return metadata & 0xC;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        if (world.isRemote) {
            return;
        }

        TileEntityFireworksLighter lighter = null;

        try {
            lighter = (TileEntityFireworksLighter) world.getBlockTileEntity(x, y, z);
        }
        catch (ClassCastException ex) {
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
                        EntityItem item = new EntityItem(world, (x + xMotion), (y + yMotion), (z + zMotion), new ItemStack(stack.itemID, size, stack.getItemDamage()));

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

        if (world.getBlockTileEntity(x, y, z) != null) {
            world.removeBlockTileEntity(x, y, z);
        }

        super.breakBlock(world, x, y, z, blockId, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        String[] prefixes = new String[] { "fireworkslighter", "noisemaker" };
        this.top = new Icon[prefixes.length];
        this.bottom = new Icon[prefixes.length];
        this.front = new Icon[prefixes.length];
        this.back = new Icon[prefixes.length];
        this.left = new Icon[prefixes.length];
        this.right = new Icon[prefixes.length];

        for (int i = 0; i < prefixes.length; i++) {
            this.top[i] = register.registerIcon("heldsperipherals:" + prefixes[i] + "_top");
            this.bottom[i] = register.registerIcon("heldsperipherals:" + prefixes[i] + "_bottom");
            this.front[i] = register.registerIcon("heldsperipherals:" + prefixes[i] + "_front");
            this.back[i] = register.registerIcon("heldsperipherals:" + prefixes[i] + "_back");
            this.left[i] = register.registerIcon("heldsperipherals:" + prefixes[i] + "_left");
            this.right[i] = register.registerIcon("heldsperipherals:" + prefixes[i] + "_right");
        }
    }

}
