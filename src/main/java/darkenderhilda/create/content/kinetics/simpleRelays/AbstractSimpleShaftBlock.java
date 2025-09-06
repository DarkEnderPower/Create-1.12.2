package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractSimpleShaftBlock extends AbstractShaftBlock {

    public AbstractSimpleShaftBlock(BlockProperties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new BracketedKineticTileEntity();
    }
}
