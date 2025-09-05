package darkenderhilda.create.test;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.BlockWithTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;

public class TestBlock extends BlockWithTE {

    public TestBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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
}
