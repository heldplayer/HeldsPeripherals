
package me.heldplayer.mods.HeldsPeripherals.item;

import java.util.List;

import me.heldplayer.mods.HeldsPeripherals.Objects;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockMulti1 extends ItemBlock {

    public ItemBlockMulti1(int par1) {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata & 0xC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {
        return Objects.blockMulti1.getIcon(0, meta);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        int type = (stack.getItemDamage() >> 2 & 0x3);

        String name = "";

        switch (type) {
        case 0:
            name = "tile.HP.fireworksLighter.name";
        break;
        case 1:
            name = "tile.HP.noiseMaker.name";
        break;
        case 2:
            name = "tile.HP.thaumicScanner.name";
        break;
        }

        return ("" + StatCollector.translateToLocal(name)).trim();
        //return StringTranslate.getInstance().translateKey(name);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        if (Objects.creativeTab.equals(CreativeTabs.tabRedstone)) {
            return new CreativeTabs[] { Objects.creativeTab };
        }

        return new CreativeTabs[] { Objects.creativeTab, CreativeTabs.tabRedstone };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab() {
        return Objects.creativeTab;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemid, CreativeTabs tabs, List itemList) {
        itemList.add(new ItemStack(itemid, 1, 0));
        itemList.add(new ItemStack(itemid, 1, 4));
        itemList.add(new ItemStack(itemid, 1, 8));
    }

}
