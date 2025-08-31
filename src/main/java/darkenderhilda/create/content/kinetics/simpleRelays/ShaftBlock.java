package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public class ShaftBlock extends AbstractSimpleShaftBlock {

    public ShaftBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.SIX_VOXEL_POLE.get(state.getValue(AXIS));
    }
}
