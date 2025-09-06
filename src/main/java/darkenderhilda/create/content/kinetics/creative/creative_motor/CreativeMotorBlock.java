package darkenderhilda.create.content.kinetics.creative.creative_motor;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.content.kinetics.base.DirectionalKineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.FACING;

public class CreativeMotorBlock extends DirectionalKineticBlock implements ITE<CreativeMotorTileEntity> {

    public CreativeMotorBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.MOTOR_BLOCK.get(state.getValue(FACING));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return face == state.getValue(FACING);
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CreativeMotorTileEntity();
    }

    @Override
    public Class<CreativeMotorTileEntity> getTileEntityClass() {
        return CreativeMotorTileEntity.class;
    }
}
