package darkenderhilda.create.test;


import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;

public class TestBlockTESR extends KineticTileEntityRenderer {

    @Override
    protected SuperByteBuffer getRotatedModel(KineticTileEntity te, IBlockState state) {
        return AllPartialModels.Model.DRILL_HEAD.renderOn(WorldUtils.stateFormTE(te));
    }
}
