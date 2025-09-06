package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.CreateClient;
import darkenderhilda.create.content.kinetics.simpleRelays.ICogWheel;
import darkenderhilda.create.foundation.tileEntity.SafeTileEntityRendererFast;
import darkenderhilda.create.foundation.utility.AnimationTickHolder;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import darkenderhilda.create.foundation.utility.SuperByteBufferCache;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

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

	protected SuperByteBuffer getRotatedModel(T te, IBlockState state) {
		return CreateClient.bufferCache.renderBlockIn(KINETIC_TILE, getRenderedBlockState(te));
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
		int x = (axis == EnumFacing.Axis.X) ? 0 : pos.getX();
		int y = (axis == EnumFacing.Axis.Y) ? 0 : pos.getY();
		int z = (axis == EnumFacing.Axis.Z) ? 0 : pos.getZ();
		return ((x + y + z) % 2) == 0;
	}

	protected IBlockState getRenderedBlockState(KineticTileEntity te) {
		return WorldUtils.stateFormTE(te);
	}

	public static IBlockState shaft(EnumFacing.Axis axis) {
		return AllBlocks.SHAFT.getDefaultState()
				.withProperty(AXIS, axis);
	}

	public static EnumFacing.Axis getRotationAxisOf(KineticTileEntity te) {
		IBlockState state = WorldUtils.stateFormTE(te);
		return ((IRotate) state.getBlock()).getRotationAxis(state);
	}
}
