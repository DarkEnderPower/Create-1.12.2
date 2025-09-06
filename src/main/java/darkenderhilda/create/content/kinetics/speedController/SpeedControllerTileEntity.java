package darkenderhilda.create.content.kinetics.speedController;

import darkenderhilda.create.content.kinetics.RotationPropagator;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.content.kinetics.simpleRelays.ICogWheel;
import net.minecraft.block.state.IBlockState;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public class SpeedControllerTileEntity extends KineticTileEntity {

    public static final int DEFAULT_SPEED = 64;

    boolean hasBracket;

    public SpeedControllerTileEntity() {
        hasBracket = false;
    }

    @Override
    public boolean hasFastRenderer() {
        return false;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        updateBracket();
    }

    private void updateTargetRotation() {
        if (hasNetwork())
            getOrCreateNetwork().remove(this);
        RotationPropagator.handleRemoved(world, getPos(), this);
        removeSource();
        attachKinetics();

        //TODO REMOVE?
//        if (isCogwheelPresent() && getSpeed() != 0)
//            award(AllAdvancements.SPEED_CONTROLLER);
    }

    public static float getConveyedSpeed(KineticTileEntity cogWheel, KineticTileEntity speedControllerIn,
                                         boolean targetingController) {
        if (!(speedControllerIn instanceof SpeedControllerTileEntity))
            return 0;

        float speed = speedControllerIn.getTheoreticalSpeed();
        float wheelSpeed = cogWheel.getTheoreticalSpeed();
        float desiredOutputSpeed = getDesiredOutputSpeed(cogWheel, speedControllerIn, targetingController);

        float compareSpeed = targetingController ? speed : wheelSpeed;
        if (desiredOutputSpeed >= 0 && compareSpeed >= 0)
            return Math.max(desiredOutputSpeed, compareSpeed);
        if (desiredOutputSpeed < 0 && compareSpeed < 0)
            return Math.min(desiredOutputSpeed, compareSpeed);

        return desiredOutputSpeed;
    }

    public static float getDesiredOutputSpeed(KineticTileEntity cogWheel, KineticTileEntity speedControllerIn,
                                              boolean targetingController) {
        SpeedControllerTileEntity speedController = (SpeedControllerTileEntity) speedControllerIn;
        float targetSpeed = 16;//speedController.targetSpeed.getValue();
        float speed = speedControllerIn.getTheoreticalSpeed();
        float wheelSpeed = cogWheel.getTheoreticalSpeed();

        if (targetSpeed == 0)
            return 0;
        if (targetingController && wheelSpeed == 0)
            return 0;
        if (!speedController.hasSource()) {
            if (targetingController)
                return targetSpeed;
            return 0;
        }

        boolean wheelPowersController = speedController.source.equals(cogWheel.getPos());

        if (wheelPowersController) {
            if (targetingController)
                return targetSpeed;
            return wheelSpeed;
        }

        if (targetingController)
            return speed;
        return targetSpeed;
    }

    public void updateBracket() {
        if (world != null && world.isRemote)
            hasBracket = isCogwheelPresent();
    }

    private boolean isCogwheelPresent() {
        IBlockState stateAbove = world.getBlockState(getPos().up());
        return ICogWheel.isDedicatedCogWheel(stateAbove.getBlock()) && ICogWheel.isLargeCog(stateAbove)
                && stateAbove.getValue(AXIS)
                .isHorizontal();
    }
}
