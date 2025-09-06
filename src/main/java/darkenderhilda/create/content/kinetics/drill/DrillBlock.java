package darkenderhilda.create.content.kinetics.drill;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.content.kinetics.base.DirectionalKineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.FACING;

public class DrillBlock extends DirectionalKineticBlock implements ITE<DrillTileEntity> {

    public DrillBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing facingToPlace = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        return getDefaultState().withProperty(FACING, placer.isSneaking() ? facingToPlace.getOpposite() : facingToPlace);
    }

    @Nullable
    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos pos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
        if(!(entity instanceof EntityItem)) {
            if(new AxisAlignedBB(pos).shrink(-.1f).intersects(entity.getEntityBoundingBox())) {
                damageEntity(world, pos, entity);
            }
        }

        return super.isEntityInsideMaterial(world, pos, iblockstate, entity, yToTest, materialIn, testingHead);
    }

    //for some reason isEntityInsideMaterial doesn't even being called when
    //entity on top of a block
    //unfortunately this doesn't trigger if entity(player) is sneaking :(
    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if(world.getBlockState(pos).getValue(FACING) != EnumFacing.UP)
            return;
        damageEntity(world, pos, entity);
    }

    private void damageEntity(IBlockAccess world, BlockPos pos, Entity entity) {
        withTileEntityDo(world, pos, te -> {
            if (te.getSpeed() == 0)
                return;
            entity.attackEntityFrom(DamageSource.GENERIC, (float) DrillBlock.getDamage(te.getSpeed()));
        });
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.CASING_12PX.get(state.getValue(FACING));
    }

    public static double getDamage(float speed) {
        float speedAbs = Math.abs(speed);
        double sub1 = Math.min(speedAbs / 16, 2);
        double sub2 = Math.min(speedAbs / 32, 4);
        double sub3 = Math.min(speedAbs / 64, 4);
        return MathHelper.clamp(sub1 + sub2 + sub3, 1, 10);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return super.isPassable(worldIn, pos);
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new DrillTileEntity();
    }

    @Override
    public Class<DrillTileEntity> getTileEntityClass() {
        return DrillTileEntity.class;
    }
}
