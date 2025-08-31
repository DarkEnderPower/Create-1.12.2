package darkenderhilda.create.foundation.block;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlockRegister {

    private final Function<BlockProperties, Block> block;
    private BlockProperties blockProperties;
    private boolean registerItem = false;
    private Function<Block, ItemBlock> itemBlock;

    public BlockRegister(String name, Function<BlockProperties, Block> block) {
        this.block = block;
        blockProperties = new BlockProperties().name(name);
    }

    public BlockRegister initialProperties(BlockProperties properties) {
        properties.name(blockProperties.getName());
        blockProperties = properties;
        return this;
    }

    public BlockRegister properties(Consumer<BlockProperties> properties) {
        properties.accept(blockProperties);
        return this;
    }

    public BlockRegister item() {
        registerItem = true;
        return this;
    }

    public BlockRegister item(Function<Block, ItemBlock> itemBlock) {
        registerItem = true;
        this.itemBlock = itemBlock;
        return this;
    }

    public Block register() {
        Block newBlock = block.apply(blockProperties);
        AllBlocks.BLOCKS.add(newBlock);
        if(registerItem) {
            Item item;
            if(itemBlock != null) {
                item = itemBlock.apply(newBlock);
            } else {
                item = new ItemBlock(newBlock);
            }

            AllItems.ITEMS.add(item.setRegistryName(newBlock.getRegistryName()));
        }

        return newBlock;
    }
}

