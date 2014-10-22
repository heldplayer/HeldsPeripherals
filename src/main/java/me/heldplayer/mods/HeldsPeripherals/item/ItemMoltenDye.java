package me.heldplayer.mods.HeldsPeripherals.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import me.heldplayer.mods.HeldsPeripherals.Objects;
import me.heldplayer.mods.HeldsPeripherals.block.BlockMoltenDye;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMoltenDye extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public ItemMoltenDye() {
        super();

        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.icon;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();

        if (meta < 0 || meta > 16) {
            return "item.HP.moltenDye";
        }

        return "item.HP.moltenDye." + BlockMoltenDye.color_names[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return BlockMoltenDye.colors[stack.getItemDamage() % 16];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        list.add("Very hot and sticky, what a mess..");
        super.addInformation(stack, player, list, advanced);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < BlockMoltenDye.colors.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab() {
        return Objects.creativeTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icon = register.registerIcon("heldsperipherals:molten_dye_still");
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        if (Objects.creativeTab.equals(CreativeTabs.tabRedstone)) {
            return new CreativeTabs[] { Objects.creativeTab };
        }

        return new CreativeTabs[] { Objects.creativeTab, CreativeTabs.tabRedstone };
    }

}
