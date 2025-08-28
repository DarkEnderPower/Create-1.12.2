package darkenderhilda.create.foundation.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {

    public static IBlockState stateFormTE(TileEntity te) {
        return te.getWorld().getBlockState(te.getPos());
    }

    public static boolean typeOf(Block block, IBlockState state) {
        return block == state.getBlock();
    }

    public static boolean hasTileEntity(IBlockState state) {
        return state.getBlock().hasTileEntity(state);
    }

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

    private static RayTraceResult rayTrace2(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        Vec3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
        Vec3d vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.add(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
    }
}
