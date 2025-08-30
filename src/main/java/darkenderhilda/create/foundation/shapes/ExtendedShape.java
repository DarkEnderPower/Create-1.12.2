package darkenderhilda.create.foundation.shapes;

import darkenderhilda.create.Create;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

import static darkenderhilda.create.foundation.shapes.ShapeUtils.createAABB;

public class ExtendedShape {

    private final List<AxisAlignedBB> shapes = new ArrayList<>();

    private ExtendedShape() {
    }

    public static ExtendedShape of(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new ExtendedShape().and(x1, y1, z1, x2, y2, z2);
    }

    public ExtendedShape and(AxisAlignedBB aabb) {
        shapes.add(aabb);
        return this;
    }

    public ExtendedShape and(double x1, double y1, double z1, double x2, double y2, double z2) {
        shapes.add(createAABB(x1, y1, z1, x2, y2, z2));
        return this;
    }

    public ExtendedShape and(ExtendedShape shape) {
        shapes.addAll(shape.getShape());
        return this;
    }

    public ExtendedShape and(List<AxisAlignedBB> aabbs) {
        shapes.addAll(aabbs);
        return this;
    }

    public List<AxisAlignedBB> getShape() {
        return shapes;
    }

    public List<AxisAlignedBB> getShape(EnumFacing.Axis axis) {
        return rotateForAxis(this, axis).shapes;
    }

    public List<AxisAlignedBB> getShape(EnumFacing facing) {
        switch (facing) {
            case DOWN: return shapes;
            case NORTH: return UpToNorth(this).shapes;

            case SOUTH: return UpToNorth(this).shapes;


            case WEST: return UpToNorth(this).shapes;



            case EAST: return UpToWest(this).shapes;
            default: return shapes;
        }
    }

    public static ExtendedShape UpToNorth(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.getShape()) {
            extendedShape.and(rotateYtoZ(aabb));
        }
        return extendedShape;
    }

    public static ExtendedShape UpToWest(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.getShape()) {
            extendedShape.and(rotateYtoX(aabb));
        }
        return extendedShape;
    }

    public static ExtendedShape shapeRotCCW90(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();

        for(AxisAlignedBB aabb : shape.shapes) {
            double new_minX = 16 - aabb.maxZ;
            double new_minZ = aabb.minX;
            double new_maxX = 16 - aabb.minZ;
            double new_maxZ = aabb.maxX;

            extendedShape.and(createAABB(new_minX, aabb.minY, new_minZ, new_maxX, aabb.maxY, new_maxZ));
        }

        return extendedShape;
    }


    /**
     * Initial axis should be Y
     */
    public static ExtendedShape rotateForAxis(ExtendedShape shape, EnumFacing.Axis axis) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.getShape()) {
            extendedShape.and(rotateToAxis(aabb, axis));
        }

        return extendedShape;
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
