package darkenderhilda.create.content.kinetics.base;

import darkenderhilda.create.api.kinetics.PlacementHelper;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.BlockWithTE;
import darkenderhilda.create.foundation.block.CreateBlock;
import darkenderhilda.create.foundation.utils.SideUtils;
import darkenderhilda.create.foundation.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class KineticBlock extends BlockWithTE implements IRotate {

    public KineticBlock(BlockProperties properties) {
        super(properties);
    }


    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (worldIn.isRemote)
            return;

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof KineticTileEntity))
            return;

        // Remove previous information when block is added
        KineticTileEntity kte = (KineticTileEntity) tileEntity;
        kte.warnOfMovement();
        kte.clearKineticInformation();
        kte.updateSpeed = true;
    }

    @Override
    public boolean hasIntegratedCogwheel(IBlockAccess world, BlockPos pos, IBlockState state) {
        return false;
    }

    @Override
    public boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face) {
        return false;
    }

    @Override
    public EnumFacing.Axis getRotationAxis(IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }
}
