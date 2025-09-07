package darkenderhilda.create.content.kinetics.saw;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.CreateClient;
import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.block.SafeTileEntityRenderer;
import darkenderhilda.create.foundation.utility.AngleHelper;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import darkenderhilda.create.foundation.utility.TessellatorHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;
import static darkenderhilda.create.foundation.block.BlockData.FACING;

public class SawRenderer extends SafeTileEntityRenderer<SawTileEntity> {

    @Override
    protected void renderWithGL(SawTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        TessellatorHelper.prepareFastRender();
        TessellatorHelper.begin(DefaultVertexFormats.BLOCK);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        renderBlade(te, x, y, z, bufferBuilder);
        renderShaft(te, x, y, z, bufferBuilder);
        TessellatorHelper.draw();
    }

    private void renderShaft(SawTileEntity te, double x, double y, double z, BufferBuilder bufferBuilder) {
        KineticTileEntityRenderer.renderRotatingBuffer(te, te.getWorld(), getRotatedModel(te), x, y, z, bufferBuilder);
    }

    protected SuperByteBuffer getRotatedModel(KineticTileEntity te) {
        IBlockState state = te.getBlockState();
        EnumFacing facing = state.getValue(FACING);
        if (facing.getAxis().isHorizontal())
            return AllPartialModels.SHAFT_HALF.renderOnDirectional(state, facing.getOpposite());

        return CreateClient.bufferCache.renderBlockIn(KineticTileEntityRenderer.KINETIC_TILE,
                getRenderedBlockState(te));
    }

    protected IBlockState getRenderedBlockState(KineticTileEntity te) {
        IBlockState state = te.getBlockState();
        return AllBlocks.SHAFT.getDefaultState().withProperty(AXIS, ((IRotate) state.getBlock()).getRotationAxis(state));
    }

    protected void renderBlade(SawTileEntity te, double x, double y, double z, BufferBuilder bufferBuilder) {
        IBlockState state = te.getBlockState();
        float speed = te.getSpeed();
        boolean rotate = false;
        AllPartialModels model;


        if (speed > 0) {
            model = AllPartialModels.SAW_BLADE_HORIZONTAL_ACTIVE;
        } else if (speed < 0) {
            model = AllPartialModels.SAW_BLADE_HORIZONTAL_REVERSED;
        } else {
            model = AllPartialModels.SAW_BLADE_HORIZONTAL_INACTIVE;
        }
        if(!SawBlock.isHorizontal(state))
            if (!state.getValue(SawBlock.FLIPPED))
              rotate = true;

        SuperByteBuffer blade = model.renderOnDirectional(state);
        if(rotate)
            blade.rotateCentered(EnumFacing.Axis.Y, AngleHelper.rad(90));

        blade.translate(x, y, z).renderInto(bufferBuilder);
    }
}
