package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.Create;
import darkenderhilda.create.api.equipment.goggles.IHaveGoggleInformation;
import darkenderhilda.create.api.equipment.goggles.IHaveHoveringInformation;
import darkenderhilda.create.content.kinetics.KineticNetwork;
import darkenderhilda.create.content.kinetics.RotationPropagator;
import darkenderhilda.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;


import static darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticTESR.isAxisShifted;

public abstract class KineticTileEntity extends SmartTileEntity implements IHaveGoggleInformation, IHaveHoveringInformation {

    public @Nullable Long network;
    public @Nullable BlockPos source;
    public boolean networkDirty;
    public boolean updateSpeed;

    //protected KineticEffectHandler effects;
    protected float speed;
    protected float capacity;
    protected float stress;
    protected boolean overStressed;
    protected boolean wasMoved;

    private int flickerTally;
    private int networkSize;
    private int validationCountdown;
    private float lastStressApplied;
    private float lastCapacityProvided;

    @Override
    public void initialize() {
        if (hasNetwork()) {
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

        if (world.isRemote)
            return;

        if (validationCountdown-- <= 0) {
            validationCountdown = 60;//AllConfigs.SERVER.kinetics.kineticValidationFrequency.get();
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

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        clearKineticInformation();

        // DO NOT READ kinetic information when placed after movement
        if (wasMoved) {
            super.readFromNBT(compound);
            return;
        }

        speed = compound.getFloat("Speed");

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
            overStressed = capacity < stress && true;//StressImpact.isEnabled();
        }

        super.readFromNBT(compound);
    }

    @Override
    public void readClientUpdate(NBTTagCompound tag) {
        boolean overStressedBefore = overStressed;
        super.readClientUpdate(tag);
        //if (overStressedBefore != overStressed && speed != 0)
            //effects.triggerOverStressedEffect();
    }

    private void validateKinetics() {
        if (hasSource()) {
            if (!hasNetwork()) {
                removeSource();
                return;
            }

            if (!world.isBlockLoaded(source))
                return;

            TileEntity tileEntity = world.getTileEntity(source);
            KineticTileEntity sourceTe = tileEntity instanceof KineticTileEntity ? (KineticTileEntity) tileEntity : null;
            if (sourceTe == null || sourceTe.speed == 0) {
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

    public float calculateAddedStressCapacity() {
       // Map<ResourceLocation, ConfigValue<Double>> capacityMap = AllConfigs.SERVER.kinetics.stressValues.capacities;
       // ResourceLocation path = getBlockState().getBlock().getRegistryName();

        //float capacity = capacityMap.containsKey(path) ? capacityMap.get(path).get().floatValue() : 0;
        float capacity = 128;
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    public float calculateStressApplied() {
        //Map<ResourceLocation, ConfigValue<Double>> stressEntries = AllConfigs.SERVER.kinetics.stressValues.impacts;
        //ResourceLocation path = getBlockState().getBlock().getRegistryName();

        //float impact = stressEntries.containsKey(path) ? stressEntries.get(path).get().floatValue() : 1;
        float impact = 1;
        this.lastStressApplied = impact;
        return impact;
    }

    public boolean hasSource() {
        return source != null;
    }

    public void setSource(BlockPos source) {
        this.source = source;
        if (world == null || world.isRemote)
            return;

        TileEntity tileEntity = world.getTileEntity(source);
        if (!(tileEntity instanceof KineticTileEntity)) {
            removeSource();
            return;
        }

        KineticTileEntity sourceTe = (KineticTileEntity) tileEntity;
        setNetwork(sourceTe.network);
    }

    public void removeSource() {
        float prevSpeed = getSpeed();

        speed = 0;
        source = null;
        setNetwork(null);

        onSpeedChanged(prevSpeed);
    }

    public void setNetwork(@Nullable Long networkIn) {
        if (network == networkIn)
            return;
        if (network != null)
            getOrCreateNetwork().remove(this);

        network = networkIn;

        if (networkIn == null)
            return;

        network = networkIn;
        KineticNetwork network = getOrCreateNetwork();
        network.initialized = true;
        network.add(this);
    }

    public void attachKinetics() {
        updateSpeed = false;
        RotationPropagator.handleAdded(world, pos, this);
    }

    public void detachKinetics() {
        RotationPropagator.handleRemoved(world, pos, this);
    }

    public void onSpeedChanged(float previousSpeed) {
        boolean fromOrToZero = (previousSpeed == 0) != (getSpeed() == 0);
        boolean directionSwap = !fromOrToZero && Math.signum(previousSpeed) != Math.signum(getSpeed());
        if (fromOrToZero || directionSwap)
            flickerTally = getFlickerScore() + 5;

//        if (fromOrToZero && previousSpeed == 0 && !world.isRemote)
//            AllTriggers
//                    .getPlayersInRange(world, pos, 4)
//                    .forEach(p -> AllTriggers.KINETIC_BLOCK.trigger(p, getBlockState()));
    }

    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        networkDirty = false;
        this.capacity = maxStress;
        this.stress = currentStress;
        this.networkSize = networkSize;
        boolean overStressed = maxStress < currentStress && true;
                //StressImpact.isEnabled();

        if (overStressed != this.overStressed) {
            //if (speed != 0 && overStressed)
                //AllTriggers.triggerForNearbyPlayers(AllTriggers.OVERSTRESSED, world, pos, 8);
            float prevSpeed = getSpeed();
            this.overStressed = overStressed;
            onSpeedChanged(prevSpeed);
            sendData();
        }
    }

    public boolean needsSpeedUpdate() {
        return updateSpeed;
    }

    public KineticNetwork getOrCreateNetwork() {
        return Create.TORQUE_PROPAGATOR.getOrCreateNetworkFor(this);
    }

    public boolean hasNetwork() {
        return network != null;
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

    public float getAxisShift(EnumFacing.Axis axis) {
        return isAxisShifted(this.pos, axis) ? 22.5F : 0.0F;
    }
}
