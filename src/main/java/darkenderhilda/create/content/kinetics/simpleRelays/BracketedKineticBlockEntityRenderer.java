package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.foundation.utility.AnimationTickHolder;
import darkenderhilda.create.foundation.utility.WorldUtils;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;


public class BracketedKineticBlockEntityRenderer extends KineticTileEntityRenderer<BracketedKineticTileEntity> {

    @Override
    public void renderFast(BracketedKineticTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer) {
        IBlockState state = WorldUtils.stateFormTE(te);
        if (!WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, state)) {
            super.renderFast(te, x, y, z, partialTicks, destroyStage, buffer);
            return;
        }

        EnumFacing.Axis axis = getRotationAxisOf(te);
        EnumFacing facing = WorldUtils.fromAxisAndDirection(axis, EnumFacing.AxisDirection.POSITIVE);
        renderRotatingBuffer(te, getWorld(), AllPartialModels.Model.LARGE_COGWHEEL_SHAFTLESS.renderOnDirectional(state, facing), x, y, z, buffer);

        float angle = getAngleForLargeCogShaft(te, axis);
        SuperByteBuffer shaft = AllPartialModels.Model.COGWHEEL_SHAFT.renderOnDirectional(state, facing);
        kineticRotationTransform(shaft, te, axis, angle, getWorld());
        shaft.translate(x, y, z).renderInto(buffer);
    }

    public static float getAngleForLargeCogShaft(SimpleKineticBlockEntity te, EnumFacing.Axis axis) {
        BlockPos pos = te.getPos();
        float offset = getShaftAngleOffset(axis, pos);
        float time = AnimationTickHolder.getRenderTick();
        return ((time * te.getSpeed() * 3f / 10 + offset) % 360) / 180 * (float) Math.PI;
    }

    public static float getShaftAngleOffset(EnumFacing.Axis axis, BlockPos pos) {
        if (shouldOffset(axis, pos)) {
            return 22.5f;
        } else {
            return 0;
        }
    }
}
