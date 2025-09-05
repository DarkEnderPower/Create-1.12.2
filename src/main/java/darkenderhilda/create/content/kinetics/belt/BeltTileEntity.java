package darkenderhilda.create.content.kinetics.belt;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.foundation.utility.WorldUtils;

import static darkenderhilda.create.content.kinetics.belt.BeltPart.MIDDLE;

public class BeltTileEntity extends KineticTileEntity {

    public BeltSlope beltSlope = BeltSlope.HORIZONTAL;
    public BeltPart beltPart = BeltPart.START;
    public boolean hasCasing = false;

    public boolean hasPulley() {
        if(WorldUtils.stateFormTE(this).getBlock() == AllBlocks.BELT)
            return false;
        return beltPart != MIDDLE;
    }
}
