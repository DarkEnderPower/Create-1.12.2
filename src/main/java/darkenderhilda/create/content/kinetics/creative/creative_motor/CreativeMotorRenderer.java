package darkenderhilda.create.content.kinetics.creative.creative_motor;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;

public class CreativeMotorRenderer extends KineticTileEntityRenderer<CreativeMotorTileEntity> {

    @Override
    protected SuperByteBuffer getRotatedModel(CreativeMotorTileEntity te, IBlockState state) {
        return AllPartialModels.Model.SHAFT_HALF.renderOnDirectional(state);
    }
}
