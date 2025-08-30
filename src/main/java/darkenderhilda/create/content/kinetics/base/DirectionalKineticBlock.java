package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.utils.Iterate;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static darkenderhilda.create.foundation.block.BlockData.FACING;

public abstract class DirectionalKineticBlock extends KineticBlock {

    public DirectionalKineticBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing facingToPlace = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        return getDefaultState().withProperty(FACING, placer.isSneaking() ? facingToPlace : facingToPlace.getOpposite());
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }
}
