package darkenderhilda.create;

import darkenderhilda.create.test.KineticTileEntityRenderer;
import darkenderhilda.create.test.SuperByteBufferCache;

public class CreateClient {

    public static SuperByteBufferCache bufferCache;

    static {
        bufferCache = new SuperByteBufferCache();
        bufferCache.registerCompartment(KineticTileEntityRenderer.KINETIC_TILE);
        //bufferCache.registerCompartment(ContraptionRenderer.CONTRAPTION, 20);
    }
}
