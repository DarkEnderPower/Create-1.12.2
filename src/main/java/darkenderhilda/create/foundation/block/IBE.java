package darkenderhilda.create.foundation.block;

import darkenderhilda.create.foundation.tileEntity.SmartTileEntity;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBE<T extends TileEntity> {

    static void onRemove(IBlockState blockState, World level, BlockPos pos, IBlockState newBlockState) {
        if (!WorldUtils.hasTileEntity(blockState))
            return;
        if (WorldUtils.typeOf(newBlockState.getBlock(), blockState) && WorldUtils.hasTileEntity(newBlockState))
            return;
        TileEntity blockEntity = level.getTileEntity(pos);
        if (blockEntity instanceof SmartTileEntity)
            ((SmartTileEntity) blockEntity).destroy();
        level.removeTileEntity(pos);
    }
}
