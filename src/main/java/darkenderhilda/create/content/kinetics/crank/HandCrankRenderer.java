package darkenderhilda.create.content.kinetics.crank;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;

import static darkenderhilda.create.foundation.block.BlockData.FACING;

public class HandCrankRenderer extends KineticTileEntityRenderer<HandCrankTileEntity> {

    @Override
    public void renderFast(HandCrankTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer) {
        if(te.shouldRenderShaft())
            super.renderFast(te, x, y, z, partialTicks, destroyStage, buffer);

        EnumFacing facing = te.getBlockState()
                .getValue(FACING);

        kineticRotationTransform(te.getRenderedHandle(), te, facing.getAxis(), te.getIndependentAngle(partialTicks), te.getWorld())
                .translate(x, y, z).renderInto(buffer);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(HandCrankTileEntity te, IBlockState state) {
        return AllPartialModels.SHAFT_HALF.renderOnDirectional(state, state.getValue(FACING).getOpposite());
    }
}
