package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public class CogWheelBlock extends AbstractSimpleShaftBlock implements ICogWheel {

    protected final boolean isLarge;

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
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return (isLarge ? AllShapes.LARGE_GEAR : AllShapes.SMALL_GEAR).get(state.getValue(AXIS));
    }

    @Override
    public boolean isDedicatedCogWheel() {
        return true;
    }
 }
