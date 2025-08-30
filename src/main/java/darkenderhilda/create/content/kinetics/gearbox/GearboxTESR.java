package darkenderhilda.create.content.kinetics.gearbox;

import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utils.Iterate;
import darkenderhilda.create.foundation.utils.WorldUtils;
import net.minecraft.util.EnumFacing;

public class GearboxTESR extends KineticTESR<GearboxTileEntity> {

    @Override
    protected void renderMe(GearboxTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(!te.isVertical()) {
            for(EnumFacing.Axis axis : Iterate.axes) {
                if(axis == WorldUtils.getTEAxis(te)) {
                    continue;
                }

                renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, axis, true);
            }
        } else {
            if(WorldUtils.getTEAxis(te) == EnumFacing.Axis.Z) {
                renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, EnumFacing.Axis.X, true);
                renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, EnumFacing.Axis.Y, false);
            } else if(WorldUtils.getTEAxis(te) == EnumFacing.Axis.X) {
                renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, EnumFacing.Axis.Z, true);
                renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, EnumFacing.Axis.Y, true);
            }
        }
    }
}
