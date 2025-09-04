package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utility.Iterate;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class GearShiftTESR extends KineticTESR<GearshiftTileEntity> {

    @Override
    protected void renderMe(GearshiftTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = WorldUtils.stateFormTE(te);
        EnumFacing.Axis boxAxis = ((IRotate) state.getBlock()).getRotationAxis(state);
        for(EnumFacing facing : Iterate.directions) {
            EnumFacing.Axis axis = facing.getAxis();
            if (boxAxis != axis)
                continue;

            renderShaftHalf(te, x, y, z, partialTicks, facing, te.getRotationSpeedModifier(facing) == -1);
        }
    }
}
