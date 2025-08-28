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
        TileEntity blockEntity = world.getTileEntity(source);
        KineticTileEntity sourceBE;
        if (!(blockEntity instanceof KineticTileEntity)) {
            return;
        } else {
            sourceBE = (KineticTileEntity) blockEntity;
        }
        if (reActivateSource && Math.abs(sourceBE.getSpeed()) >= Math.abs(getGeneratedSpeed()))
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

//    @Override
//    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
//        if (!StressImpact.isEnabled())
//            return added;
//
//        float stressBase = calculateAddedStressCapacity();
//        if (Mth.equal(stressBase, 0))
//            return added;
//
//        CreateLang.translate("gui.goggles.generator_stats")
//                .forGoggles(tooltip);
//        CreateLang.translate("tooltip.capacityProvided")
//                .style(ChatFormatting.GRAY)
//                .forGoggles(tooltip);
//
//        float speed = getTheoreticalSpeed();
//        if (speed != getGeneratedSpeed() && speed != 0)
//            stressBase *= getGeneratedSpeed() / speed;
//
//        float stressTotal = Math.abs(stressBase * speed);
//
//        CreateLang.number(stressTotal)
//                .translate("generic.unit.stress")
//                .style(ChatFormatting.AQUA)
//                .space()
//                .add(CreateLang.translate("gui.goggles.at_current_speed")
//                        .style(ChatFormatting.DARK_GRAY))
//                .forGoggles(tooltip, 1);
//
//        return true;
//    }

    public void updateGeneratedRotation() {
        float speed = getGeneratedSpeed();
        float prevSpeed = this.speed;

        if (world == null || world.isRemote)
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
                    world.destroyBlock(getPos(), true);
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
        return getPos().toLong();
    }
}
