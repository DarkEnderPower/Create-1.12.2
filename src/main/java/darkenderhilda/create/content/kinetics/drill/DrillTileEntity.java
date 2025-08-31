package darkenderhilda.create.content.kinetics.drill;

import darkenderhilda.create.content.kinetics.base.BlockBreakingKineticTileEntity;
import darkenderhilda.create.foundation.block.BlockData;
import net.minecraft.util.math.BlockPos;

public class DrillTileEntity extends BlockBreakingKineticTileEntity {


    @Override
    protected BlockPos getBreakingPos() {
        return getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockData.FACING));
    }
}
