package darkenderhilda.create.content.kinetics.saw;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.content.kinetics.base.DirectionalAxisKineticBlock;
import darkenderhilda.create.content.kinetics.drill.DrillBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.FACING;

public class SawBlock extends DirectionalAxisKineticBlock implements ITE<SawTileEntity> {

    public static final PropertyBool FLIPPED = PropertyBool.create("flipped");

    public SawBlock(BlockProperties properties) {
        super(properties);
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
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FLIPPED, FACING);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing facingToPlace = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        return getDefaultState()
                .withProperty(FACING, placer.isSneaking() ? facingToPlace.getOpposite() : facingToPlace)
                .withProperty(FLIPPED, facingToPlace.getAxis() == EnumFacing.Axis.Y && placer.getHorizontalFacing().getAxis() == EnumFacing.Axis.X);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean flipped = (meta & 1) != 0;
        EnumFacing facing = EnumFacing.byIndex(meta >> 1);
        return this.getDefaultState()
                .withProperty(FLIPPED, flipped)
                .withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FLIPPED) ? 1 : 0;
        meta |= state.getValue(FACING).getIndex() << 1;
        return meta;
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.CASING_12PX.get(state.getValue(FACING));
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return isHorizontal(state) ? state.getValue(FACING).getAxis() : state.getValue(FLIPPED) ? EnumFacing.Axis.X : EnumFacing.Axis.Z;
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return isHorizontal(state) ? face == state.getValue(FACING)
                .getOpposite() : super.hasShaftTowards(world, pos, state, face);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    public static boolean isHorizontal(IBlockState state) {
        return state.getValue(FACING)
                .getAxis()
                .isHorizontal();
    }

    @Override
    public Class<SawTileEntity> getTileEntityClass() {
        return SawTileEntity.class;
    }
}
