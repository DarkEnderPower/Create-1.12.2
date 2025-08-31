package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static darkenderhilda.create.foundation.block.BlockData.POWERED;

public class ClutchBlock extends GearshiftBlock {

    public ClutchBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isRemote)
            return;

        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != worldIn.isBlockPowered(pos)) {
            worldIn.setBlockState(pos, state.cycleProperty(POWERED), 2 | 16);
            detachKinetics(worldIn, pos, previouslyPowered);
        }
    }
}
