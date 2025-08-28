package darkenderhilda.create.content.kinetics.creative_motor;

import darkenderhilda.create.content.kinetics.base.GeneratingKineticTileEntity;

public class CreativeMotorTileEntity extends GeneratingKineticTileEntity {

    public static final int DEFAULT_SPEED = 32;

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        return DEFAULT_SPEED;
    }
}
