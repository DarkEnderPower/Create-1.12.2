package darkenderhilda.create.content.kinetics.creative.creative_gearbox;

import darkenderhilda.create.content.kinetics.base.KineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CreativeGearBoxTileEntity();
    }
}
