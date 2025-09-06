package darkenderhilda.create.content.kinetics.speedController;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.content.kinetics.base.HorizontalAxisKineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.HORIZONTAL_AXIS;

public class SpeedControllerBlock extends HorizontalAxisKineticBlock implements ITE<SpeedControllerTileEntity> {

    public SpeedControllerBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.SPEED_CONTROLLER.get();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if(neighbor == pos) {
            withTileEntityDo(world, pos, SpeedControllerTileEntity::updateBracket);
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(HORIZONTAL_AXIS, (placer.isSneaking()) ? placer.getHorizontalFacing() : placer.getHorizontalFacing().rotateY());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new SpeedControllerTileEntity();
    }

    @Override
    public Class<SpeedControllerTileEntity> getTileEntityClass() {
        return SpeedControllerTileEntity.class;
    }
}
