package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import net.minecraft.util.EnumFacing;

public abstract class SplitShaftTileEntity extends DirectionalShaftHalvesBlockEntity {

    public abstract float getRotationSpeedModifier(EnumFacing face);
}
