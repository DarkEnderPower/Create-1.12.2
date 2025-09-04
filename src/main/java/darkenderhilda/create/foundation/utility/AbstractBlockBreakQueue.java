package darkenderhilda.create.foundation.utility;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractBlockBreakQueue {

    protected Consumer<BlockPos> makeCallbackFor(World world, float effectChance, ItemStack toDamage,
                                                 @Nullable EntityPlayer playerEntity, BiConsumer<BlockPos, ItemStack> drop) {
        return pos -> {
            ItemStack usedTool = toDamage.copy();
            BlockHelper.destroyBlockAs(world, pos, playerEntity, toDamage, effectChance,
                    stack -> drop.accept(pos, stack));
            if (toDamage.isEmpty() && !usedTool.isEmpty())
                ForgeEventFactory.onPlayerDestroyItem(playerEntity, usedTool, EnumHand.MAIN_HAND);
        };
    }

    public void destroyBlocks(World world, @Nullable EntityLivingBase entity, BiConsumer<BlockPos, ItemStack> drop) {
        EntityPlayer player = entity instanceof EntityPlayer ? ((EntityPlayer) entity) : null;

        ItemStack toDamage =
                player != null && !player.isCreative() ? player.getHeldItemMainhand() : ItemStack.EMPTY;
        destroyBlocks(world, toDamage, player, drop);
    }

    public abstract void destroyBlocks(World world, ItemStack toDamage, @Nullable EntityPlayer playerEntity,
                                       BiConsumer<BlockPos, ItemStack> drop);
}
