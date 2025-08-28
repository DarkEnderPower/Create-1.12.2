package darkenderhilda.create.content.kinetics.simpleRelays;

import darkenderhilda.create.AllShapes;
import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.shapes.ExtendedShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ShaftBlock extends AbstractSimpleShaftBlock {

    public ShaftBlock(BlockProperties properties) {
        super(properties);
    }

    @Override
    public ExtendedShape getShape(IBlockState state) {
        return ExtendedShape.rotate(AllShapes.SIX_VOXEL_POLE, state.getValue(AXIS));
    }
}
