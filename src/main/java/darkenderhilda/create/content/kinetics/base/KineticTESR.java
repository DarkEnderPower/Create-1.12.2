package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.content.kinetics.simpleRelays.AbstractShaftBlock;
import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

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
        spinModel(te, x, y, z, partialTicks, axis, AllBlocks.SHAFT.getDefaultState().withProperty(AbstractShaftBlock.AXIS, axis));
    }

    protected void renderShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing facing) {
        renderShaftHalf(te, x, y, z, partialTicks, facing, false);
    }

    protected void renderShaftHalf(KineticTileEntity te, double x, double y, double z, float partialTicks, EnumFacing facing, boolean reverseSpeed) {
        final IBlockState render = AllBlocks.RENDER.getDefaultState();
        IBlockState shaftHalf = null;
        switch (facing) {
            case UP: shaftHalf = render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_U); break;
            case DOWN: shaftHalf = render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_D); break;
            case NORTH: shaftHalf = render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_N); break;
            case EAST: shaftHalf = render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_E); break;
            case SOUTH: shaftHalf = render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_S); break;
            case WEST: shaftHalf = render.withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SHAFT_HALF_W);
        }

        spinModel(te, x, y, z, partialTicks, facing.getAxis(), shaftHalf, reverseSpeed);
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
        renderShaftHalf(te, x, y, z, partialTicks, facing, false);
        renderShaftHalf(te, x, y, z, partialTicks, facing.getOpposite(), true);
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

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        GlStateManager.rotate(-90, 0, 1, 0);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0f, true);

        GlStateManager.popMatrix();
    }

//    protected void glRotate(float angle, EnumFacing.Axis axis) {
//        GlStateManager.translate(0.5F, 0.5F, 0.5F);
//        GlStateManager.rotate(angle,
//                axis == EnumFacing.Axis.X ? 1 : 0,
//                axis == EnumFacing.Axis.Y ? 1 : 0,
//                axis == EnumFacing.Axis.Z ? 1 : 0
//        );
//        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
//    }

    protected float calculateAngle(float speed, KineticTileEntity te, EnumFacing.Axis axis, float pt, float m, boolean addOffset) {
        if (speed == 0) return addOffset ? te.getAxisShift(axis) : 0.0F;
        float time = te.getWorld().getTotalWorldTime() + pt;

        return ((time * 0.3F * speed * m) % 360) + (addOffset ? te.getAxisShift(axis) : 0.0F);
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
