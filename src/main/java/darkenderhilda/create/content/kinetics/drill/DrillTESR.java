package darkenderhilda.create.content.kinetics.drill;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class DrillTESR extends KineticTESR<DrillTileEntity> {

    @Override
    protected void renderMe(DrillTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing facing = WorldUtils.getTEFacing(te);
        spinModel(te, x, y, z, partialTicks, facing.getAxis(), AllPartialModels.drillHead(facing));
    }
}
