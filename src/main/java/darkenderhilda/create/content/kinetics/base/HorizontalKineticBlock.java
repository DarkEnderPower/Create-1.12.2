package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static darkenderhilda.create.foundation.block.BlockData.HORIZONTAL_FACING;


public abstract class HorizontalKineticBlock extends KineticBlock {

    public HorizontalKineticBlock(BlockProperties properties) {
        super(properties);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState()
                .withProperty(HORIZONTAL_FACING, placer.getHorizontalFacing().getOpposite());
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HORIZONTAL_FACING);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HORIZONTAL_FACING, EnumFacing.byHorizontalIndex(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(HORIZONTAL_FACING).getHorizontalIndex();
    }
}
