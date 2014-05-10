
package me.heldplayer.mods.HeldsPeripherals.block;

import me.heldplayer.mods.HeldsPeripherals.CommonProxy;
import me.heldplayer.mods.HeldsPeripherals.api.IHeldsPeripheral;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class BlockHeldsPeripheral extends Block {

    public BlockHeldsPeripheral() {
        super(Material.rock);

        this.setHardness(2.0F);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int rotation = ((MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3) + 2) % 4;

        world.setBlockMetadataWithNotify(x, y, z, rotation, 3);

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);

            if (tileEntity != null && (tileEntity instanceof IHeldsPeripheral)) {
                ((IHeldsPeripheral) tileEntity).setName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        world.setTileEntity(x, y, z, this.createTileEntity(world, world.getBlockMetadata(x, y, z)));
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public boolean onBlockEventReceived(World world, int blockX, int blockY, int blockZ, int blockId, int eventId) {
        if (world.isRemote) {
            return false;
        }

        super.onBlockEventReceived(world, blockX, blockY, blockZ, blockId, eventId);
        TileEntity tileEntity = world.getTileEntity(blockX, blockY, blockZ);

        if (tileEntity != null) {
            tileEntity.receiveClientEvent(blockId, eventId);

            return true;
        }

        return false;
    }

    @Override
    public int getRenderType() {
        return CommonProxy.renderId;
    }

}
