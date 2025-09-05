package darkenderhilda.create.content.kinetics.millstone;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.KineticTESR;
import net.minecraft.util.EnumFacing;

public class MillstoneTESR extends KineticTESR<MillstoneTileEntity> {

    @Override
    protected void renderMe(MillstoneTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        spinModel(te, x, y, z, partialTicks, EnumFacing.Axis.Y, AllPartialModels.Model.MILLSTONE_COG.getState());
    }
}
