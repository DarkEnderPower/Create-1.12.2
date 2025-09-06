package darkenderhilda.create.foundation.tileEntity;

import darkenderhilda.create.foundation.tileEntity.behaviour.base.IBehaviourType;
import darkenderhilda.create.foundation.tileEntity.behaviour.base.TileEntityBehaviour;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class SmartTileEntity extends SyncedTileEntity implements ITickable {

    private final Map<IBehaviourType<?>, TileEntityBehaviour> behaviours;
    private boolean initialized = false;
    private boolean firstNbtRead;
    protected int lazyTickRate;
    protected int lazyTickCounter;

    public SmartTileEntity() {
        behaviours = new HashMap<>();
        setLazyTickRate(10);
        initialized = false;
        firstNbtRead = true;

        ArrayList<TileEntityBehaviour> list = new ArrayList<>();
        addBehaviours(list);
        list.forEach(b -> behaviours.put(b.getType(), b));
    }

    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    public void addBehavioursDeferred(List<TileEntityBehaviour> behaviours) {
    }

    @Override
    public void update() {
        if (!initialized && hasWorld()) {
            initialize();
            initialized = true;
        }

        if (lazyTickCounter-- <= 0) {
            lazyTickCounter = lazyTickRate;
            lazyTick();
        }

        behaviours.values().forEach(TileEntityBehaviour::tick);
    }

    public void destroy() {
        //forEachBehaviour(BlockEntityBehaviour::destroy);
    }


    public void initialize() {
        behaviours.values().forEach(TileEntityBehaviour::initialize);
        lazyTick();
    }

    @Override
    public void invalidate() {
        forEachBehaviour(TileEntityBehaviour::remove);
        super.invalidate();
    }

    public void updateClient(NBTTagCompound compound) {
        behaviours.values().forEach(tb -> tb.updateClient(compound));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        behaviours.values().forEach(tb -> tb.writeNBT(compound));
        return super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound writeToClient(NBTTagCompound tag) {
        behaviours.values().forEach(tb -> tb.writeToClient(tag));
        return super.writeToClient(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (firstNbtRead) {
            firstNbtRead = false;
            ArrayList<TileEntityBehaviour> list = new ArrayList<>();
            addBehavioursDeferred(list);
            list.forEach(b -> behaviours.put(b.getType(), b));
        }

        super.readFromNBT(compound);
        forEachBehaviour(tb -> tb.readNBT(compound));

        if (world != null && world.isRemote)
            updateClient(compound);
    }

    public void setLazyTickRate(int slowTickRate) {
        this.lazyTickRate = slowTickRate;
        this.lazyTickCounter = slowTickRate;
    }

    public void lazyTick() {

    }

    protected void forEachBehaviour(Consumer<TileEntityBehaviour> action) {
        behaviours.values().forEach(tb -> {
            if (!tb.isPaused())
                action.accept(tb);
        });
    }

    protected void putBehaviour(TileEntityBehaviour behaviour) {
        behaviours.put(behaviour.getType(), behaviour);
        behaviour.initialize();
    }

    protected void removeBehaviour(IBehaviourType<?> type) {
        TileEntityBehaviour remove = behaviours.remove(type);
        if (remove != null)
            remove.remove();
    }

    @SuppressWarnings("unchecked")
    public <T extends TileEntityBehaviour> T getBehaviour(IBehaviourType<T> type) {
        if (behaviours.containsKey(type))
            return (T) behaviours.get(type);
        return null;
    }
}
