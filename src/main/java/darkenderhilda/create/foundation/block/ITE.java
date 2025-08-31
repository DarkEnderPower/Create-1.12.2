package darkenderhilda.create.foundation.block;

import darkenderhilda.create.foundation.tileEntity.SmartTileEntity;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ITE<T extends TileEntity> {

    Class<T> getTileEntityClass();

    default void withBlockEntityDo(IBlockAccess world, BlockPos pos, Consumer<T> action) {
        getBlockEntityOptional(world, pos).ifPresent(action);
    }

    default EnumActionResult onBlockEntityUse(IBlockAccess world, BlockPos pos, Function<T, EnumActionResult> action) {
        return getBlockEntityOptional(world, pos).map(action)
                .orElse(EnumActionResult.PASS);
    }

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

    default Optional<T> getBlockEntityOptional(IBlockAccess world, BlockPos pos) {
        return Optional.ofNullable(getBlockEntity(world, pos));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    default T getBlockEntity(IBlockAccess worldIn, BlockPos pos) {
        TileEntity blockEntity = worldIn.getTileEntity(pos);
        Class<T> expectedClass = getTileEntityClass();

        if (blockEntity == null)
            return null;
        if (!expectedClass.isInstance(blockEntity))
            return null;

        return (T) blockEntity;
    }
}
