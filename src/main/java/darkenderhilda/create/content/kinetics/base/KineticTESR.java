package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.foundation.utility.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public abstract class KineticTESR<T extends KineticTileEntity> extends TileEntitySpecialRenderer<T> {

    public KineticTESR() {
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public final void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        if (destroyStage >= 0) {
            bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(30.0F, 30.0F, 30.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        renderMe(te, x, y, z, partialTicks, destroyStage, alpha);

        GlStateManager.popMatrix();

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    protected abstract void renderMe(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);

    protected void renderShaft(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis) {
        spinModel(te, x, y, z, partialTicks, axis, AllBlocks.SHAFT.getDefaultState().withProperty(AXIS, axis));
    }

    protected void renderShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing facing) {
        renderShaftHalf(te, x, y, z, partialTicks, facing, false);
    }

    protected void renderShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing facing, boolean reverseSpeed) {
        spinModel(te, x, y, z, partialTicks, facing.getAxis(), AllPartialModels.shaftHalf(facing), reverseSpeed);
    }

    protected void renderBiDirectionalShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis) {
        EnumFacing facing;
        if(axis == EnumFacing.Axis.X) {
            facing = EnumFacing.EAST;
        } else if (axis == EnumFacing.Axis.Y) {
            facing = EnumFacing.UP;
        } else {
            facing = EnumFacing.NORTH;
        }

        renderShaftHalf(te, x, y, z, partialTicks, facing, true);
        renderShaftHalf(te, x, y, z, partialTicks, facing.getOpposite(), false);
    }

    protected void spinModel(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state) {
        spinModel(te, x, y, z, partialTicks, axis, state, false);
    }

    protected void spinModel(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state, boolean reverseSpeed) {
        float speed = (reverseSpeed) ? -te.getSpeed() : te.getSpeed();
        rotateModel(calculateAngle(speed, te, axis, partialTicks), x, y, z, axis, state);
    }

    protected void rotateModel(float angle, double x, double y, double z, EnumFacing.Axis axis, IBlockState state) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        GlStateManager.rotate(angle,
                axis == EnumFacing.Axis.X ? 1 : 0,
                axis == EnumFacing.Axis.Y ? 1 : 0,
                axis == EnumFacing.Axis.Z ? 1 : 0);

        GlStateManager.translate(-0.5, -0.5, -0.5);

        GlStateManager.rotate(-90, 0, 1, 0);
        IBakedModel model = ClientUtils.getModelForState(state);
        ClientUtils.getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }

    protected float calculateAngle(float speed, KineticTileEntity te, EnumFacing.Axis axis, float pt) {
        float offset = getRotationOffsetForPosition(te, te.getPos(), axis);
        float time = te.getWorld().getTotalWorldTime() + pt;

        return ((time * 0.3F * speed) % 360) + offset;
    }

    protected static float getRotationOffsetForPosition(KineticTileEntity te, final BlockPos pos, final EnumFacing.Axis axis) {
        float offset = WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, WorldUtils.stateFormTE(te)) ? 11.25f : 0;
        double d = (((axis == EnumFacing.Axis.X) ? 0 : pos.getX()) + ((axis == EnumFacing.Axis.Y) ? 0 : pos.getY())
                + ((axis == EnumFacing.Axis.Z) ? 0 : pos.getZ())) % 2;
        if (d == 0) {
            offset = 22.5f;
        }
        return offset;
    }

    protected interface ModelRender {

        void render();
    }
}
