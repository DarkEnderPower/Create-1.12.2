package darkenderhilda.create.foundation.tileEntity;

import darkenderhilda.create.foundation.block.SafeTileEntityRenderer;

public class SmartTileEntityRenderer <T extends SmartTileEntity> extends SafeTileEntityRenderer<T> {

    @Override
    protected void renderWithGL(T tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        //FilteringRenderer.renderOnTileEntity(tileEntityIn, x, y, z, partialTicks, destroyStage);
        //LinkRenderer.renderOnTileEntity(tileEntityIn, x, y, z, partialTicks, destroyStage);

    }
}
