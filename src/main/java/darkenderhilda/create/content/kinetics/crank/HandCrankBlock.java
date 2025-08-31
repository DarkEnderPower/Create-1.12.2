package darkenderhilda.create.content.kinetics.crank;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.content.kinetics.base.DirectionalKineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.FACING;


public class HandCrankBlock extends DirectionalKineticBlock implements ITE<HandCrankTileEntity> {

    public HandCrankBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isRemote)
            return;

        EnumFacing facing = state.getValue(FACING);
        if(worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public int getRotationSpeed() {
        return 32;
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.CRANK.get(state.getValue(FACING));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<HandCrankTileEntity> getTileEntityClass() {
        return HandCrankTileEntity.class;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }
}
