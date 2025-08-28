package darkenderhilda.create.content.kinetics.creative_motor;

import darkenderhilda.create.content.kinetics.base.DirectionalKineticBlock;
import darkenderhilda.create.content.kinetics.base.KineticTESR;

public class CreativeMotorTESR extends KineticTESR<CreativeMotorTileEntity> {

    @Override
    protected void renderMe(CreativeMotorTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderShaftHalf(te, x, y, z, partialTicks, te.getWorld().getBlockState(te.getPos()).getValue(DirectionalKineticBlock.FACING));
    }
}
