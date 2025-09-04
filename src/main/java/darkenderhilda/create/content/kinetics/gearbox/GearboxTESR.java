package darkenderhilda.create.content.kinetics.gearbox;

import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utility.Iterate;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class GearboxTESR extends KineticTESR<GearboxTileEntity> {

    @Override
    protected void renderMe(GearboxTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = WorldUtils.stateFormTE(te);
        EnumFacing.Axis boxAxis = ((IRotate) state.getBlock()).getRotationAxis(state);
        for (EnumFacing facing : Iterate.directions) {
            final EnumFacing.Axis axis = facing.getAxis();
            if (boxAxis == axis)
                continue;

            boolean b = true;
            if (te.getSpeed() != 0 && te.hasSource()) {
                BlockPos source = te.source.subtract(te.getPos());
                EnumFacing sourceFacing = WorldUtils.getNearest(source.getX(), source.getY(), source.getZ());
                if (sourceFacing.getAxis() == facing.getAxis()) {
                    b = sourceFacing == facing;
                }
                else if (sourceFacing.getAxisDirection() == facing.getAxisDirection()) {
                    b = false;
                }
            }

            renderShaftHalf(te, x, y, z, partialTicks, facing, !b);
        }
    }
}
