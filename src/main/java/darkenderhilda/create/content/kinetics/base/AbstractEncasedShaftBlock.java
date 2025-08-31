package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public abstract class AbstractEncasedShaftBlock extends RotatedPillarKineticBlock {

    public AbstractEncasedShaftBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return state.getValue(AXIS);
    }
}
