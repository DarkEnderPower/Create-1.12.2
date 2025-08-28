package darkenderhilda.create.foundation.data;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.BlockRegister;
import darkenderhilda.create.foundation.tileEntity.TileEntityRegister;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Function;

public class CreateRegistrate {

    public TileEntityRegister tileEntity(Class<? extends TileEntity> te, String registry) {
        return new TileEntityRegister(te, registry);
    }

    public BlockRegister block(Function<BlockProperties, Block> block) {
        return new BlockRegister(block);
    }
}
