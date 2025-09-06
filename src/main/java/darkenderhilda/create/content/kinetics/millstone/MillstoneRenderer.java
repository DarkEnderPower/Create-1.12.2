package darkenderhilda.create.content.kinetics.millstone;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;

public class MillstoneRenderer extends KineticTileEntityRenderer<MillstoneTileEntity> {

    @Override
    protected SuperByteBuffer getRotatedModel(MillstoneTileEntity te, IBlockState state) {
        return AllPartialModels.Model.MILLSTONE_COG.renderOn(state);
    }
}
