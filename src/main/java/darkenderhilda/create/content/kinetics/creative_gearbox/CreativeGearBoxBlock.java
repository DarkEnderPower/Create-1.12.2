package darkenderhilda.create.content.kinetics.creative_gearbox;

import darkenderhilda.create.content.kinetics.base.KineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeGearBoxBlock extends KineticBlock {

    public CreativeGearBoxBlock(BlockProperties properties) {
        super(properties);
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
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return null;
    }
}
