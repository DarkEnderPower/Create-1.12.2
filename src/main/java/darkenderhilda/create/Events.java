package darkenderhilda.create;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Events {

    @SubscribeEvent
    public static void onLoadWorld(WorldEvent.Load event) {
        World world = event.getWorld();
        //Create.redstoneLinkNetworkHandler.onLoadWorld(world);
        Create.TORQUE_PROPAGATOR.onLoadWorld(world);
        //if (event.getWorld().isRemote())
        //    DistExecutor.runWhenOn(Dist.CLIENT, () -> CreateClient.bufferCache::invalidate);
    }

    @SubscribeEvent
    public static void onUnloadWorld(WorldEvent.Unload event) {
        World world = event.getWorld();
        //Create.redstoneLinkNetworkHandler.onUnloadWorld(world);
        Create.TORQUE_PROPAGATOR.onUnloadWorld(world);
    }
}
