package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.content.kinetics.KineticNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class GeneratingKineticTileEntity extends KineticTileEntity {

    public boolean reActivateSource;

    protected void notifyStressCapacityChange(float capacity) {
        getOrCreateNetwork().updateCapacityFor(this, capacity);
    }

    @Override
    public void removeSource() {
        if (hasSource() && isSource())
            reActivateSource = true;
        super.removeSource();
    }

    @Override
    public void setSource(BlockPos source) {
        super.setSource(source);
        TileEntity tileEntity = world.getTileEntity(source);
        if (!(tileEntity instanceof KineticTileEntity))
            return;
        KineticTileEntity sourceTe = (KineticTileEntity) tileEntity;
        if (reActivateSource && sourceTe != null && Math.abs(sourceTe.getSpeed()) >= Math.abs(getGeneratedSpeed()))
            reActivateSource = false;
    }

    @Override
    public void update() {
        super.update();
        if (reActivateSource) {
            updateGeneratedRotation();
            reActivateSource = false;
        }
    }

    public void updateGeneratedRotation() {
        float speed = getGeneratedSpeed();
        float prevSpeed = this.speed;

        if (world.isRemote)
            return;

        if (prevSpeed != speed) {
//            if (!hasSource()) {
//                SpeedLevel levelBefore = SpeedLevel.of(this.speed);
//                SpeedLevel levelafter = SpeedLevel.of(speed);
//                if (levelBefore != levelafter)
//                    effects.queueRotationIndicators();
//            }

            applyNewSpeed(prevSpeed, speed);
        }

        if (hasNetwork() && speed != 0) {
            KineticNetwork network = getOrCreateNetwork();
            notifyStressCapacityChange(calculateAddedStressCapacity());
            getOrCreateNetwork().updateStressFor(this, calculateStressApplied());
            network.updateStress();
        }

        onSpeedChanged(prevSpeed);
        sendData();
    }

    public void applyNewSpeed(float prevSpeed, float speed) {

        // Speed changed to 0
        if (speed == 0) {
            if (hasSource()) {
                notifyStressCapacityChange(0);
                getOrCreateNetwork().updateStressFor(this, calculateStressApplied());
                return;
            }
            detachKinetics();
            setSpeed(0);
            setNetwork(null);
            return;
        }

        // Now turning - create a new Network
        if (prevSpeed == 0) {
            setSpeed(speed);
            setNetwork(createNetworkId());
            attachKinetics();
            return;
        }

        // Change speed when overpowered by other generator
        if (hasSource()) {

            // Staying below Overpowered speed
            if (Math.abs(prevSpeed) >= Math.abs(speed)) {
                if (Math.signum(prevSpeed) != Math.signum(speed))
                    world.destroyBlock(pos, true);
                return;
            }

            // Faster than attached network -> become the new source
            detachKinetics();
            setSpeed(speed);
            source = null;
            setNetwork(createNetworkId());
            attachKinetics();
            return;
        }

        // Reapply source
        detachKinetics();
        setSpeed(speed);
        attachKinetics();
    }

    public Long createNetworkId() {
        return pos.toLong();
    }

}
