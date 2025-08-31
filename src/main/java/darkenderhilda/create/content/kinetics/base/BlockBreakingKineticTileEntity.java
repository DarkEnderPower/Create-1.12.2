package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.foundation.utility.VecHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BlockBreakingKineticTileEntity extends KineticTileEntity {

    public static final AtomicInteger NEXT_BREAKER_ID = new AtomicInteger();
    protected int ticksUntilNextProgress;
    protected int destroyProgress;
    protected int breakerId = -NEXT_BREAKER_ID.incrementAndGet();
    protected BlockPos breakingPos;

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (destroyProgress == -1)
            destroyNextTick();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (ticksUntilNextProgress == -1)
            destroyNextTick();
    }

    public void destroyNextTick() {
        ticksUntilNextProgress = 1;
    }

    protected abstract BlockPos getBreakingPos();

    protected boolean shouldRun() {
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("Progress", destroyProgress);
        compound.setInteger("NextTick", ticksUntilNextProgress);
        if (breakingPos != null)
            compound.setTag("Breaking", NBTUtil.createPosTag(breakingPos));
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        destroyProgress = compound.getInteger("Progress");
        ticksUntilNextProgress = compound.getInteger("NextTick");
        if (compound.hasKey("Breaking"))
            breakingPos = NBTUtil.getPosFromTag((NBTTagCompound) compound.getTag("Breaking"));
        super.readFromNBT(compound);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!world.isRemote && destroyProgress != 0)
            world.sendBlockBreakProgress(breakerId, breakingPos, -1);
    }

    @Override
    public void update() {
        super.update();

        if (world.isRemote)
            return;
        if (!shouldRun())
            return;
        if (getSpeed() == 0)
            return;

        breakingPos = getBreakingPos();

        if (ticksUntilNextProgress < 0)
            return;
        if (ticksUntilNextProgress-- > 0)
            return;

        IBlockState stateToBreak = world.getBlockState(breakingPos);
        float blockHardness = stateToBreak.getBlockHardness(world, breakingPos);

        if (!canBreak(stateToBreak, blockHardness)) {
            if (destroyProgress != 0) {
                destroyProgress = 0;
                world.sendBlockBreakProgress(breakerId, breakingPos, -1);
            }
            return;
        }

        float breakSpeed = getBreakSpeed();
        destroyProgress += MathHelper.clamp((int) (breakSpeed / blockHardness), 1, 10 - destroyProgress);
        world.playSound(null, getPos(), stateToBreak.getBlock().getSoundType()
                .getHitSound(), SoundCategory.BLOCKS, .25f, 1);

        if (destroyProgress >= 10) {
            onBlockBroken(stateToBreak);
            destroyProgress = 0;
            ticksUntilNextProgress = -1;
            world.sendBlockBreakProgress(breakerId, breakingPos, -1);
            return;
        }

        ticksUntilNextProgress = (int) (blockHardness / breakSpeed);
        world.sendBlockBreakProgress(breakerId, breakingPos, destroyProgress);
    }

    public boolean canBreak(IBlockState stateToBreak, float blockHardness) {
        return isBreakable(stateToBreak, blockHardness);
    }

    public static boolean isBreakable(IBlockState stateToBreak, float blockHardness) {
        return !(stateToBreak.getMaterial().isLiquid() || stateToBreak.getBlock() == Blocks.AIR || blockHardness == -1);
    }

    public void onBlockBroken(IBlockState stateToBreak) {
        world.playEvent(2001, breakingPos, Block.getStateId(stateToBreak));
        Vec3d vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(breakingPos), world.rand, .125f);

        NonNullList<ItemStack> drops = NonNullList.create();
        stateToBreak.getBlock().getDrops(drops, world, breakingPos, stateToBreak, 0);
        drops.forEach((stack) -> {
            if (!stack.isEmpty() && !world.restoringBlockSnapshots) {
                EntityItem entityItem = new EntityItem(world, vec.x, vec.y, vec.z, stack);
                entityItem.setDefaultPickupDelay();
                entityItem.setVelocity(0, 0, 0);
                world.spawnEntity(entityItem);
            }
        });

        world.setBlockState(breakingPos, Blocks.AIR.getDefaultState(), 3);
    }

    protected float getBreakSpeed() {
        return Math.abs(getSpeed() / 100f);
    }
}
