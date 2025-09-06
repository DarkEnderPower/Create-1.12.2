package darkenderhilda.create.content.kinetics.gearbox;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.foundation.utility.AnimationTickHolder;
import darkenderhilda.create.foundation.utility.Iterate;
import darkenderhilda.create.foundation.utility.WorldUtils;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public class GearboxRenderer extends KineticTileEntityRenderer<GearboxTileEntity> {

    @Override
    public void renderFast(GearboxTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer) {
        IBlockState state = WorldUtils.stateFormTE(te);
        final EnumFacing.Axis boxAxis = state.getValue(AXIS);
        final BlockPos pos = te.getPos();
        float time = AnimationTickHolder.getRenderTick();

        for (EnumFacing direction : Iterate.directions) {
            final EnumFacing.Axis axis = direction.getAxis();
            if (boxAxis == axis)
                continue;

            SuperByteBuffer shaft = AllPartialModels.Model.SHAFT_HALF.renderOnDirectional(state, direction);
            float offset = getRotationOffsetForPosition(te, pos, axis);
            float angle = (time * te.getSpeed() * 3f / 10) % 360;

            if (te.getSpeed() != 0 && te.hasSource()) {
                BlockPos source = te.source.subtract(te.getPos());
                EnumFacing sourceFacing = EnumFacing.getFacingFromVector(source.getX(), source.getY(), source.getZ());
                if (sourceFacing.getAxis() == direction.getAxis())
                    angle *= sourceFacing == direction ? 1 : -1;
                else if (sourceFacing.getAxisDirection() == direction.getAxisDirection())
                    angle *= -1;
            }

            angle += offset;
            angle = angle / 180f * (float) Math.PI;

            kineticRotationTransform(shaft, te, axis, angle, getWorld());
            shaft.translate(x, y, z).renderInto(buffer);
        }
    }
}
