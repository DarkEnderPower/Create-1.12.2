package darkenderhilda.create.foundation.utility;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlockHelper {

    public static void destroyBlock(World world, BlockPos pos, float effectChance, Consumer<ItemStack> droppedItemCallback) {
        destroyBlockAs(world, pos, null, ItemStack.EMPTY, effectChance, droppedItemCallback);
    }

    public static void destroyBlockAs(World world, BlockPos pos, @Nullable EntityPlayer player, ItemStack usedTool, float effectChance, Consumer<ItemStack> droppedItemCallback) {

    }
}
