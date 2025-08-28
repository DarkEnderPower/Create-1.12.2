package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.utils.WorldUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DirectionalShaftHalvesBlockEntity extends KineticTileEntity {


    public EnumFacing getSourceFacing() {
        BlockPos localSource = source.subtract(getPos());
        return WorldUtils.getNearest(localSource.getX(), localSource.getY(), localSource.getZ());
    }
}
