package darkenderhilda.create.foundation.register;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.BlockRegister;
import darkenderhilda.create.foundation.tileEntity.TileEntityRegister;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Function;

public class CreateRegister {

    public TileEntityRegister tileEntity(Class<? extends TileEntity> te, String registry) {
        return new TileEntityRegister(te, registry);
    }

    public BlockRegister block(String name, Function<BlockProperties, Block> block) {
        return new BlockRegister(name, block);
    }
}
