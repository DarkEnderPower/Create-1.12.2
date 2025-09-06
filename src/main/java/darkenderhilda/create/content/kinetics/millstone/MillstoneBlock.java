package darkenderhilda.create.content.kinetics.millstone;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.content.kinetics.base.KineticBlock;
import darkenderhilda.create.content.kinetics.simpleRelays.ICogWheel;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
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

public class MillstoneBlock extends KineticBlock implements ITE<MillstoneTileEntity>, ICogWheel {

    public MillstoneBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.MILLSTONE.get();
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
        return face == EnumFacing.DOWN;
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return EnumFacing.Axis.Y;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new MillstoneTileEntity();
    }

    @Override
    public Class<MillstoneTileEntity> getTileEntityClass() {
        return MillstoneTileEntity.class;
    }
}
