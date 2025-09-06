package darkenderhilda.create.content.kinetics.crank;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.content.kinetics.base.DirectionalKineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static darkenderhilda.create.foundation.block.BlockData.FACING;


public class HandCrankBlock extends DirectionalKineticBlock implements ITE<HandCrankTileEntity> {

    public HandCrankBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(player.isSpectator())
            return false;

        withTileEntityDo(worldIn, pos, te -> te.turn(player.isSneaking()));
        player.addExhaustion((float) (getRotationSpeed() * 0.009999999776482582));//AllConfigs.server().kinetics.crankHungerMultiplier.getF());

        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isRemote)
            return;

        EnumFacing facing = state.getValue(FACING);
        if(worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock() == Blocks.AIR) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, facing);
    }

    public int getRotationSpeed() {
        return 32;
    }

    @Override
    public List<AxisAlignedBB> getShape(IBlockState state) {
        return AllShapes.CRANK.get(state.getValue(FACING));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new HandCrankTileEntity();
    }

    @Override
    public Class<HandCrankTileEntity> getTileEntityClass() {
        return HandCrankTileEntity.class;
    }
}
