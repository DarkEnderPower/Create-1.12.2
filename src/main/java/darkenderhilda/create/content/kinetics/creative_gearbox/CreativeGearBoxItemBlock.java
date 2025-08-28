package darkenderhilda.create.content.kinetics.creative_gearbox;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CreativeGearBoxItemBlock extends ItemBlock {

    public CreativeGearBoxItemBlock(Block block) {
        super(block);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
