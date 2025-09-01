package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utility.Iterate;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;


public class ClutchTESR extends KineticTESR<ClutchTileEntity> {
    @Override
    protected void renderMe(ClutchTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = WorldUtils.stateFormTE(te);
        EnumFacing.Axis boxAxis = ((IRotate) state.getBlock()).getRotationAxis(state);
        for(EnumFacing facing : Iterate.direction) {
            EnumFacing.Axis axis = facing.getAxis();
            if (boxAxis != axis)
                continue;

            if(te.getRotationSpeedModifier(facing) == 1)
                renderShaftHalf(te, x, y, z, partialTicks, facing);
            else {
                rotateModel(calculateAngle(0, te, axis, partialTicks, 1.0F, true), x, y, z, axis, halfShaftState(facing));
            }
        }
    }
}
