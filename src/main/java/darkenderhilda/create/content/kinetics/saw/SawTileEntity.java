package darkenderhilda.create.content.kinetics.saw;

import darkenderhilda.create.content.kinetics.base.BlockBreakingKineticTileEntity;
import darkenderhilda.create.foundation.block.BlockData;
import darkenderhilda.create.foundation.utility.VecHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static darkenderhilda.create.foundation.block.BlockData.FACING;

public class SawTileEntity extends BlockBreakingKineticTileEntity {

    protected boolean canProcess() {
        return getWorld().getBlockState(getPos()).getValue(FACING) == EnumFacing.UP;
    }

    @Override
    public void onBlockBroken(IBlockState stateToBreak) {
        super.onBlockBroken(stateToBreak);
        TreeCutter.findTree(world, breakingPos, stateToBreak)
                .destroyBlocks(world, null, this::dropItemFromCutTree);
    }

    public void dropItemFromCutTree(BlockPos pos, ItemStack stack) {
        float distance = (float) Math.sqrt(pos.distanceSq(breakingPos));
        Vec3d dropPos = VecHelper.getCenterOf(pos);
        EntityItem entity = new EntityItem(world, dropPos.x, dropPos.y, dropPos.z, stack);
        Vec3d vec3d = new Vec3d(breakingPos.subtract(this.pos)).scale(distance / 20f);
        entity.setVelocity(vec3d.x, vec3d.y, vec3d.z);
        world.spawnEntity(entity);
    }

    @Override
    protected boolean shouldRun() {
        return getWorld().getBlockState(getPos()).getValue(FACING)
                .getAxis()
                .isHorizontal();
    }

    @Override
    protected BlockPos getBreakingPos() {
        return getPos().offset(getWorld().getBlockState(getPos()).getValue(FACING));
    }

    @Override
    public boolean canBreak(IBlockState stateToBreak, float blockHardness) {
        boolean sawable = isSawable(stateToBreak);
        return super.canBreak(stateToBreak, blockHardness) && sawable;
    }

    public static boolean isSawable(IBlockState stateToBreak) {
        Block blockToBreak = stateToBreak.getBlock();
        if(blockToBreak == Blocks.SAPLING)
            return false;
        if(blockToBreak == Blocks.LOG || blockToBreak == Blocks.LEAVES)
            return true;
        if(blockToBreak == Blocks.CACTUS)
            return true;
        if(blockToBreak == Blocks.REEDS)
            return true;
        if(blockToBreak == Blocks.CHORUS_PLANT)
            return true;
        return false;
    }
}
