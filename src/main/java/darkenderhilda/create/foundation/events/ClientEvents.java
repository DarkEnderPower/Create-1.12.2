package darkenderhilda.create.foundation.events;

import darkenderhilda.create.content.kinetics.belt.item.BeltConnectorHandler;
import darkenderhilda.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEvents {


    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;

        AnimationTickHolder.tick();
        BeltConnectorHandler.tick();
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null);
    }
}
