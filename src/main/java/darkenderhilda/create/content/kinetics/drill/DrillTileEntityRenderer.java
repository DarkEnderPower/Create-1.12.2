package darkenderhilda.create.content.kinetics.drill;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.test.KineticTileEntityRenderer;
import darkenderhilda.create.test.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;


public class DrillTileEntityRenderer extends KineticTileEntityRenderer<DrillTileEntity> {

    @Override
    protected SuperByteBuffer getRotatedModel(DrillTileEntity te, IBlockState state) {
        return getRotatingModel(state);
    }

    protected static SuperByteBuffer getRotatingModel(IBlockState state) {
        return AllPartialModels.Model.DRILL_HEAD.renderOnDirectional(state);
    }
}
