package darkenderhilda.create.content.kinetics.gearbox;

import darkenderhilda.create.content.kinetics.base.RotatedPillarKineticBlock;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.ITE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

import static darkenderhilda.create.foundation.block.BlockData.AXIS;

public class GearboxBlock extends RotatedPillarKineticBlock implements ITE<GearboxTileEntity> {

    protected final boolean isVertical;

    protected GearboxBlock(boolean vertical, BlockProperties properties) {
        super(properties);
        isVertical = vertical;
    }

    public static GearboxBlock gearbox(BlockProperties properties) {
        return new GearboxBlock(false, properties);
    }

    public static GearboxBlock gearboxVertical(BlockProperties properties) {
        return new GearboxBlock(true, properties);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return super.getItemDropped(state, rand, fortune);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if(isVertical) {
            EnumFacing.Axis axis;
            if(placer.getHorizontalFacing().getAxis() == EnumFacing.Axis.X) {
                axis = EnumFacing.Axis.Z;
            } else {
                axis = EnumFacing.Axis.X;
            }

            return this.getDefaultState().withProperty(AXIS, axis);
        } else {
            return getDefaultState().withProperty(AXIS, EnumFacing.Axis.Y);
        }
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return face.getAxis() != state.getValue(AXIS);
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return state.getValue(AXIS);
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new GearboxTileEntity();
    }

    @Override
    public Class<GearboxTileEntity> getTileEntityClass() {
        return GearboxTileEntity.class;
    }
}
