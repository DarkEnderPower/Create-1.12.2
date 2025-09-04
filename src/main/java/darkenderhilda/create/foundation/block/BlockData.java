package darkenderhilda.create.foundation.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public class BlockData {

    public static final PropertyEnum<EnumFacing.Axis> AXIS = BlockRotatedPillar.AXIS;
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    //TODO REWRITE?
    public static final PropertyDirection HORIZONTAL_AXIS = BlockHorizontal.FACING;
    public static final PropertyDirection HORIZONTAL_FACING = BlockHorizontal.FACING;
    public static final PropertyBool POWERED = BlockLever.POWERED;
}
