package darkenderhilda.create.content.kinetics;

import darkenderhilda.create.Create;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class TorquePropagator {

    static Map<World, Map<Long, KineticNetwork>> networks = new HashMap<>();

    public void onLoadWorld(World world) {
        networks.put(world, new HashMap<>());
        Create.logger.debug("Prepared Kinetic Network Space for " + world.provider.getDimensionType().getName());
    }

    public void onUnloadWorld(World world) {
        networks.remove(world);
        Create.logger.debug("Removed Kinetic Network Space for " + world.provider.getDimensionType().getName());
    }

    public KineticNetwork getOrCreateNetworkFor(KineticTileEntity be) {
        Long id = be.network;
        KineticNetwork network;
        Map<Long, KineticNetwork> map = networks.computeIfAbsent(be.getWorld(), $ -> new HashMap<>());
        if (id == null)
            return null;

        if (!map.containsKey(id)) {
            network = new KineticNetwork();
            network.id = be.network;
            map.put(id, network);
        }
        network = map.get(id);
        return network;
    }
}
