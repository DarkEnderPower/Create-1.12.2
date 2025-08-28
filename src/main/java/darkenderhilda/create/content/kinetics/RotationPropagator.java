package darkenderhilda.create.content.kinetics;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.content.kinetics.gearbox.GearboxTileEntity;
import darkenderhilda.create.content.kinetics.transmission.SplitShaftTileEntity;
import darkenderhilda.create.foundation.utils.Iterate;
import darkenderhilda.create.foundation.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static darkenderhilda.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;

public class RotationPropagator {

    private static final int MAX_FLICKER_SCORE = 128;

    private static float getRotationSpeedModifier(KineticTileEntity from, KineticTileEntity to) {
        final IBlockState stateFrom = WorldUtils.stateFormTE(from);
        final IBlockState stateTo = WorldUtils.stateFormTE(to);
        final IRotate definitionFrom = (IRotate) stateFrom.getBlock();
        final IRotate definitionTo = (IRotate) stateTo.getBlock();
        final BlockPos diff = to.getPos().subtract(from.getPos());
        final EnumFacing direction = EnumFacing.getFacingFromVector(diff.getX(), diff.getY(), diff.getZ());
        final World world = from.getWorld();

        boolean alignedAxes = true;
        for (EnumFacing.Axis axis : EnumFacing.Axis.values())
            if (axis != direction.getAxis())
                if (getCoordinate(axis, diff.getX(), diff.getY(), diff.getZ()) != 0)
                    alignedAxes = false;

        boolean connectedByAxis =
                alignedAxes && definitionFrom.hasShaftTowards(world, from.getPos(), stateFrom, direction)
                        && definitionTo.hasShaftTowards(world, to.getPos(), stateTo, direction.getOpposite());

        boolean connectedByGears = definitionFrom.hasIntegratedCogwheel(world, from.getPos(), stateFrom)
                && definitionTo.hasIntegratedCogwheel(world, to.getPos(), stateTo);

        // Belt <-> Belt
//        if (from instanceof BeltTileEntity && to instanceof BeltTileEntity && !connectedByAxis) {
//            return ((BeltTileEntity) from).getController().equals(((BeltTileEntity) to).getController()) ? 1 : 0;
//        }

        // Axis <-> Axis
        if (connectedByAxis) {
            float axisModifier = getAxisModifier(to, direction.getOpposite());
            if (axisModifier != 0)
                axisModifier = 1 / axisModifier;
            return getAxisModifier(from, direction) * axisModifier;
        }

        // Attached Encased Belts
//        if (stateFrom.getBlock() instanceof EncasedBeltBlock && stateTo.getBlock() instanceof EncasedBeltBlock) {
//            boolean connected = EncasedBeltBlock.areBlocksConnected(stateFrom, stateTo, direction);
//            return connected ? EncasedBeltBlock.getRotationSpeedModifier(from, to) : 0;
//        }

        // Large Gear <-> Large Gear
        if (isLargeToLargeGear(stateFrom, stateTo, diff)) {
            EnumFacing.Axis sourceAxis = stateFrom.getValue(AXIS);
            EnumFacing.Axis targetAxis = stateTo.getValue(AXIS);
            int sourceAxisDiff = getCoordinate(sourceAxis, diff.getX(), diff.getY(), diff.getZ());
            int targetAxisDiff = getCoordinate(targetAxis, diff.getX(), diff.getY(), diff.getZ());

            return sourceAxisDiff > 0 ^ targetAxisDiff > 0 ? -1 : 1;
        }

        // Gear <-> Large Gear
        if (WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, stateFrom) && definitionTo.hasIntegratedCogwheel(world, to.getPos(), stateTo))
            if (isLargeToSmallGear(stateFrom, stateTo, definitionTo, diff))
                return -2f;
        if (WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, stateTo) && definitionFrom.hasIntegratedCogwheel(world, from.getPos(), stateFrom))
            if (isLargeToSmallGear(stateTo, stateFrom, definitionFrom, diff))
                return -.5f;

        // Gear <-> Gear
        if (connectedByGears) {
            if (manhattanDistance(diff, BlockPos.ORIGIN) != 1)
                return 0;
            if (WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, stateTo))
                return 0;
            if (direction.getAxis() == definitionFrom.getRotationAxis(stateFrom))
                return 0;
            if (definitionFrom.getRotationAxis(stateFrom) == definitionTo.getRotationAxis(stateTo))
                return -1;
        }

        return 0;
    }

    private static float getConveyedSpeed(KineticTileEntity from, KineticTileEntity to) {
        final IBlockState stateFrom = WorldUtils.stateFormTE(from);
        final IBlockState stateTo = WorldUtils.stateFormTE(to);
        final BlockPos diff = to.getPos().subtract(from.getPos());

        // Rotation Speed Controller <-> Large Gear
//        if (isLargeGearToSpeedController(stateFrom, stateTo, diff))
//            return SpeedControllerTileEntity.getConveyedSpeed(from, to, true);
//        if (isLargeGearToSpeedController(stateTo, stateFrom, diff))
//            return SpeedControllerTileEntity.getConveyedSpeed(to, from, false);

        float rotationSpeedModifier = getRotationSpeedModifier(from, to);
        return from.getTheoreticalSpeed() * rotationSpeedModifier;
    }

    private static boolean isLargeToLargeGear(IBlockState from, IBlockState to, BlockPos diff) {
        if(!WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, from) || !WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, to))
            return false;
        EnumFacing.Axis fromAxis = from.getValue(AXIS);
        EnumFacing.Axis toAxis = to.getValue(AXIS);
        if (fromAxis == toAxis)
            return false;
        for (EnumFacing.Axis axis : EnumFacing.Axis.values()) {
            int axisDiff = getCoordinate(axis, diff.getX(), diff.getY(), diff.getZ());
            if (axis == fromAxis || axis == toAxis) {
                if (axisDiff == 0)
                    return false;

            } else if (axisDiff != 0)
                return false;
        }
        return true;
    }

    private static float getAxisModifier(KineticTileEntity te, EnumFacing direction) {
        if (!te.hasSource() || !(te instanceof DirectionalShaftHalvesBlockEntity))
            return 1;
        EnumFacing source = ((DirectionalShaftHalvesBlockEntity) te).getSourceFacing();

        if (te instanceof GearboxTileEntity)
            return direction.getAxis() == source.getAxis() ? direction == source ? 1 : -1
                    : direction.getAxisDirection() == source.getAxisDirection() ? -1 : 1;

        if (te instanceof SplitShaftTileEntity)
            return ((SplitShaftTileEntity) te).getRotationSpeedModifier(direction);

        return 1;
    }

    private static boolean isLargeToSmallGear(IBlockState from, IBlockState to, IRotate defTo, BlockPos diff) {
        EnumFacing.Axis axisFrom = from.getValue(AXIS);
        if (axisFrom != defTo.getRotationAxis(to))
            return false;
        if (getCoordinate(axisFrom, diff.getX(), diff.getY(), diff.getZ()) != 0)
            return false;
        for (EnumFacing.Axis axis : EnumFacing.Axis.values()) {
            if (axis == axisFrom)
                continue;
            if (Math.abs(getCoordinate(axis, diff.getX(), diff.getY(), diff.getZ())) != 1)
                return false;
        }
        return true;
    }

    private static boolean isLargeGearToSpeedController(IBlockState from, IBlockState to, BlockPos diff) {
//        if (!LARGE_COGWHEEL.typeOf(from) || !AllBlocks.ROTATION_SPEED_CONTROLLER.typeOf(to))
//            return false;
//        if (!diff.equals(BlockPos.ZERO.up()) && !diff.equals(BlockPos.ZERO.down()))
//            return false;
//        return true;
        return false;
    }

    public static void handleAdded(World worldIn, BlockPos pos, KineticTileEntity addedTE) {
        if (worldIn.isRemote || isFrozen())
            return;
        if (!worldIn.isBlockLoaded(pos))
            return;
        propagateNewSource(addedTE);
    }

    private static void propagateNewSource(KineticTileEntity currentTE) {
        BlockPos pos = currentTE.getPos();
        World world = currentTE.getWorld();

        for (KineticTileEntity neighbourTE : getConnectedNeighbours(currentTE)) {
            float speedOfCurrent = currentTE.getTheoreticalSpeed();
            float speedOfNeighbour = neighbourTE.getTheoreticalSpeed();
            float newSpeed = getConveyedSpeed(currentTE, neighbourTE);
            float oppositeSpeed = getConveyedSpeed(neighbourTE, currentTE);

            boolean incompatible =
                    Math.signum(newSpeed) != Math.signum(speedOfNeighbour) && (newSpeed != 0 && speedOfNeighbour != 0);

            boolean tooFast = Math.abs(newSpeed) > 1000; //AllConfigs.SERVER.kinetics.maxRotationSpeed.get();
            boolean speedChangedTooOften = currentTE.getFlickerScore() > MAX_FLICKER_SCORE;
            if (tooFast || speedChangedTooOften) {
                world.destroyBlock(pos, true);
                return;
            }

            // Opposite directions
            if (incompatible) {
                world.destroyBlock(pos, true);
                return;

                // Same direction: overpower the slower speed
            } else {

                // Neighbour faster, overpower the incoming tree
                if (Math.abs(oppositeSpeed) > Math.abs(speedOfCurrent)) {
                    float prevSpeed = currentTE.getSpeed();
                    currentTE.setSource(neighbourTE.getPos());
                    currentTE.setSpeed(getConveyedSpeed(neighbourTE, currentTE));
                    currentTE.onSpeedChanged(prevSpeed);
                    currentTE.sendData();

                    propagateNewSource(currentTE);
                    return;
                }

                // Current faster, overpower the neighbours' tree
                if (Math.abs(newSpeed) >= Math.abs(speedOfNeighbour)) {

                    // Do not overpower you own network -> cycle
                    if (!currentTE.hasNetwork() || currentTE.network.equals(neighbourTE.network)) {
                        float epsilon = Math.abs(speedOfNeighbour) / 256f / 256f;
                        if (Math.abs(newSpeed) > Math.abs(speedOfNeighbour) + epsilon)
                            world.destroyBlock(pos, true);
                        continue;
                    }

                    if (currentTE.hasSource() && currentTE.source.equals(neighbourTE.getPos()))
                        currentTE.removeSource();

                    float prevSpeed = neighbourTE.getSpeed();
                    neighbourTE.setSource(currentTE.getPos());
                    neighbourTE.setSpeed(getConveyedSpeed(currentTE, neighbourTE));
                    neighbourTE.onSpeedChanged(prevSpeed);
                    neighbourTE.sendData();
                    propagateNewSource(neighbourTE);
                    continue;
                }
            }

            if (neighbourTE.getTheoreticalSpeed() == newSpeed)
                continue;

            float prevSpeed = neighbourTE.getSpeed();
            neighbourTE.setSpeed(newSpeed);
            neighbourTE.setSource(currentTE.getPos());
            neighbourTE.onSpeedChanged(prevSpeed);
            neighbourTE.sendData();
            propagateNewSource(neighbourTE);

        }
    }

    public static void handleRemoved(World worldIn, BlockPos pos, KineticTileEntity removedTE) {
        if (worldIn.isRemote || isFrozen())
            return;
        if (removedTE == null)
            return;
        if (removedTE.getTheoreticalSpeed() == 0)
            return;

        for (BlockPos neighbourPos : getPotentialNeighbourLocations(removedTE)) {
            IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
            if (!(neighbourState.getBlock() instanceof IRotate))
                continue;
            TileEntity tileEntity = worldIn.getTileEntity(neighbourPos);
            if (!(tileEntity instanceof KineticTileEntity))
                continue;

            final KineticTileEntity neighbourTE = (KineticTileEntity) tileEntity;
            if (!neighbourTE.hasSource() || !neighbourTE.source.equals(pos))
                continue;

            propagateMissingSource(neighbourTE);
        }

    }

    private static void propagateMissingSource(KineticTileEntity updateTE) {
        final World world = updateTE.getWorld();

        List<KineticTileEntity> potentialNewSources = new LinkedList<>();
        List<BlockPos> frontier = new LinkedList<>();
        frontier.add(updateTE.getPos());
        BlockPos missingSource = updateTE.hasSource() ? updateTE.source : null;

        while (!frontier.isEmpty()) {
            final BlockPos pos = frontier.remove(0);
            TileEntity tileEntity = world.getTileEntity(pos);
            if (!(tileEntity instanceof KineticTileEntity))
                continue;
            final KineticTileEntity currentTE = (KineticTileEntity) tileEntity;

            currentTE.removeSource();
            currentTE.sendData();

            for (KineticTileEntity neighbourTE : getConnectedNeighbours(currentTE)) {
                if (neighbourTE.getPos().equals(missingSource))
                    continue;
                if (!neighbourTE.hasSource())
                    continue;

                if (!neighbourTE.source.equals(pos)) {
                    potentialNewSources.add(neighbourTE);
                    continue;
                }

                if (neighbourTE.isSource())
                    potentialNewSources.add(neighbourTE);

                frontier.add(neighbourTE.getPos());
            }
        }

        for (KineticTileEntity newSource : potentialNewSources) {
            if (newSource.hasSource() || newSource.isSource()) {
                propagateNewSource(newSource);
                return;
            }
        }
    }

    private static KineticTileEntity findConnectedNeighbour(KineticTileEntity currentTE, BlockPos neighbourPos) {
        IBlockState neighbourState = currentTE.getWorld().getBlockState(neighbourPos);
        if (!(neighbourState.getBlock() instanceof IRotate))
            return null;
        if (!WorldUtils.hasTileEntity(neighbourState))
            return null;
        TileEntity neighbourTE = currentTE.getWorld().getTileEntity(neighbourPos);
        if (!(neighbourTE instanceof KineticTileEntity))
            return null;
        KineticTileEntity neighbourKTE = (KineticTileEntity) neighbourTE;
        if (!(WorldUtils.stateFormTE(neighbourKTE).getBlock() instanceof IRotate))
            return null;
        if (!isConnected(currentTE, neighbourKTE))
            return null;
        return neighbourKTE;
    }

    public static boolean isConnected(KineticTileEntity from, KineticTileEntity to) {
        final IBlockState stateFrom = WorldUtils.stateFormTE(from);
        final IBlockState stateTo = WorldUtils.stateFormTE(to);
        final BlockPos diff = to.getPos().subtract(from.getPos());

        if (isLargeGearToSpeedController(stateFrom, stateTo, diff))
            return true;
        if (isLargeGearToSpeedController(stateTo, stateFrom, diff))
            return true;
        return getRotationSpeedModifier(from, to) != 0;
    }

    private static List<KineticTileEntity> getConnectedNeighbours(KineticTileEntity te) {
        List<KineticTileEntity> neighbours = new LinkedList<>();
        for (BlockPos neighbourPos : getPotentialNeighbourLocations(te)) {
            final KineticTileEntity neighbourTE = findConnectedNeighbour(te, neighbourPos);
            if (neighbourTE == null)
                continue;

            neighbours.add(neighbourTE);
        }

        return neighbours;
    }

    private static List<BlockPos> getPotentialNeighbourLocations(KineticTileEntity te) {
        List<BlockPos> neighbours = new LinkedList<>();

        if (!te.getWorld().isAreaLoaded(te.getPos(), 1))
            return neighbours;

        for (EnumFacing facing : Iterate.facing)
            neighbours.add(te.getPos().offset(facing));

        // Some Blocks can interface diagonally
        IBlockState blockState = WorldUtils.stateFormTE(te);
        boolean isLargeWheel = WorldUtils.typeOf(AllBlocks.LARGE_COGWHEEL, blockState);

        if (!(blockState.getBlock() instanceof IRotate))
            return neighbours;
        IRotate block = (IRotate) blockState.getBlock();

        if (block.hasIntegratedCogwheel(te.getWorld(), te.getPos(), blockState) || isLargeWheel //|| BELT.typeOf(blockState)
                ) {
            EnumFacing.Axis axis = block.getRotationAxis(blockState);

            BlockPos.getAllInBox(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).forEach(offset -> {
                if (!isLargeWheel && getCoordinate(axis, offset.getX(), offset.getY(), offset.getZ()) != 0)
                    return;
                if (offset.distanceSq(0, 0, 0) != BlockPos.ORIGIN.distanceSq(1, 1, 0))
                    return;
                neighbours.add(te.getPos().add(offset));
            });
        }

        return neighbours;
    }

    public static boolean isFrozen() {
        //return AllConfigs.SERVER.control.freezeRotationPropagator.get();
        return false;
    }

    private static int getCoordinate(EnumFacing.Axis axis, int x, int y, int z) {
        if(axis == EnumFacing.Axis.X) {
            return x;
        } else if (axis == EnumFacing.Axis.Y) {
            return y;
        } else {
            return z;
        }
    }

    private static int manhattanDistance(BlockPos pos, Vec3i vec3i) {
        float x = (float)Math.abs(vec3i.getX() - pos.getX());
        float y = (float)Math.abs(vec3i.getY() - pos.getY());
        float z = (float)Math.abs(vec3i.getZ() - pos.getZ());
        return (int)(x + y + z);
    }
}
