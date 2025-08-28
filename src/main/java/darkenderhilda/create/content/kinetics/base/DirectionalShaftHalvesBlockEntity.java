package darkenderhilda.create.content.kinetics.base;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DirectionalShaftHalvesBlockEntity extends KineticTileEntity {


    public EnumFacing getSourceFacing() {
        BlockPos localSource = source.subtract(getPos());
        return EnumFacing.getFacingFromVector(localSource.getX(), localSource.getY(), localSource.getZ());
    }
}
