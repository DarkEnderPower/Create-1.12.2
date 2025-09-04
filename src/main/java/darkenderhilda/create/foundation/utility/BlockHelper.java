package darkenderhilda.create.foundation.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlockHelper {

    public static void destroyBlock(World world, BlockPos pos, float effectChance, Consumer<ItemStack> droppedItemCallback) {
        destroyBlockAs(world, pos, null, ItemStack.EMPTY, effectChance, droppedItemCallback);
    }

    public static void destroyBlockAs(World world, BlockPos pos, @Nullable EntityPlayer player, ItemStack usedTool, float effectChance, Consumer<ItemStack> droppedItemCallback) {
        IBlockState state = world.getBlockState(pos);
        if (world.rand.nextFloat() < effectChance) world.playEvent(2001, pos, Block.getStateId(state));
        TileEntity tileentity = WorldUtils.hasTileEntity(state) ? world.getTileEntity(pos) : null;

        if (!world.restoringBlockSnapshots) {
            for (ItemStack itemStack : world.getBlockState(pos).getBlock().getDrops(world, pos, state, 0))
                droppedItemCallback.accept(itemStack);
        }

        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
}
