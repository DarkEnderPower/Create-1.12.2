package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.content.kinetics.base.IRotate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public interface ICogWheel extends IRotate {

    static boolean isSmallCog(IBlockState state) {
        return isSmallCog(state.getBlock());
    }

    static boolean isLargeCog(IBlockState state) {
        return isLargeCog(state.getBlock());
    }

    static boolean isSmallCog(Block block) {
        return block instanceof ICogWheel && ((ICogWheel) block).isSmallCog();
    }

    static boolean isLargeCog(Block block) {
        return block instanceof ICogWheel && ((ICogWheel) block).isLargeCog();
    }

    static boolean isDedicatedCogWheel(Block block) {
        return block instanceof ICogWheel && ((ICogWheel) block).isDedicatedCogWheel();
    }

    static boolean isDedicatedCogItem(ItemStack test) {
        Item item = test.getItem();
        if (!(item instanceof ItemBlock))
            return false;
        return isDedicatedCogWheel(((ItemBlock) item).getBlock());
    }

    static boolean isSmallCogItem(ItemStack test) {
        Item item = test.getItem();
        if (!(item instanceof ItemBlock))
            return false;
        return isSmallCog(((ItemBlock) item).getBlock());
    }

    static boolean isLargeCogItem(ItemStack test) {
        Item item = test.getItem();
        if (!(item instanceof ItemBlock))
            return false;
        return isLargeCog(((ItemBlock) item).getBlock());
    }

    default boolean isLargeCog() {
        return false;
    }

    default boolean isSmallCog() {
        return !isLargeCog();
    }

    default boolean isDedicatedCogWheel() {
        return false;
    }

}
