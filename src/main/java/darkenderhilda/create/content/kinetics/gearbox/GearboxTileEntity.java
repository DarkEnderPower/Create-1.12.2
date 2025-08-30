package darkenderhilda.create.content.kinetics.gearbox;

import darkenderhilda.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import darkenderhilda.create.foundation.utils.WorldUtils;

public class GearboxTileEntity extends DirectionalShaftHalvesBlockEntity {


    public boolean isVertical() {
        return ((GearboxBlock) getWorld().getBlockState(getPos()).getBlock()).isVertical;
    }
}
