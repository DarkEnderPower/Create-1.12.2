package darkenderhilda.create.foundation.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public class BlockData {

    public static final PropertyEnum<EnumFacing.Axis> AXIS = BlockRotatedPillar.AXIS;
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyDirection HORIZONTAL_AXIS = BlockHorizontal.FACING;


    public static final float WOOD_HARDNESS = 2.0F;
    public static final float WOOD_RESISTANCE = 2.0F;

    public static final float STONE_HARDNESS = 1.5F;
    public static final float STONE_RESISTANCE = 6.0F;

    public static final float ORE_HARDNESS = 3.0F;
    public static final float ORE_RESISTANCE = 1.5F;

    public static final float IRON_HARDNESS = 5.0F;
    public static final float IRON_RESISTANCE = 6.0F;
}
