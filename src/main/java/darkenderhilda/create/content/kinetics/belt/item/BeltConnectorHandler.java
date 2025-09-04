package darkenderhilda.create.content.kinetics.belt.item;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Random;

public class BeltConnectorHandler {

    private static Random r = new Random();

    public static void tick() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = Minecraft.getMinecraft().world;

        if (player == null || world == null)
            return;
        if (Minecraft.getMinecraft().currentScreen != null)
            return;

        for (EnumHand hand : EnumHand.values()) {
            ItemStack heldItem = player.getHeldItem(hand);

            if(heldItem.isEmpty())
                continue;
            if(!new ItemStack(AllBlocks.BELT).equals(heldItem))
                continue;

        }
    }
}
