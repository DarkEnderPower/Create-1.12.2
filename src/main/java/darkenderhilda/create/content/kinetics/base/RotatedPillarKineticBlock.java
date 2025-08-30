package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public abstract class RotatedPillarKineticBlock extends KineticBlock {

    public RotatedPillarKineticBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing.Axis axis = EnumFacing.Axis.X;
        if(meta == 1) {
            axis = EnumFacing.Axis.Y;
        }
        if(meta == 2) {
            axis = EnumFacing.Axis.Z;
        }
        return getDefaultState().withProperty(AXIS, axis);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if(state.getValue(AXIS) == EnumFacing.Axis.Y) {
            meta = 1;
        }
        if(state.getValue(AXIS) == EnumFacing.Axis.Z) {
            meta = 2;
        }
        return meta;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(AXIS, facing.getAxis());
    }
}
