package darkenderhilda.create.content.kinetics.speedController;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.CreateClient;
import darkenderhilda.create.foundation.tileEntity.SmartTileEntityRenderer;
import darkenderhilda.create.foundation.utility.TessellatorHelper;
import darkenderhilda.create.foundation.utility.WorldUtils;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;
import static darkenderhilda.create.foundation.block.BlockData.HORIZONTAL_AXIS;

public class SpeedControllerRenderer extends SmartTileEntityRenderer<SpeedControllerTileEntity> {

    @Override
    protected void renderWithGL(SpeedControllerTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderWithGL(te, x, y, z, partialTicks, destroyStage);

        IBlockState state = WorldUtils.stateFormTE(te);
        TessellatorHelper.prepareFastRender();
        TessellatorHelper.begin(DefaultVertexFormats.BLOCK);

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        KineticTileEntityRenderer.renderRotatingBuffer(te, te.getWorld(), getRotatedModel(te, state), x, y, z, bufferBuilder);

        if(!te.hasBracket) {
            TessellatorHelper.draw();
            return;
        }

        EnumFacing facing = (state.getValue(HORIZONTAL_AXIS).getAxis() == EnumFacing.Axis.X)
                ? EnumFacing.NORTH : EnumFacing.WEST;
        bufferBuilder.putBulkData(AllPartialModels.SPEED_CONTROLLER_BRACKET.renderOnDirectional(state, facing).translate(x, y + 1, z).build());

        TessellatorHelper.draw();
    }

    protected SuperByteBuffer getRotatedModel(SpeedControllerTileEntity te, IBlockState state) {
        return CreateClient.bufferCache.renderBlockIn(KineticTileEntityRenderer.KINETIC_TILE,
                AllBlocks.SHAFT.getDefaultState().withProperty(AXIS, state.getValue(HORIZONTAL_AXIS).getAxis()));
    }

}
