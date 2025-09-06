package darkenderhilda.create.foundation.utility;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import darkenderhilda.create.AllPartialModels;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SuperByteBufferCache {

	public static class Compartment<T> {
	}

	public static final Compartment<IBlockState> GENERIC_TILE = new Compartment<>();
	public static final Compartment<AllPartialModels> PARTIAL = new Compartment<>();

	Map<Compartment<?>, Cache<Object, SuperByteBuffer>> cache;

	public SuperByteBufferCache() {
		cache = new HashMap<>();
		registerCompartment(GENERIC_TILE);
		registerCompartment(PARTIAL);
	}

	public SuperByteBuffer renderBlock(IBlockState toRender) {
		return getGeneric(toRender, () -> standardBlockRender(toRender));
	}

	public SuperByteBuffer renderPartial(AllPartialModels partial, IBlockState referenceState) {
		return get(PARTIAL, partial, () -> standardModelRender(partial.get(), referenceState));
	}

	public SuperByteBuffer renderBlockIn(Compartment<IBlockState> compartment, IBlockState toRender) {
		return get(compartment, toRender, () -> standardBlockRender(toRender));
	}

	SuperByteBuffer getGeneric(IBlockState key, Supplier<SuperByteBuffer> supplier) {
		return get(GENERIC_TILE, key, supplier);
	}

	public <T> SuperByteBuffer get(Compartment<T> compartment, T key, Supplier<SuperByteBuffer> supplier) {
		Cache<Object, SuperByteBuffer> compartmentCache = this.cache.get(compartment);
		try {
			return compartmentCache.get(key, supplier::get);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void registerCompartment(Compartment<?> instance) {
		cache.put(instance, CacheBuilder.newBuilder().build());
	}

	public void registerCompartment(Compartment<?> instance, long ticksTillExpired) {
		cache.put(instance,
				CacheBuilder.newBuilder().expireAfterAccess(ticksTillExpired * 50, TimeUnit.MILLISECONDS).build());
	}

	private SuperByteBuffer standardBlockRender(IBlockState renderedState) {
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		return standardModelRender(dispatcher.getModelForState(renderedState), renderedState);
	}

	private SuperByteBuffer standardModelRender(IBakedModel model, IBlockState referenceState) {
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockModelRenderer blockRenderer = dispatcher.getBlockModelRenderer();
		BufferBuilder builder = new BufferBuilder(0);
		Random random = new Random();

		builder.setTranslation(0, 1, 0);
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		blockRenderer.renderModelFlat(Minecraft.getMinecraft().world, model, referenceState, BlockPos.ORIGIN.down(),
				builder, true, random.nextLong());
		builder.finishDrawing();

		return new SuperByteBuffer(builder.getByteBuffer());
	}

	public void invalidate() {
		cache.forEach((comp, cache) -> {
			cache.invalidateAll();
		});
	}

}
