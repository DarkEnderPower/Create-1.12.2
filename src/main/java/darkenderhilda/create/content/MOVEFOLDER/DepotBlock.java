package darkenderhilda.create.content.MOVEFOLDER;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.CreateBlock;
import darkenderhilda.create.AllShapes;
import darkenderhilda.create.foundation.shapes.ExtendedShape;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DepotBlock extends CreateBlock {

    public DepotBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public ExtendedShape getShape(IBlockState state) {
        return AllShapes.CASING_13PX;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN || super.isSideSolid(base_state, world, pos, side);
    }
}
