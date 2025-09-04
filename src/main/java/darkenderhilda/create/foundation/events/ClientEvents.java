package darkenderhilda.create.foundation.events;

import darkenderhilda.create.content.kinetics.belt.item.BeltConnectorHandler;
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

        BeltConnectorHandler.tick();
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null);
    }
}
