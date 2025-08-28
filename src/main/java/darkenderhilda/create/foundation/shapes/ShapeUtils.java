package darkenderhilda.create.foundation.shapes;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class ShapeUtils {

    public static AxisAlignedBB createAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(x1 / (double)16.0F, y1 / (double)16.0F, z1 / (double)16.0F, x2 / (double)16.0F, y2 / (double)16.0F, z2 / (double)16.0F);
    }

    public static AxisAlignedBB rotateYtoX(AxisAlignedBB aabb) {
        return new AxisAlignedBB(aabb.minY, aabb.minX, aabb.minZ, aabb.maxY, aabb.maxX, aabb.maxZ);
    }

    public static AxisAlignedBB rotateYtoZ(AxisAlignedBB aabb) {
        return new AxisAlignedBB(aabb.minX, aabb.minZ, aabb.minY, aabb.maxX, aabb.maxZ, aabb.maxY);
    }

    public static AxisAlignedBB rotateToAxis(AxisAlignedBB aabb, EnumFacing.Axis axis) {
        if(axis == EnumFacing.Axis.X) {
            return rotateYtoX(aabb);
        } else if(axis == EnumFacing.Axis.Z){
            return rotateYtoZ(aabb);
        } else {
            return aabb;
        }
    }
}
