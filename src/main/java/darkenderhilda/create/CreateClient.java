package darkenderhilda.create;

import darkenderhilda.create.content.kinetics.base.KineticTileEntityRenderer;
import darkenderhilda.create.foundation.utility.SuperByteBufferCache;

public class CreateClient {

    public static SuperByteBufferCache bufferCache;

    static {
        bufferCache = new SuperByteBufferCache();
        bufferCache.registerCompartment(KineticTileEntityRenderer.KINETIC_TILE);
        //bufferCache.registerCompartment(ContraptionRenderer.CONTRAPTION, 20);
    }
}
