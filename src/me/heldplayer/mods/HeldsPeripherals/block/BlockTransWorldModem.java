
package me.heldplayer.mods.HeldsPeripherals.block;

import java.util.Random;

import me.heldplayer.mods.HeldsPeripherals.ModHeldsPeripherals;
import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.tileentity.TileEntityTransWorldModem;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTransWorldModem extends BlockHeldsPeripheral {
    private Random rnd = new Random();

    private Icon top;
    private Icon bottom;
    private Icon front[];
    private Icon back[];
    private Icon left[];
    private Icon right[];

    public BlockTransWorldModem(int blockId) {
        super(blockId);
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        if (side == 0) {
            return this.bottom;
        }
        if (side == 1) {
            return this.top;
        }

        int offset = 0x0;

        if (((metadata >> 2) & 0x1) == 1) {
            offset |= 0x1;
        }
        if (((metadata >> 3) & 0x1) == 1) {
            offset |= 0x2;
        }

        metadata = metadata & 0x3;

        if (metadata == 0) {
            switch (side) {
            case 2:
                return this.back[offset & 0x1];
            case 3:
                return this.front[offset];
            case 4:
                return this.left[offset & 0x1];
            case 5:
                return this.right[offset & 0x1];
            }
        }
        if (metadata == 1) {
            switch (side) {
            case 2:
                return this.left[offset & 0x1];
            case 3:
                return this.right[offset & 0x1];
            case 4:
                return this.front[offset];
            case 5:
                return this.back[offset & 0x1];
            }
        }
        if (metadata == 2) {
            switch (side) {
            case 2:
                return this.front[offset];
            case 3:
                return this.back[offset & 0x1];
            case 4:
                return this.right[offset & 0x1];
            case 5:
                return this.left[offset & 0x1];
            }
        }
        switch (side) {
        case 2:
            return this.right[offset & 0x1];
        case 3:
            return this.left[offset & 0x1];
        case 4:
            return this.back[offset & 0x1];
        case 5:
            return this.front[offset];
        }

        return this.top;
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
        this.top = register.registerIcon("heldsperipherals:transworldmodem_top");

        this.bottom = register.registerIcon("heldsperipherals:transworldmodem_bottom");

        this.front = new Icon[4];
        this.front[0] = register.registerIcon("heldsperipherals:transworldmodem_front_off_empty");
        this.front[1] = register.registerIcon("heldsperipherals:transworldmodem_front_off_filled");
        this.front[2] = register.registerIcon("heldsperipherals:transworldmodem_front_on_empty");
        this.front[3] = register.registerIcon("heldsperipherals:transworldmodem_front_on_filled");

        this.back = new Icon[2];
        this.back[0] = register.registerIcon("heldsperipherals:transworldmodem_back_off");
        this.back[1] = register.registerIcon("heldsperipherals:transworldmodem_back_on");

        this.left = new Icon[2];
        this.left[0] = register.registerIcon("heldsperipherals:transworldmodem_left_off");
        this.left[1] = register.registerIcon("heldsperipherals:transworldmodem_left_off");

        this.right = new Icon[2];
        this.right[0] = register.registerIcon("heldsperipherals:transworldmodem_right_off");
        this.right[1] = register.registerIcon("heldsperipherals:transworldmodem_right_off");
    }

}
