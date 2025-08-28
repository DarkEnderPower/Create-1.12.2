package darkenderhilda.create.content.kinetics;

import darkenderhilda.create.content.kinetics.base.KineticTileEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KineticNetwork {


    public Long id;
    public boolean initialized;
    public Map<KineticTileEntity, Float> sources;
    public Map<KineticTileEntity, Float> members;

    private float currentCapacity;
    private float currentStress;
    private float unloadedCapacity;
    private float unloadedStress;
    private int unloadedMembers;

    public KineticNetwork() {
        sources = new HashMap<>();
        members = new HashMap<>();
    }

    public void initFromTE(float maxStress, float currentStress, int members) {
        unloadedCapacity = maxStress;
        unloadedStress = currentStress;
        unloadedMembers = members;
        initialized = true;
        updateStress();
        updateCapacity();
    }

    public void addSilently(KineticTileEntity be, float lastCapacity, float lastStress) {
        if (members.containsKey(be))
            return;
        if (be.isSource()) {
            unloadedCapacity -= lastCapacity * getStressMultiplierForSpeed(be.getGeneratedSpeed());
            float addedStressCapacity = be.calculateAddedStressCapacity();
            sources.put(be, addedStressCapacity);
        }

        unloadedStress -= lastStress * getStressMultiplierForSpeed(be.getTheoreticalSpeed());
        float stressApplied = be.calculateStressApplied();
        members.put(be, stressApplied);

        unloadedMembers--;
        if (unloadedMembers < 0)
            unloadedMembers = 0;
        if (unloadedCapacity < 0)
            unloadedCapacity = 0;
        if (unloadedStress < 0)
            unloadedStress = 0;
    }

    public void add(KineticTileEntity be) {
        if (members.containsKey(be))
            return;
        if (be.isSource())
            sources.put(be, be.calculateAddedStressCapacity());
        members.put(be, be.calculateStressApplied());
        updateFromNetwork(be);
        be.networkDirty = true;
    }

    public void updateCapacityFor(KineticTileEntity be, float capacity) {
        sources.put(be, capacity);
        updateCapacity();
    }

    public void updateStressFor(KineticTileEntity be, float stress) {
        members.put(be, stress);
        updateStress();
    }

    public void remove(KineticTileEntity be) {
        if (!members.containsKey(be))
            return;
        if (be.isSource())
            sources.remove(be);
        members.remove(be);
        be.updateFromNetwork(0, 0, 0);

        if (members.isEmpty()) {
            TorquePropagator.networks.get(be.getWorld())
                    .remove(this.id);
            return;
        }

        members.keySet()
                .stream()
                .findFirst()
                .map(member -> member.networkDirty = true);
    }

    public void sync() {
        for (KineticTileEntity be : members.keySet())
            updateFromNetwork(be);
    }

    private void updateFromNetwork(KineticTileEntity be) {
        be.updateFromNetwork(currentCapacity, currentStress, getSize());
    }

    public void updateCapacity() {
        float newMaxStress = calculateCapacity();
        if (currentCapacity != newMaxStress) {
            currentCapacity = newMaxStress;
            sync();
        }
    }

    public void updateStress() {
        float newStress = calculateStress();
        if (currentStress != newStress) {
            currentStress = newStress;
            sync();
        }
    }

    public void updateNetwork() {
        float newStress = calculateStress();
        float newMaxStress = calculateCapacity();
        if (currentStress != newStress || currentCapacity != newMaxStress) {
            currentStress = newStress;
            currentCapacity = newMaxStress;
            sync();
        }
    }

    public float calculateCapacity() {
        float presentCapacity = 0;
        for (Iterator<KineticTileEntity> iterator = sources.keySet()
                .iterator(); iterator.hasNext();) {
            KineticTileEntity be = iterator.next();
            if (be.getWorld().getTileEntity(be.getPos()) != be) {
                iterator.remove();
                continue;
            }
            presentCapacity += getActualCapacityOf(be);
        }
        float newMaxStress = presentCapacity + unloadedCapacity;
        return newMaxStress;
    }

    public float calculateStress() {
        float presentStress = 0;
        for (Iterator<KineticTileEntity> iterator = members.keySet()
                .iterator(); iterator.hasNext();) {
            KineticTileEntity be = iterator.next();
            if (be.getWorld().getTileEntity(be.getPos()) != be) {
                iterator.remove();
                continue;
            }
            presentStress += getActualStressOf(be);
        }
        float newStress = presentStress + unloadedStress;
        return newStress;
    }

    public float getActualCapacityOf(KineticTileEntity be) {
        return sources.get(be) * getStressMultiplierForSpeed(be.getGeneratedSpeed());
    }

    public float getActualStressOf(KineticTileEntity be) {
        return members.get(be) * getStressMultiplierForSpeed(be.getTheoreticalSpeed());
    }

    private static float getStressMultiplierForSpeed(float speed) {
        return Math.abs(speed);
    }

    public int getSize() {
        return unloadedMembers + members.size();
    }

}
