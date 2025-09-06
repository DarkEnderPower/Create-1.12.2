package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.AnimationTickHolder;
import darkenderhilda.create.foundation.utility.Iterate;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;

public class SplitShaftRenderer extends KineticTileEntityRenderer<SplitShaftTileEntity> {

    @Override
    public void renderFast(SplitShaftTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer) {

        IBlockState state = te.getBlockState();
        final EnumFacing.Axis boxAxis = ((IRotate) state.getBlock()).getRotationAxis(state);
        float time = AnimationTickHolder.getRenderTick();

        for(EnumFacing facing : Iterate.directions) {
            EnumFacing.Axis axis = facing.getAxis();
            if(boxAxis != axis)
                continue;

            float offset = getRotationOffsetForPosition(te, te.getPos(), axis);
            float angle = (time * te.getSpeed() * 3f / 10) % 360;
            float modifier = te.getRotationSpeedModifier(facing);

            angle *= modifier;
            angle += offset;
            angle = angle / 180f * (float) Math.PI;


            SuperByteBuffer superByteBuffer = AllPartialModels.Model.SHAFT_HALF.renderOnDirectional(state, facing);

            kineticRotationTransform(superByteBuffer, te, axis, angle, te.getWorld());
            superByteBuffer.translate(x, y, z).renderInto(buffer);
        }
    }
}
