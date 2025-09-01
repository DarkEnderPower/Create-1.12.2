package darkenderhilda.create.content.kinetics;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import darkenderhilda.create.content.kinetics.base.IRotate;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.content.kinetics.gearbox.GearboxTileEntity;
import darkenderhilda.create.content.kinetics.simpleRelays.ICogWheel;
import darkenderhilda.create.content.kinetics.speedController.SpeedControllerTileEntity;
import darkenderhilda.create.content.kinetics.transmission.SplitShaftTileEntity;
import darkenderhilda.create.foundation.utility.Iterate;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;
import static darkenderhilda.create.foundation.block.BlockData.HORIZONTAL_AXIS;
import static darkenderhilda.create.foundation.utility.WorldUtils.choose;

public class RotationPropagator {


    private static final int MAX_FLICKER_SCORE = 128;

    /**
     * Determines the change in rotation between two attached kinetic entities. For
     * instance, an axis connection returns 1 while a 1-to-1 gear connection
     * reverses the rotation and therefore returns -1.
     *
     * @param from
     * @param to
     * @return
     */
    private static float getRotationSpeedModifier(KineticTileEntity from, KineticTileEntity to) {
        final IBlockState stateFrom = WorldUtils.stateFormTE(from);
        
        final IBlockState stateTo = WorldUtils.stateFormTE(to);

        Block fromBlock = stateFrom.getBlock();
        Block toBlock = stateTo.getBlock();

        IRotate definitionFrom;
        IRotate definitionTo;
        if (!(fromBlock instanceof IRotate && toBlock instanceof IRotate)) {
            return 0;
        } else {
            definitionFrom = (IRotate) fromBlock;
            definitionTo = (IRotate) toBlock;
        }
        final BlockPos diff = to.getPos()
                .subtract(from.getPos());
        final EnumFacing direction = WorldUtils.getNearest(diff.getX(), diff.getY(), diff.getZ());
        final World world = from.getWorld();

        boolean alignedAxes = true;
        for (EnumFacing.Axis axis : EnumFacing.Axis.values())
            if (axis != direction.getAxis())
                if (choose(axis, diff.getX(), diff.getY(), diff.getZ()) != 0)
                    alignedAxes = false;

        boolean connectedByAxis =
                alignedAxes && definitionFrom.hasShaftTowards(world, from.getPos(), stateFrom, direction)
                        && definitionTo.hasShaftTowards(world, to.getPos(), stateTo, direction.getOpposite());

        boolean connectedByGears = ICogWheel.isSmallCog(stateFrom) && ICogWheel.isSmallCog(stateTo);

        float custom = from.propagateRotationTo(to, stateFrom, stateTo, diff, connectedByAxis, connectedByGears);
        if (custom != 0)
            return custom;

        // Axis <-> Axis
        if (connectedByAxis) {
            float axisModifier = getAxisModifier(to, direction.getOpposite());
            if (axisModifier != 0)
                axisModifier = 1 / axisModifier;
            return getAxisModifier(from, direction) * axisModifier;
        }

        // Attached Encased Belts
//        if (fromBlock instanceof ChainDriveBlock && toBlock instanceof ChainDriveBlock) {
//            boolean connected = ChainDriveBlock.areBlocksConnected(stateFrom, stateTo, direction);
//            return connected ? ChainDriveBlock.getRotationSpeedModifier(from, to) : 0;
//        }

        // Large Gear <-> Large Gear
        if (isLargeToLargeGear(stateFrom, stateTo, diff)) {
            EnumFacing.Axis sourceAxis = stateFrom.getValue(AXIS);
            EnumFacing.Axis targetAxis = stateTo.getValue(AXIS);
            int sourceAxisDiff = choose(sourceAxis, diff.getX(), diff.getY(), diff.getZ());
            int targetAxisDiff = choose(targetAxis, diff.getX(), diff.getY(), diff.getZ());

            return sourceAxisDiff > 0 ^ targetAxisDiff > 0 ? -1 : 1;
        }

        // Gear <-> Large Gear
        if (ICogWheel.isLargeCog(stateFrom) && ICogWheel.isSmallCog(stateTo))
            if (isLargeToSmallCog(stateFrom, stateTo, definitionTo, diff))
                return -2f;
        if (ICogWheel.isLargeCog(stateTo) && ICogWheel.isSmallCog(stateFrom))
            if (isLargeToSmallCog(stateTo, stateFrom, definitionFrom, diff))
                return -.5f;

        // Gear <-> Gear
        if (connectedByGears) {
            if (manhattanDistance(diff, BlockPos.ORIGIN) != 1)
                return 0;
            if (ICogWheel.isLargeCog(stateTo))
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

        // Rotation Speed Controller <-> Large Gear
        if (isLargeCogToSpeedController(stateFrom, stateTo, to.getPos()
                .subtract(from.getPos())))
            return SpeedControllerTileEntity.getConveyedSpeed(from, to, true);
        if (isLargeCogToSpeedController(stateTo, stateFrom, from.getPos()
                .subtract(to.getPos())))
            return SpeedControllerTileEntity.getConveyedSpeed(to, from, false);

        float rotationSpeedModifier = getRotationSpeedModifier(from, to);
        return from.getTheoreticalSpeed() * rotationSpeedModifier;
    }

    private static boolean isLargeToLargeGear(IBlockState from, IBlockState to, BlockPos diff) {
        if (!ICogWheel.isLargeCog(from) || !ICogWheel.isLargeCog(to))
            return false;
        EnumFacing.Axis fromAxis = from.getValue(AXIS);
        EnumFacing.Axis toAxis = to.getValue(AXIS);
        if (fromAxis == toAxis)
            return false;
        for (EnumFacing.Axis axis : EnumFacing.Axis.values()) {
            int axisDiff = choose(axis, diff.getX(), diff.getY(), diff.getZ());
            if (axis == fromAxis || axis == toAxis) {
                if (axisDiff == 0)
                    return false;

            } else if (axisDiff != 0)
                return false;
        }
        return true;
    }

    private static float getAxisModifier(KineticTileEntity be, EnumFacing direction) {
        if (!(be.hasSource() || be.isSource()) || !(be instanceof DirectionalShaftHalvesBlockEntity))
            return 1;
        EnumFacing source = ((DirectionalShaftHalvesBlockEntity) be).getSourceFacing();

        if (be instanceof GearboxTileEntity)
            return direction.getAxis() == source.getAxis() ? direction == source ? 1 : -1
                    : direction.getAxisDirection() == source.getAxisDirection() ? -1 : 1;

        if (be instanceof SplitShaftTileEntity)
            return ((SplitShaftTileEntity) be).getRotationSpeedModifier(direction);

        return 1;
    }

    private static boolean isLargeToSmallCog(IBlockState from, IBlockState to, IRotate defTo, BlockPos diff) {
        EnumFacing.Axis axisFrom = from.getValue(AXIS);
        if (axisFrom != defTo.getRotationAxis(to))
            return false;
        if (choose(axisFrom, diff.getX(), diff.getY(), diff.getZ()) != 0)
            return false;
        for (EnumFacing.Axis axis : EnumFacing.Axis.values()) {
            if (axis == axisFrom)
                continue;
            if (Math.abs(choose(axis, diff.getX(), diff.getY(), diff.getZ())) != 1)
                return false;
        }
        return true;
    }

    private static boolean isLargeCogToSpeedController(IBlockState from, IBlockState to, BlockPos diff) {
        if (!ICogWheel.isLargeCog(from) || !WorldUtils.typeOf(AllBlocks.ROTATION_SPEED_CONTROLLER, to))
            return false;
        if (!diff.equals(BlockPos.ORIGIN.down()))
            return false;
        EnumFacing.Axis axis = from.getValue(AXIS);
        if (axis.isVertical())
            return false;
        if (to.getValue(HORIZONTAL_AXIS).getAxis() == axis)
            return false;
        return true;
    }

    /**
     * Insert the added position to the kinetic network.
     *
     * @param worldIn
     * @param pos
     */
    public static void handleAdded(World worldIn, BlockPos pos, KineticTileEntity addedTE) {
        if (worldIn.isRemote)
            return;
        if (!worldIn.isBlockLoaded(pos))
            return;
        propagateNewSource(addedTE);
    }

    /**
     * Search for sourceless networks attached to the given entity and update them.
     *
     * @param currentTE
     */
    private static void propagateNewSource(KineticTileEntity currentTE) {
        BlockPos pos = currentTE.getPos();
        World world = currentTE.getWorld();

        for (KineticTileEntity neighbourTE : getConnectedNeighbours(currentTE)) {
            float speedOfCurrent = currentTE.getTheoreticalSpeed();
            float speedOfNeighbour = neighbourTE.getTheoreticalSpeed();
            float newSpeed = getConveyedSpeed(currentTE, neighbourTE);
            float oppositeSpeed = getConveyedSpeed(neighbourTE, currentTE);

            if (newSpeed == 0 && oppositeSpeed == 0)
                continue;

            boolean incompatible =
                    Math.signum(newSpeed) != Math.signum(speedOfNeighbour) && (newSpeed != 0 && speedOfNeighbour != 0);

            boolean tooFast = false;//Math.abs(newSpeed) > AllConfigs.server().kinetics.maxRotationSpeed.get()
                   // || Math.abs(oppositeSpeed) > AllConfigs.server().kinetics.maxRotationSpeed.get();
            // Check for both the new speed and the opposite speed, just in case

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

    /**
     * Remove the given entity from the network.
     *
     * @param worldIn
     * @param pos
     * @param removedBE
     */
    public static void handleRemoved(World worldIn, BlockPos pos, KineticTileEntity removedBE) {
        if (worldIn.isRemote)
            return;
        if (removedBE == null)
            return;
        if (removedBE.getTheoreticalSpeed() == 0)
            return;

        for (BlockPos neighbourPos : getPotentialNeighbourLocations(removedBE)) {
            IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
            if (!(neighbourState.getBlock() instanceof IRotate))
                continue;
            TileEntity blockEntity = worldIn.getTileEntity(neighbourPos);
            KineticTileEntity neighbourBE;
            if (!(blockEntity instanceof KineticTileEntity)) {
                continue;
            } else {
                neighbourBE = (KineticTileEntity) blockEntity;
            }
            if (!neighbourBE.hasSource() || !neighbourBE.source.equals(pos))
                continue;

            propagateMissingSource(neighbourBE);
        }
    }

    /**
     * Clear the entire subnetwork depending on the given entity and find a new
     * source
     *
     * @param updateTE
     */
    private static void propagateMissingSource(KineticTileEntity updateTE) {
        final World world = updateTE.getWorld();

        List<KineticTileEntity> potentialNewSources = new LinkedList<>();
        List<BlockPos> frontier = new LinkedList<>();
        frontier.add(updateTE.getPos());
        BlockPos missingSource = updateTE.hasSource() ? updateTE.source : null;

        while (!frontier.isEmpty()) {
            final BlockPos pos = frontier.remove(0);
            TileEntity blockEntity = world.getTileEntity(pos);
            KineticTileEntity currentBE;
            if (!(blockEntity instanceof KineticTileEntity)) {
                continue;
            } else {
                currentBE = (KineticTileEntity) blockEntity;
            }

            currentBE.removeSource();
            currentBE.sendData();

            for (KineticTileEntity neighbourBE : getConnectedNeighbours(currentBE)) {
                if (neighbourBE.getPos()
                        .equals(missingSource))
                    continue;
                if (!neighbourBE.hasSource())
                    continue;

                if (!neighbourBE.source.equals(pos)) {
                    potentialNewSources.add(neighbourBE);
                    continue;
                }

                if (neighbourBE.isSource())
                    potentialNewSources.add(neighbourBE);

                frontier.add(neighbourBE.getPos());
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
        IBlockState neighbourState = currentTE.getWorld()
                .getBlockState(neighbourPos);
        if (!(neighbourState.getBlock() instanceof IRotate))
            return null;
        if (!WorldUtils.hasTileEntity(neighbourState))
            return null;
        TileEntity neighbourBE = currentTE.getWorld().getTileEntity(neighbourPos);
        KineticTileEntity neighbourKBE;
        if (!(neighbourBE instanceof KineticTileEntity)) {
            return null;
        } else {
            neighbourKBE = (KineticTileEntity) neighbourBE;
        }
        if (!(WorldUtils.stateFormTE(neighbourKBE).getBlock() instanceof IRotate))
            return null;
        if (!isConnected(currentTE, neighbourKBE) && !isConnected(neighbourKBE, currentTE))
            return null;
        return neighbourKBE;
    }

    public static boolean isConnected(KineticTileEntity from, KineticTileEntity to) {
        final IBlockState stateFrom = WorldUtils.stateFormTE(from);
        final IBlockState stateTo = WorldUtils.stateFormTE(to);
        return isLargeCogToSpeedController(stateFrom, stateTo, to.getPos().subtract(from.getPos()))
                || getRotationSpeedModifier(from, to) != 0
                || from.isCustomConnection(to, stateFrom, stateTo);
    }

    private static List<KineticTileEntity> getConnectedNeighbours(KineticTileEntity be) {
        List<KineticTileEntity> neighbours = new LinkedList<>();
        for (BlockPos neighbourPos : getPotentialNeighbourLocations(be)) {
            final KineticTileEntity neighbourBE = findConnectedNeighbour(be, neighbourPos);
            if (neighbourBE == null)
                continue;

            neighbours.add(neighbourBE);
        }
        return neighbours;
    }

    private static List<BlockPos> getPotentialNeighbourLocations(KineticTileEntity be) {
        List<BlockPos> neighbours = new LinkedList<>();
        BlockPos blockPos = be.getPos();
        World level = be.getWorld();

        if (!level.isBlockLoaded(blockPos))
            return neighbours;

        for (EnumFacing facing : Iterate.direction) {
            BlockPos relative = blockPos.offset(facing);
            if (level.isBlockLoaded(relative))
                neighbours.add(relative);
        }

        IBlockState blockState = WorldUtils.stateFormTE(be);
        IRotate block;
        if (!(blockState.getBlock() instanceof IRotate)) {
            return neighbours;
        } else {
            block = (IRotate) blockState.getBlock();
        }
        return be.addPropagationLocations(block, blockState, neighbours);
    }

    private static int manhattanDistance(BlockPos pos, Vec3i vec3i) {
        float x = (float)Math.abs(vec3i.getX() - pos.getX());
        float y = (float)Math.abs(vec3i.getY() - pos.getY());
        float z = (float)Math.abs(vec3i.getZ() - pos.getZ());
        return (int)(x + y + z);
    }
}
