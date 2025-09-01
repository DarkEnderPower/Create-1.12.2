package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.Create;
import darkenderhilda.create.api.equipment.goggles.IHaveGoggleInformation;
import darkenderhilda.create.api.equipment.goggles.IHaveHoveringInformation;
import darkenderhilda.create.content.kinetics.KineticNetwork;
import darkenderhilda.create.content.kinetics.RotationPropagator;
import darkenderhilda.create.content.kinetics.simpleRelays.ICogWheel;
import darkenderhilda.create.foundation.tileEntity.SmartTileEntity;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.List;

import static darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticTESR.isAxisShifted;
import static darkenderhilda.create.foundation.utility.WorldUtils.choose;

public abstract class KineticTileEntity extends SmartTileEntity implements IHaveGoggleInformation, IHaveHoveringInformation {

    public @Nullable Long network;
    public @Nullable BlockPos source;
    public boolean networkDirty;
    public boolean updateSpeed;
    public int preventSpeedUpdate;

    //protected KineticEffectHandler effects;
    protected float speed;
    protected float capacity;
    protected float stress;
    protected boolean overStressed;
    protected boolean wasMoved;

    private int flickerTally;
    private int networkSize;
    private int validationCountdown;
    protected float lastStressApplied;
    protected float lastCapacityProvided;

    //public SequenceContext sequenceContext;


    public KineticTileEntity() {
        updateSpeed = true;
    }

    @Override
    public void initialize() {
        if (hasNetwork() && !world.isRemote) {
            KineticNetwork network = getOrCreateNetwork();
            if (!network.initialized)
                network.initFromTE(capacity, stress, networkSize);
            network.addSilently(this, lastCapacityProvided, lastStressApplied);
        }

        super.initialize();
    }

    @Override
    public void update() {
        if (!world.isRemote && needsSpeedUpdate())
            attachKinetics();

        super.update();
        //effects.tick();

        preventSpeedUpdate = 0;

        if (world.isRemote) {
            //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.tickAudio());
            return;
        }

        if (validationCountdown-- <= 0) {
            validationCountdown = 60;//AllConfigs.server().kinetics.kineticValidationFrequency.get();
            validateKinetics();
        }

        if (getFlickerScore() > 0)
            flickerTally = getFlickerScore() - 1;

        if (networkDirty) {
            if (hasNetwork())
                getOrCreateNetwork().updateNetwork();
            networkDirty = false;
        }
    }

    private void validateKinetics() {
        if (hasSource()) {
            if (!hasNetwork()) {
                removeSource();
                return;
            }

            if (!world.isBlockLoaded(source))
                return;

            TileEntity blockEntity = world.getTileEntity(source);
            KineticTileEntity sourceBE = blockEntity instanceof KineticTileEntity ? (KineticTileEntity) blockEntity : null;
            if (sourceBE == null || sourceBE.speed == 0) {
                removeSource();
                detachKinetics();
                return;
            }

            return;
        }

        if (speed != 0) {
            if (getGeneratedSpeed() == 0)
                speed = 0;
        }
    }

    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        networkDirty = false;
        this.capacity = maxStress;
        this.stress = currentStress;
        this.networkSize = networkSize;
        boolean overStressed = maxStress < currentStress && true;//StressImpact.isEnabled();
        //TODO
        //setChanged();
        sendData();

