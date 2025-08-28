package darkenderhilda.create;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AllCreativeModeTabs {

    public static final CreativeTabs CREATE_TAB = new CreativeTabs("create_tab") {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.GOLDEN_PICKAXE);
        }
    };

    public static final CreativeTabs CREATE_TAB_DECORATIONS = new CreativeTabs("create_tab_deco") {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.BRICK_BLOCK);
        }
    };
}
