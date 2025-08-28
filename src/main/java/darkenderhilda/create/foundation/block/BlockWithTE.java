package darkenderhilda.create.foundation.block;

import darkenderhilda.create.AllTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockWithTE extends CreateBlock implements ITileEntityProvider {

    public BlockWithTE(BlockProperties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        try {
            return AllTileEntity.getTEForBlock(this);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