        if (overStressed != this.overStressed) {
            float prevSpeed = getSpeed();
            this.overStressed = overStressed;
            onSpeedChanged(prevSpeed);
            sendData();
        }
    }

    protected Block getStressConfigKey() {
        return WorldUtils.stateFormTE(this).getBlock();
    }

    public float calculateStressApplied() {
        //float impact = (float) BlockStressValues.getImpact(getStressConfigKey());
        float impact = 1;
        this.lastStressApplied = impact;
        return impact;
    }

    public float calculateAddedStressCapacity() {
        //float capacity = (float) BlockStressValues.getCapacity(getStressConfigKey());
        float capacity = 128;
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    public void onSpeedChanged(float previousSpeed) {
        boolean fromOrToZero = (previousSpeed == 0) != (getSpeed() == 0);
        boolean directionSwap = !fromOrToZero && Math.signum(previousSpeed) != Math.signum(getSpeed());
        if (fromOrToZero || directionSwap)
            flickerTally = getFlickerScore() + 5;
        //TODO
        //setChanged();
        sendData();
    }

    @Override
    public void invalidate() {
        if (!world.isRemote) {
            if (hasNetwork())
                getOrCreateNetwork().remove(this);
            detachKinetics();
        }
        super.invalidate();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setFloat("Speed", speed);
        //if (sequenceContext != null && (!clientPacket || syncSequenceContext()))
        //    compound.put("Sequence", sequenceContext.serializeNBT());

        if (needsSpeedUpdate())
            compound.setBoolean("NeedsSpeedUpdate", true);

        if (hasSource())
            compound.setTag("Source", NBTUtil.createPosTag(source));

        if (hasNetwork()) {
            NBTTagCompound networkTag = new NBTTagCompound();
            networkTag.setLong("Id", this.network);
            networkTag.setFloat("Stress", stress);
            networkTag.setFloat("Capacity", capacity);
            networkTag.setInteger("Size", networkSize);

            if (lastStressApplied != 0)
                networkTag.setFloat("AddedStress", lastStressApplied);
            if (lastCapacityProvided != 0)
                networkTag.setFloat("AddedCapacity", lastCapacityProvided);

            compound.setTag("Network", networkTag);
        }

        return super.writeToNBT(compound);
    }


    public boolean needsSpeedUpdate() {
        return updateSpeed;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        boolean overStressedBefore = overStressed;
        clearKineticInformation();

        // DO NOT READ kinetic information when placed after movement
        if (wasMoved) {
            super.readFromNBT(compound);
            return;
        }

        speed = compound.getFloat("Speed");
        //sequenceContext = SequenceContext.fromNBT(compound.getCompound("Sequence"));

        if (compound.hasKey("Source"))
            source = NBTUtil.getPosFromTag((NBTTagCompound) compound.getTag("Source"));

        if (compound.hasKey("Network")) {
            NBTTagCompound networkTag = (NBTTagCompound) compound.getTag("Network");
            network = networkTag.getLong("Id");
            stress = networkTag.getFloat("Stress");
            capacity = networkTag.getFloat("Capacity");
            networkSize = networkTag.getInteger("Size");
            lastStressApplied = networkTag.getFloat("AddedStress");
            lastCapacityProvided = networkTag.getFloat("AddedCapacity");
            overStressed = capacity < stress &&  true;//StressImpact.isEnabled();
        }

        super.readFromNBT(compound);

//        if (clientPacket && overStressedBefore != overStressed && speed != 0)
//            effects.triggerOverStressedEffect();
//
//        if (clientPacket)
//            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> VisualizationHelper.queueUpdate(this));
    }

    public float getGeneratedSpeed() {
        return 0;
    }

    public boolean isSource() {
        return getGeneratedSpeed() != 0;
    }

    public float getSpeed() {
        if (overStressed)
            return 0;
        return getTheoreticalSpeed();
    }

    public float getTheoreticalSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean hasSource() {
        return source != null;
    }

    public void setSource(BlockPos source) {
        this.source = source;
        if (world == null || world.isRemote)
            return;

        TileEntity blockEntity = world.getTileEntity(source);
        KineticTileEntity sourceBE;
        if (!(blockEntity instanceof KineticTileEntity)) {
            removeSource();
            return;
        } else {
            sourceBE = (KineticTileEntity) blockEntity;
        }

        setNetwork(sourceBE.network);
        //copySequenceContextFrom(sourceBE);
    }

//    protected void copySequenceContextFrom(KineticBlockEntity sourceBE) {
//        sequenceContext = sourceBE.sequenceContext;
//    }

    public void removeSource() {
        float prevSpeed = getSpeed();

        speed = 0;
        source = null;
        setNetwork(null);
        //sequenceContext = null;

        onSpeedChanged(prevSpeed);
    }

    public void setNetwork(@Nullable Long networkIn) {
        if (network == networkIn)
            return;
        if (network != null)
            getOrCreateNetwork().remove(this);

        network = networkIn;
        //TODO
        //setChanged();
        sendData();

        if (networkIn == null)
            return;

        network = networkIn;
        KineticNetwork network = getOrCreateNetwork();
        network.initialized = true;
        network.add(this);
    }

    public KineticNetwork getOrCreateNetwork() {
        return Create.TORQUE_PROPAGATOR.getOrCreateNetworkFor(this);
    }

    public boolean hasNetwork() {
        return network != null;
    }

    public void attachKinetics() {
        updateSpeed = false;
        RotationPropagator.handleAdded(world, getPos(), this);
    }

    public void detachKinetics() {
        RotationPropagator.handleRemoved(world, getPos(), this);
    }

    public boolean isSpeedRequirementFulfilled() {
        IBlockState state = WorldUtils.stateFormTE(this);
        if (!(WorldUtils.stateFormTE(this).getBlock() instanceof IRotate))
            return true;
        IRotate def = (IRotate) state.getBlock();
        IRotate.SpeedLevel minimumRequiredSpeedLevel = def.getMinimumRequiredSpeedLevel();
        return Math.abs(getSpeed()) >= minimumRequiredSpeedLevel.getSpeedValue();
    }

    public static void switchToBlockState(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote)
            return;

        TileEntity blockEntity = world.getTileEntity(pos);
        IBlockState currentState = world.getBlockState(pos);
        boolean isKinetic = blockEntity instanceof KineticTileEntity;

        if (currentState == state)
            return;
        if (blockEntity == null || !isKinetic) {
            world.setBlockState(pos, state, 3);
            return;
        }

        KineticTileEntity kineticBlockEntity = (KineticTileEntity) blockEntity;
        if (state.getBlock() instanceof KineticBlock
                && !((KineticBlock) state.getBlock()).areStatesKineticallyEquivalent(currentState, state)) {
            if (kineticBlockEntity.hasNetwork())
                kineticBlockEntity.getOrCreateNetwork()
                        .remove(kineticBlockEntity);
            kineticBlockEntity.detachKinetics();
            kineticBlockEntity.removeSource();
        }

        if (blockEntity instanceof GeneratingKineticTileEntity) {
            ((GeneratingKineticTileEntity) blockEntity).reActivateSource = true;
        }

        world.setBlockState(pos, state, 3);
    }

    @Override
    public boolean addToTooltip(List<String> tooltip, boolean isPlayerSneaking) {
        tooltip.add("Speed = " + getSpeed());
        tooltip.add("Net = " + network);
        return true;
    }

    @Override
    public boolean addToGoggleTooltip(List<String> tooltip, boolean isPlayerSneaking) {
        tooltip.add("Speed = " + getSpeed());
        tooltip.add("Net = " + network);
        return true;
    }

    public void clearKineticInformation() {
        speed = 0;
        source = null;
        network = null;
        overStressed = false;
        stress = 0;
        capacity = 0;
        lastStressApplied = 0;
        lastCapacityProvided = 0;
    }

    public void warnOfMovement() {
        wasMoved = true;
    }

    public int getFlickerScore() {
        return flickerTally;
    }

    public static float convertToDirection(float axisSpeed, EnumFacing d) {
        return d.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? axisSpeed : -axisSpeed;
    }

    public static float convertToLinear(float speed) {
        return speed / 512f;
    }

    public static float convertToAngular(float speed) {
        return speed * 3 / 10f;
    }

    public boolean isOverStressed() {
        return overStressed;
    }

    public float propagateRotationTo(KineticTileEntity target, IBlockState stateFrom, IBlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        return 0;
    }

    public List<BlockPos> addPropagationLocations(IRotate block, IBlockState state, List<BlockPos> neighbours) {
        if (!canPropagateDiagonally(block, state))
            return neighbours;

        EnumFacing.Axis axis = block.getRotationAxis(state);
        BlockPos.getAllInBox(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
                .forEach(offset -> {
                    if (choose(axis, offset.getX(), offset.getY(), offset.getZ()) != 0)
                        return;
                    if (WorldUtils.distSqr(offset, BlockPos.ORIGIN) != 2)
                        return;
                    neighbours.add(getPos().subtract(offset));
                });
        return neighbours;
    }

    public boolean isCustomConnection(KineticTileEntity other, IBlockState state, IBlockState otherState) {
        return false;
    }

    protected boolean canPropagateDiagonally(IRotate block, IBlockState state) {
        return ICogWheel.isSmallCog(state);
    }

    protected boolean isNoisy() {
        return true;
    }

    public int getRotationAngleOffset(EnumFacing.Axis axis) {
        return 0;
    }

    public float getAxisShift(EnumFacing.Axis axis) {
        return isAxisShifted(this.pos, axis) ? 22.5F : 0.0F;
    }
}
