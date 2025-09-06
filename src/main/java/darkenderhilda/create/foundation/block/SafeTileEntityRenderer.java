package darkenderhilda.create.foundation.block;

import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

public abstract class SafeTileEntityRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T> {

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (isInvalid(te))
            return;
        renderWithGL(te, x, y, z, partialTicks, destroyStage);
    }

    protected abstract void renderWithGL(T tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage);

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        if (isInvalid(te))
            return;
        renderFast(te, x, y, z, partialTicks, destroyStage, buffer);
    }

    protected void renderFast(T tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer) {
    }

    public boolean isInvalid(T te) {
        return WorldUtils.stateFormTE(te).getBlock() == Blocks.AIR;
    }
}
