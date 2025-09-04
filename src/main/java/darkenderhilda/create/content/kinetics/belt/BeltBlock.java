package darkenderhilda.create.content.kinetics.belt;

import darkenderhilda.create.content.kinetics.base.HorizontalKineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static darkenderhilda.create.foundation.block.BlockData.HORIZONTAL_FACING;

public class BeltBlock extends HorizontalKineticBlock
        implements ITE<BeltTileEntity> {

    BeltTileEntity beltTileEntity;

    public BeltBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        if (face.getAxis() != getRotationAxis(state))
            return false;
        return getBlockEntityOptional(world, pos).map(BeltTileEntity::hasPulley)
                .orElse(false);
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        if (beltTileEntity.beltSlope == BeltSlope.SIDEWAYS)
            return EnumFacing.Axis.Y;
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        beltTileEntity = new BeltTileEntity();
        return beltTileEntity;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return beltTileEntity.hasCasing ? EnumBlockRenderType.MODEL : EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public Class<BeltTileEntity> getTileEntityClass() {
        return BeltTileEntity.class;
    }
}
