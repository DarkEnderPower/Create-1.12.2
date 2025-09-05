package darkenderhilda.create.test;

import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.animation.FastTESR;

public abstract class SafeTileEntityRendererFast<T extends TileEntity> extends FastTESR<T> {

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        if (isInvalid(te))
            return;
        renderFast(te, x, y, z, partialTicks, destroyStage, buffer);
    }

    protected abstract void renderFast(T te, double x, double y, double z, float partialTicks, int destroyStage,
                                       BufferBuilder buffer);

    public boolean isInvalid(T te) {
        return WorldUtils.stateFormTE(te).getBlock() == Blocks.AIR;
    }
}
