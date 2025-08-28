package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.api.kinetics.PlacementHelper;
import darkenderhilda.create.content.equipment.wrench.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IRotate extends IWrenchable {

    enum SpeedLevel {
        NONE(),
        SLOW(),
        MEDIUM(),
        FAST();
    }

    boolean hasIntegratedCogwheel(IBlockAccess world, BlockPos pos, IBlockState state);

    boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face);

    EnumFacing.Axis getRotationAxis(IBlockState state);

    default SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.NONE;
    }
}
