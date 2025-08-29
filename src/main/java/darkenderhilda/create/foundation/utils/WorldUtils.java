package darkenderhilda.create.foundation.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.List;

import static darkenderhilda.create.content.kinetics.base.DirectionalKineticBlock.FACING;
import static darkenderhilda.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.VALUES;

public class WorldUtils {

    public static IBlockState stateFormTE(TileEntity te) {
        return te.getWorld().getBlockState(te.getPos());
    }

    public static EnumFacing getTEFacing(TileEntity te) {
        return stateFormTE(te).getValue(FACING);
    }

    public static EnumFacing.Axis getTEAxis(TileEntity te) {
        return stateFormTE(te).getValue(AXIS);
    }

    public static boolean typeOf(Block block, IBlockState state) {
        return block == state.getBlock();
    }

    public static boolean hasTileEntity(IBlockState state) {
        return state.getBlock().hasTileEntity(state);
    }

    public static double distSqr(Vec3i vec, Vec3i vector) {
        return distToLowCornerSqr(vec, vector.getX(), vector.getY(), vector.getZ());
    }

    public static double distToLowCornerSqr(Vec3i vec3i, double x, double y, double z) {
        double d0 = (double)vec3i.getX() - x;
        double d1 = (double)vec3i.getY() - y;
        double d2 = (double)vec3i.getZ() - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public static EnumFacing getNearest(float x, float y, float z) {
        EnumFacing direction = NORTH;
        float f = Float.MIN_VALUE;

        for(EnumFacing direction1 : VALUES) {
            float f1 = x * (float)direction1.getDirectionVec().getX() + y * (float)direction1.getDirectionVec().getY() + z * (float)direction1.getDirectionVec().getZ();
            if (f1 > f) {
                f = f1;
                direction = direction1;
            }
        }

        return direction;
    }

    public static int choose(EnumFacing.Axis axis, int x, int y, int z) {
        if(axis == EnumFacing.Axis.X) {
            return x;
        } else if (axis == EnumFacing.Axis.Y) {
            return y;
        } else {
            return z;
        }
    }
}
