package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.content.kinetics.base.KineticTESR;
import net.minecraft.block.state.IBlockState;

public class BracketedKineticTESR extends KineticTESR<BracketedKineticTileEntity> {

    @Override
    protected void renderMe(BracketedKineticTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = te.getWorld().getBlockState(te.getPos());
        spinModel(te, x, y, z, partialTicks, state.getValue(AbstractShaftBlock.AXIS), state);
    }

}
