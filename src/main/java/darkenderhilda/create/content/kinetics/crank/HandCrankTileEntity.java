package darkenderhilda.create.content.kinetics.crank;

import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.GeneratingKineticTileEntity;
import darkenderhilda.create.foundation.utility.SuperByteBuffer;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static darkenderhilda.create.foundation.block.BlockData.FACING;

public class HandCrankTileEntity extends GeneratingKineticTileEntity {

    public int inUse;
    public boolean backwards;
    public float independentAngle;
    public float chasingVelocity;


    public void turn(boolean back) {
        boolean update = getGeneratedSpeed() == 0 || back != backwards;

        inUse = 10;
        this.backwards = back;
        if (update && !world.isRemote)
            updateGeneratedRotation();
    }

    public float getIndependentAngle(float partialTicks) {
        return ((independentAngle + partialTicks * chasingVelocity) / 360);
    }

    @Override
    public float getGeneratedSpeed() {
        Block block = WorldUtils.stateFormTE(this).getBlock();
        if (!(block instanceof HandCrankBlock)) {
            return 0;
        }
        HandCrankBlock crank = (HandCrankBlock) block;
        int speed = (inUse == 0 ? 0 : clockwise() ? -1 : 1) * crank.getRotationSpeed();
        return convertToDirection(speed, WorldUtils.stateFormTE(this).getValue(FACING));
    }

    protected boolean clockwise() {
        return backwards;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("InUse", inUse);
        compound.setBoolean("Backwards", backwards);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inUse = compound.getInteger("InUse");
        backwards = compound.getBoolean("Backwards");
        super.readFromNBT(compound);
    }

    @Override
    public void update() {
        super.update();

        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        if (inUse > 0) {
            inUse--;

            if (inUse == 0 && !world.isRemote) {
                //sequenceContext = null;
                updateGeneratedRotation();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public SuperByteBuffer getRenderedHandle() {
        IBlockState state = getBlockState();
        return AllPartialModels.HAND_CRANK_HANDLE.renderOnDirectional(state, state.getValue(FACING).getOpposite());
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldRenderShaft() {
        return true;
    }
}
