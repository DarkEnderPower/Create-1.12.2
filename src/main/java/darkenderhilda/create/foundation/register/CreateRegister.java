package darkenderhilda.create.foundation.register;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.BlockRegister;
import net.minecraft.block.Block;

import java.util.function.Function;

public class CreateRegister {

    public BlockRegister block(String name, Function<BlockProperties, Block> block) {
        return new BlockRegister(name, block);
    }
}
