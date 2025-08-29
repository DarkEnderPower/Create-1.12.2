package darkenderhilda.create.foundation.tileEntity;

import net.minecraft.util.ITickable;

public abstract class SmartTileEntity extends SyncedTileEntity implements ITickable {

    private boolean initialized = false;
    protected int lazyTickRate;
    protected int lazyTickCounter;

    public SmartTileEntity() {

        setLazyTickRate(10);

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
    }

    public void destroy() {
        //forEachBehaviour(BlockEntityBehaviour::destroy);
    }


    public void initialize() {
        lazyTick();
    }

    public void setLazyTickRate(int slowTickRate) {
        this.lazyTickRate = slowTickRate;
        this.lazyTickCounter = slowTickRate;
    }

    public void lazyTick() {

    }
}
