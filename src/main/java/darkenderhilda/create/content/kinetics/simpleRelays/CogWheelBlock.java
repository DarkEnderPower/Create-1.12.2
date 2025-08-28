package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.shapes.ExtendedShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CogWheelBlock extends AbstractSimpleShaftBlock implements ICogWheel {

    protected boolean isLarge;

    protected CogWheelBlock(boolean large, BlockProperties properties) {
        super(properties);
        isLarge = large;
    }

    public static CogWheelBlock small(BlockProperties properties) {
        return new CogWheelBlock(false, properties);
    }

    public static CogWheelBlock large(BlockProperties properties) {
        return new CogWheelBlock(true, properties);
    }

    @Override
    public boolean isLargeCog() {
        return isLarge;
    }

    @Override
    public boolean isSmallCog() {
        return !isLarge;
    }

    @Override
    public ExtendedShape getShape(IBlockState state) {
        return ExtendedShape.rotate((isLarge) ? AllShapes.LARGE_GEAR : AllShapes.SMALL_GEAR, state.getValue(AXIS));
    }

    @Override
    public boolean hasIntegratedCogwheel(IBlockAccess world, BlockPos pos, IBlockState state) {
        return !isLarge;
    }
}
