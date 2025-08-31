package darkenderhilda.create.content.kinetics.creative.creative_motor;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CreativeMotorItemBlock extends ItemBlock {

    public CreativeMotorItemBlock(Block block) {
        super(block);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
