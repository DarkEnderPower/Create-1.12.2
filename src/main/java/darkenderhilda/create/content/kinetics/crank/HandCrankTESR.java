package darkenderhilda.create.content.kinetics.crank;

import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.util.EnumFacing;

public class HandCrankTESR extends KineticTESR<HandCrankTileEntity> {

    @Override
    protected void renderMe(HandCrankTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing facing = WorldUtils.getTEFacing(te);
        rotateModel(te.getIndependentAngle(partialTicks), x, y, z, facing.getAxis(), WorldUtils.stateFormTE(te));


        renderShaftHalf(te, x, y, z, partialTicks, facing.getOpposite());
    }
}
