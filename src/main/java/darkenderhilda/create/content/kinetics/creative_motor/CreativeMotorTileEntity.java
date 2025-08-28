package darkenderhilda.create.content.kinetics.creative_motor;

import darkenderhilda.create.content.kinetics.base.GeneratingKineticTileEntity;

public class CreativeMotorTileEntity extends GeneratingKineticTileEntity {

    public static final int DEFAULT_SPEED = 16;
    public static final int MAX_SPEED = 256;


    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
//        if (!AllBlocks.CREATIVE_MOTOR.has(getBlockState()))
//            return 0;
//        return convertToDirection(generatedSpeed.getValue(), getBlockState().getValue(CreativeMotorBlock.FACING));
        return DEFAULT_SPEED;
    }
}
