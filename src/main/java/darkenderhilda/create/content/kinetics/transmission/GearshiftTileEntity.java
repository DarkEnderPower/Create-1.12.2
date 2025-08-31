package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.util.EnumFacing;

import static darkenderhilda.create.foundation.block.BlockData.POWERED;

public class GearshiftTileEntity extends SplitShaftTileEntity {

    @Override
    public float getRotationSpeedModifier(EnumFacing face) {
        if (hasSource()) {
            if (face != getSourceFacing() && WorldUtils.stateFormTE(this).getValue(POWERED))
                return -1;
        }

        return 1;
    }
}
