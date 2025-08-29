package darkenderhilda.create.content.kinetics.drill;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.DirectionalKineticBlock;
import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class DrillTESR extends KineticTESR<DrillTileEntity> {

    @Override
    protected void renderMe(DrillTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing facing = WorldUtils.getTEFacing(te);
        spinModel(te, x, y, z, partialTicks, facing.getAxis(), headState(facing));
    }

    private IBlockState headState(EnumFacing facing) {
        final IBlockState render = AllBlocks.RENDER.getDefaultState();
        switch (facing) {
            case UP:    return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.DRILL_HEAD_U);
            case DOWN:  return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.DRILL_HEAD_D);
            case NORTH: return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.DRILL_HEAD_N);
            case EAST:  return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.DRILL_HEAD_E);
            case SOUTH: return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.DRILL_HEAD_S);
            case WEST:  return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.DRILL_HEAD_W);
        }

        return null;
    }
}
