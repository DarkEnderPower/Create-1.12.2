package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static darkenderhilda.create.foundation.block.BlockData.HORIZONTAL_AXIS;

public abstract class HorizontalAxisKineticBlock extends KineticBlock {

    public HorizontalAxisKineticBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return state.getValue(HORIZONTAL_AXIS).getAxis();
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return face.getAxis() == state.getValue(HORIZONTAL_AXIS).getAxis();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HORIZONTAL_AXIS);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(HORIZONTAL_AXIS, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HORIZONTAL_AXIS, EnumFacing.byHorizontalIndex(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(HORIZONTAL_AXIS).getHorizontalIndex();
    }
}
