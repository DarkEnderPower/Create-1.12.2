package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utility.WorldUtils;

public class SplitShaftTESR extends KineticTESR<SplitShaftTileEntity> {

    @Override
    protected void renderMe(SplitShaftTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderShaft(te, x, y, z, partialTicks, WorldUtils.getTEAxis(te));
    }
}
