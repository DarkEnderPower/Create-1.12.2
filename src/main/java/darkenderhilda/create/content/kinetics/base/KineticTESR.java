package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.simpleRelays.ICogWheel;
import darkenderhilda.create.foundation.utility.ClientUtils;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public abstract class KineticTESR<T extends KineticTileEntity> extends TileEntitySpecialRenderer<T> {

    public KineticTESR() {
        this.rendererDispatcher = TileEntityRendererDispatcher.instance;
    }

    @Override
    public final void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        renderMe(te, x, y, z, partialTicks, destroyStage, alpha);
    }

    protected abstract void renderMe(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);

    protected void renderShaft(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis) {
        spinModel(te, x, y, z, partialTicks, axis, AllBlocks.SHAFT.getDefaultState().withProperty(AXIS, axis));
    }

    protected void renderShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing facing) {
        renderShaftHalf(te, x, y, z, partialTicks, facing, false);
    }

    protected void renderShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing facing, boolean reverseSpeed) {
        spinModel(te, x, y, z, partialTicks, facing.getAxis(), halfShaftState(facing), reverseSpeed);
    }

    protected IBlockState halfShaftState(EnumFacing facing) {
        final IBlockState render = AllBlocks.RENDER.getDefaultState();
        switch (facing) {
            case UP:    return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_U);
            case DOWN:  return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_D);
            case NORTH: return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_N);
            case EAST:  return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_E);
            case SOUTH: return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_S);
            case WEST:  return render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_W);
        }

        return null;
    }

    protected void renderBiDirectionalShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis) {
        renderBiDirectionalShaftHalf(te, x, y, z, partialTicks, axis, false);
    }

    protected void renderBiDirectionalShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, boolean reverseSpeed) {
        EnumFacing facing;
        if(axis == EnumFacing.Axis.X) {
            facing = EnumFacing.EAST;
        } else if (axis == EnumFacing.Axis.Y) {
            facing = EnumFacing.UP;
        } else {
            facing = EnumFacing.NORTH;
        }
        renderShaftHalf(te, x, y, z, partialTicks, facing, reverseSpeed);
        renderShaftHalf(te, x, y, z, partialTicks, facing.getOpposite(), !reverseSpeed);
    }

    protected void spinModel(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state) {
        spinModel(te, x, y, z, partialTicks, axis, state, false);
    }

    protected void spinModel(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing.Axis axis, IBlockState state, boolean reverseSpeed) {
        float speed = (reverseSpeed) ? -te.getSpeed() : te.getSpeed();
        rotateModel(calculateAngle(speed, te, axis, partialTicks, 1.0F, true), x, y, z, axis, state);
    }

    protected void rotateModel(float angle, double x, double y, double z, EnumFacing.Axis axis, IBlockState state) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        GlStateManager.rotate(angle,
                axis == EnumFacing.Axis.X ? 1 : 0,
                axis == EnumFacing.Axis.Y ? 1 : 0,
                axis == EnumFacing.Axis.Z ? 1 : 0);

        GlStateManager.translate(-0.5, -0.5, -0.5);

        IBakedModel model = ClientUtils.getModelForState(state);
        GlStateManager.rotate(-90, 0, 1, 0);
        ClientUtils.getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }

    protected float calculateAngle(float speed, KineticTileEntity te, EnumFacing.Axis axis, float pt, float m, boolean addOffset) {
        if (speed == 0) return addOffset ? te.getAxisShift(axis) : 0.0F;
        float time = te.getWorld().getTotalWorldTime() + pt;

        return ((time * 0.3F * speed * m) % 360) + (addOffset ? te.getAxisShift(axis) : 0.0F);
    }

    public static float getRotationOffsetForPosition(KineticTileEntity te, final BlockPos pos, final EnumFacing.Axis axis) {
        return rotationOffset(WorldUtils.stateFormTE(te), axis, pos) + te.getRotationAngleOffset(axis);
    }

    public static float rotationOffset(IBlockState state, EnumFacing.Axis axis, Vec3i pos) {
        if (shouldOffset(axis, pos)) {
            return 22.5f;
        } else {
            return ICogWheel.isLargeCog(state) ? 11.25f : 0;
        }
    }

    public static boolean shouldOffset(EnumFacing.Axis axis, Vec3i pos) {
        // Sum the components of the other 2 axes.
        int x = (axis == EnumFacing.Axis.X) ? 0 : pos.getX();
        int y = (axis == EnumFacing.Axis.Y) ? 0 : pos.getY();
        int z = (axis == EnumFacing.Axis.Z) ? 0 : pos.getZ();
        return ((x + y + z) % 2) == 0;
    }

    public static boolean isAxisShifted(BlockPos pos, EnumFacing.Axis axis) {
        switch (axis) {
            case X: return (pos.getY() & 1) != (pos.getZ() & 1);
            case Y: return (pos.getX() & 1) != (pos.getZ() & 1);
            case Z: return (pos.getX() & 1) != (pos.getY() & 1);
            default:return false;
        }
    }
}
