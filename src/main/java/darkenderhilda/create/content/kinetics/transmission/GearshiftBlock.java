package darkenderhilda.create.content.kinetics.transmission;

import darkenderhilda.create.content.kinetics.RotationPropagator;
import darkenderhilda.create.content.kinetics.base.AbstractEncasedShaftBlock;
import darkenderhilda.create.content.kinetics.base.KineticTileEntity;
import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;
import static darkenderhilda.create.foundation.block.BlockData.POWERED;

public class GearshiftBlock extends AbstractEncasedShaftBlock {

    public GearshiftBlock(BlockProperties properties) {
        super(properties);
        setDefaultState(blockState.getBaseState().withProperty(POWERED, Boolean.FALSE));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isRemote)
            return;

        boolean previouslyPowered = state.getValue(POWERED);

        if (previouslyPowered != worldIn.isBlockPowered(pos)) {
            detachKinetics(worldIn, pos, true);
            worldIn.setBlockState(pos, state.cycleProperty(POWERED), 2);
        }
    }

    public void detachKinetics(World worldIn, BlockPos pos, boolean reAttachNextTick) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof KineticTileEntity))
            return;
        RotationPropagator.handleRemoved(worldIn, pos, (KineticTileEntity) te);

        // Re-attach next tick
        if (reAttachNextTick)
            worldIn.scheduleBlockUpdate(pos, this, 1, 1);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, POWERED);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(AXIS, facing.getAxis());//.withProperty(POWERED, false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int axisIndex = meta & 0x3;
        int poweredBit = (meta >> 2) & 0x1;
        
        axisIndex = Math.min(axisIndex, 2);
        EnumFacing.Axis axis = EnumFacing.Axis.values()[axisIndex];

        return this.getDefaultState()
                .withProperty(AXIS, axis)
                .withProperty(POWERED, poweredBit == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(AXIS).ordinal() & 0x3) | ((state.getValue(POWERED) ? 1 : 0) << 2);
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
}
