package darkenderhilda.create.content.kinetics.simpleRelays;

import net.minecraft.util.math.AxisAlignedBB;

public class BracketedKineticTileEntity extends SimpleKineticBlockEntity {

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos).grow(1);
    }
}
