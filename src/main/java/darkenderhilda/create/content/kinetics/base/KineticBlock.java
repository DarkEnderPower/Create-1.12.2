package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.BlockWithTE;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class KineticBlock extends BlockWithTE implements IRotate {

    public KineticBlock(BlockProperties properties) {
        super(properties);
    }


    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity blockEntity = worldIn.getTileEntity(pos);

        KineticTileEntity kineticBlockEntity;
        if (blockEntity instanceof KineticTileEntity) {
            kineticBlockEntity = (KineticTileEntity) blockEntity;
            kineticBlockEntity.preventSpeedUpdate = 0;

//            if (oldState.getBlock() != state.getBlock())
//                return;
//            if (state.hasBlockEntity() != oldState.hasBlockEntity())
//                return;
//            if (!areStatesKineticallyEquivalent(oldState, state))
//                return;

            kineticBlockEntity.preventSpeedUpdate = 2;
        }
    }

//    @Override
//    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//        IBE.onRemove();
//    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return false;
    }

    protected boolean areStatesKineticallyEquivalent(IBlockState oldState, IBlockState newState) {
        if (oldState.getBlock() != newState.getBlock())
            return false;
        return getRotationAxis(newState) == getRotationAxis(oldState);
    }


    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
