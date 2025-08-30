package darkenderhilda.create.foundation.shapes;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class ShapeUtils {

    public static AxisAlignedBB createAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(x1 / (double)16.0F, y1 / (double)16.0F, z1 / (double)16.0F, x2 / (double)16.0F, y2 / (double)16.0F, z2 / (double)16.0F);
    }

    //---Mirroring AABB---
    public static AxisAlignedBB mirrorX(AxisAlignedBB aabb) {
        return new AxisAlignedBB(1 - aabb.maxX, aabb.minY, aabb.minZ, 1 - aabb.minX, aabb.maxY, aabb.maxZ);
    }

    public static AxisAlignedBB mirrorY(AxisAlignedBB aabb) {
        return new AxisAlignedBB(aabb.minX, 1 - aabb.maxY, aabb.minZ, aabb.maxX, 1 - aabb.minY, aabb.maxZ);
    }

    public static AxisAlignedBB mirrorZ(AxisAlignedBB aabb) {
        return new AxisAlignedBB(aabb.minX, aabb.minY, 1 - aabb.maxZ, aabb.maxX, aabb.maxY, 1 - aabb.minZ);
    }
    //--------------------

    //---Axis rotations---
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
    //-------------------

    public static RayTraceResult raytraceMultiAABB(List<AxisAlignedBB> aabbs, BlockPos pos, Vec3d start, Vec3d end) {
        List<RayTraceResult> list = new ArrayList<>();

        for(AxisAlignedBB axisalignedbb : aabbs) {
            list.add(rayTrace2(pos, start, end, axisalignedbb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for(RayTraceResult raytraceresult : list) {
            if(raytraceresult != null) {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if(d0 > d1) {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }

    public static RayTraceResult rayTrace2(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        Vec3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
        Vec3d vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.add(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
    }
}
