
package me.heldplayer.mods.HeldsPeripherals.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.heldplayer.mods.HeldsPeripherals.common.Objects;
import me.heldplayer.mods.HeldsPeripherals.common.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.common.tileentity.TileEntityTransWorldModem;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockTransWorldModem extends BlockHeldsPeripheral {
    private Random rnd = new Random();

    //private int topIndex = 16;
    //private int bottomIndex = 0;
    //private int[] frontIndex = new int[] { 32, 33, 34, 35 };
    //private int[] backIndex = new int[] { 48, 49 };
    //private int[] leftIndex = new int[] { 64, 65 };
    //private int[] rightIndex = new int[] { 80, 81 };

    private Icon top[] = new Icon[4];
    private Icon bottom[] = new Icon[4];
    private Icon front[] = new Icon[4];
    private Icon back[] = new Icon[4];
    private Icon left[] = new Icon[4];
    private Icon right[] = new Icon[4];

    public BlockTransWorldModem(int blockId) {
        super(blockId);
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        // Bytes: ABCD
        // AB = orientation
        // C = holding power
        // D = holding reserve

        // Sides
        // 0: bottom
        // 1: top
        // 2: North
        // 3: South
        // 4: West
        // 5: East

        int offset = 0x0;

        if (((metadata >> 2) & 0x1) == 1) {
            offset = offset & 0x1;
        }
        if (((metadata >> 3) & 0x1) == 1) {
            offset = offset & 0x2;
        }

        if (side == 0) {
            return bottom[offset];
        }
        if (side == 1) {
            return top[offset];
        }

        metadata = metadata & 0x3;

        if (metadata == 0) {
            switch (side) {
            case 2:
                return this.back[offset];
            case 3:
                return this.front[offset];
            case 4:
                return this.left[offset];
            case 5:
                return this.right[offset];
            }
        }
        if (metadata == 1) {
            switch (side) {
            case 2:
                return this.left[offset];
            case 3:
                return this.right[offset];
            case 4:
                return this.front[offset];
            case 5:
                return this.back[offset];
            }
        }
        if (metadata == 2) {
            switch (side) {
            case 2:
                return this.front[offset];
            case 3:
                return this.back[offset];
            case 4:
                return this.right[offset];
            case 5:
                return this.left[offset];
            }
        }
        switch (side) {
        case 2:
            return this.right[offset];
        case 3:
            return this.left[offset];
        case 4:
            return this.back[offset];
        case 5:
            return this.front[offset];
        }

        return this.front[3];
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
        if (world.isRemote) {
            return true;
        }
        else {
            TileEntityTransWorldModem tileEntity = (TileEntityTransWorldModem) world.getBlockTileEntity(x, y, z);

            if (tileEntity != null) {
                player.openGui(ModHeldsPeripherals.instance, 0, world, x, y, z);
            }

            return true;
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityTransWorldModem();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        if (world.isRemote) {
            return;
        }

        TileEntityTransWorldModem modem = (TileEntityTransWorldModem) world.getBlockTileEntity(x, y, z);

        if (modem != null) {
            for (int i = 0; i < modem.getSizeInventory(); i++) {
                ItemStack stack = modem.getStackInSlot(i);

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

            while (modem.charge > 0) {
                float xMotion = this.rnd.nextFloat() * 0.8F + 0.1F;
                float yMotion = this.rnd.nextFloat() * 0.8F + 0.1F;
                float zMotion = this.rnd.nextFloat() * 0.8F + 0.1F;

                EntityItem var14 = new EntityItem(world, (x + xMotion), (y + yMotion), (z + zMotion), new ItemStack(Objects.itemEnderCharge, 1));

                var14.motionX = ((float) this.rnd.nextGaussian() * 0.05F);
                var14.motionY = ((float) this.rnd.nextGaussian() * 0.05F + 0.2F);
                var14.motionZ = ((float) this.rnd.nextGaussian() * 0.05F);
                world.spawnEntityInWorld(var14);

                modem.charge--;
            }

            world.removeBlockTileEntity(x, y, z);
        }

        super.breakBlock(world, x, y, z, blockId, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        for (int i = 0; i < 4; i++) {
            this.top[i] = register.registerIcon("heldsperipherals:twm-top-" + i);
        }
        for (int i = 0; i < 4; i++) {
            this.bottom[i] = register.registerIcon("heldsperipherals:twm-bottom-" + i);
        }
        for (int i = 0; i < 4; i++) {
            this.front[i] = register.registerIcon("heldsperipherals:twm-front-" + i);
        }
        for (int i = 0; i < 4; i++) {
            this.back[i] = register.registerIcon("heldsperipherals:twm-back-" + i);
        }
        for (int i = 0; i < 4; i++) {
            this.left[i] = register.registerIcon("heldsperipherals:twm-left-" + i);
        }
        for (int i = 0; i < 4; i++) {
            this.right[i] = register.registerIcon("heldsperipherals:twm-right-" + i);
        }
    }

}
