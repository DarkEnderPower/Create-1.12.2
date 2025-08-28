package darkenderhilda.create.content.kinetics.creative_gearbox;

import darkenderhilda.create.content.kinetics.base.KineticTESR;

import net.minecraft.util.EnumFacing;

public class CreativeGearBoxTESR extends KineticTESR<CreativeGearBoxTileEntity> {

    @Override
    protected void renderMe(CreativeGearBoxTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        //IBlockState state = AllBlocks.RENDER.getDefaultState().withProperty(BlockRender.TYPE, BlockRender.Type.SHAFT_HALF_U);
        //IBlockState state = AllBlocks.SHAFT.getDefaultState().withProperty(AbstractShaftBlock.AXIS, EnumFacing.Axis.Y);
        //spinModel(te, x, y, z, partialTicks, EnumFacing.Axis.Y, state);
        //renderShaft(te, x, y, z, partialTicks, EnumFacing.Axis.Y);
        //renderShaftHalf(te, x, y, z, partialTicks, EnumFacing.NORTH, false);
        renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, EnumFacing.Axis.X);
        renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, EnumFacing.Axis.Y);
        renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, EnumFacing.Axis.Z);
    }
}
