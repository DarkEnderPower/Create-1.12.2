package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.foundation.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SimpleKineticBlockEntity extends KineticTileEntity {


    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, IBlockState state, List<BlockPos> neighbours) {
        if (!ICogWheel.isLargeCog(state))
            return super.addPropagationLocations(block, state, neighbours);

        BlockPos.getAllInBox(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
                .forEach(offset -> {
                    if (WorldUtils.distSqr(offset, BlockPos.ORIGIN) == 2)
                        neighbours.add(getPos().subtract(offset));
                });
        return neighbours;
    }

    @Override
    protected boolean isNoisy() {
        return false;
    }
}
