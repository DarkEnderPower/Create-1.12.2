package darkenderhilda.create.test;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.CreateClient;
import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.foundation.utility.AnimationTickHolder;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KineticTileEntityRenderer<T extends KineticTileEntity> extends SafeTileEntityRendererFast<T> {

	public static final SuperByteBufferCache.Compartment<IBlockState> KINETIC_TILE = new SuperByteBufferCache.Compartment<>();
	public static boolean rainbowMode = false;

	public KineticTileEntityRenderer() {
		this.rendererDispatcher = TileEntityRendererDispatcher.instance;
	}

	@Override
	public void renderFast(T te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer) {
		renderRotatingBuffer(te, getWorld(), getRotatedModel(te, WorldUtils.stateFormTE(te)), x, y, z, buffer);
	}

	public static void renderRotatingKineticBlock(KineticTileEntity te, World world, IBlockState renderedState, double x,
			double y, double z, BufferBuilder buffer) {
		SuperByteBuffer superByteBuffer = CreateClient.bufferCache.renderBlockIn(KINETIC_TILE, renderedState);
		renderRotatingBuffer(te, world, superByteBuffer, x, y, z, buffer);
	}

	public static void renderRotatingBuffer(KineticTileEntity te, World world, SuperByteBuffer superBuffer, double x,
			double y, double z, BufferBuilder buffer) {
		buffer.putBulkData(standardKineticRotationTransform(superBuffer, te, world).translate(x, y, z).build());
	}

	public static float getAngleForTe(KineticTileEntity te, final BlockPos pos, EnumFacing.Axis axis) {
		float time = AnimationTickHolder.getRenderTick();
		float offset = getRotationOffsetForPosition(te, pos, axis);
        return ((time * te.getSpeed() * 3f / 10 + offset) % 360) / 180 * (float) Math.PI;
	}

	public static SuperByteBuffer standardKineticRotationTransform(SuperByteBuffer buffer, KineticTileEntity te, World world) {
		final BlockPos pos = te.getPos();
		IBlockState state = WorldUtils.stateFormTE(te);
		EnumFacing.Axis axis = ((IRotate) state.getBlock()).getRotationAxis(state);
		return kineticRotationTransform(buffer, te, axis, getAngleForTe(te, pos, axis), world);
	}

	public static SuperByteBuffer kineticRotationTransform(SuperByteBuffer buffer, KineticTileEntity te, EnumFacing.Axis axis, float angle, World world) {
		int packedLightmapCoords = WorldUtils.stateFormTE(te).getPackedLightmapCoords(world, te.getPos());
		buffer.light(packedLightmapCoords);
		buffer.rotateCentered(axis, angle);

//		int white = 0xFFFFFF;
//		if (KineticDebugger.isActive()) {
//			rainbowMode = true;
//			buffer.color(te.hasNetwork() ? ColorHelper.colorFromLong(te.network) : white);
//		} else {
//			float overStressedEffect = te.effects.overStressedEffect;
//			if (overStressedEffect != 0)
//				if (overStressedEffect > 0)
//					buffer.color(ColorHelper.mixColors(white, 0xFF0000, overStressedEffect));
//				else
//					buffer.color(ColorHelper.mixColors(white, 0x00FFBB, -overStressedEffect));
//			else
//				buffer.color(white);
//		}

		return buffer;
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

	protected IBlockState getRenderedBlockState(KineticTileEntity te) {
		return WorldUtils.stateFormTE(te);
	}

	protected SuperByteBuffer getRotatedModel(T te, IBlockState state) {
		return CreateClient.bufferCache.renderBlockIn(KINETIC_TILE, getRenderedBlockState(te));
	}

}
