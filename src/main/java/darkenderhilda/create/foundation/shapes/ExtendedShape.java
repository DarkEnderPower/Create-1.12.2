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

    public static ExtendedShape of(AxisAlignedBB aabb) {
        return new ExtendedShape().and(aabb);
    }

    public static ExtendedShape of(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new ExtendedShape().and(x1, y1, z1, x2, y2, z2);
    }

    public static ExtendedShape of(ExtendedShape shape) {
        return new ExtendedShape().and(shape);
    }

    public static ExtendedShape of(List<AxisAlignedBB> aabbs) {
        return new ExtendedShape().and(aabbs);
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
        shapes.addAll(shape.get());
        return this;
    }

    public ExtendedShape and(List<AxisAlignedBB> aabbs) {
        shapes.addAll(aabbs);
        return this;
    }

    public List<AxisAlignedBB> get() {
        return shapes;
    }

    public List<AxisAlignedBB> get(EnumFacing.Axis axis) {
        return rotateAxis(this, axis).shapes;
    }

    public List<AxisAlignedBB> get(EnumFacing facing) {
        return null;
    }

    public static ExtendedShape UpToNorth(ExtendedShape shape) {


        return null;
    }

    /**
     * Initial rotation should be north
     */
    public static ExtendedShape rotateHorizontal(ExtendedShape shape, EnumFacing facing) {
        switch (facing) {
            case NORTH: return shape;
            case EAST:  return rotate90(shape);
            case WEST:  return rotate180(shape);
            case SOUTH: return rotate270(shape);
            default: {
                Create.logger.debug("Can't rotate horizontal shape to " + facing.getName());
                return shape;
            }
        }

    }

    private static ExtendedShape rotate90(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.get()) {
            double newMinX = 0.5 + (aabb.minZ - 0.5);
            double newMaxX = 0.5 + (aabb.maxZ - 0.5);
            double newMinZ = 0.5 - (aabb.maxX - 0.5);
            double newMaxZ = 0.5 - (aabb.minX - 0.5);

            double minX = Math.min(newMinX, newMaxX);
            double maxX = Math.max(newMinX, newMaxX);
            double minZ = Math.min(newMinZ, newMaxZ);
            double maxZ = Math.max(newMinZ, newMaxZ);

            extendedShape.and(createAABB(minX, aabb.minY, minZ, maxX, aabb.maxY, maxZ));
        }

        return extendedShape;
    }

    private static ExtendedShape rotate180(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.get()) {
            double newMinX = 1.0 - aabb.maxX;
            double newMaxX = 1.0 - aabb.minX;
            double newMinZ = 1.0 - aabb.maxZ;
            double newMaxZ = 1.0 - aabb.minZ;

            double minX = Math.min(newMinX, newMaxX);
            double maxX = Math.max(newMinX, newMaxX);
            double minZ = Math.min(newMinZ, newMaxZ);
            double maxZ = Math.max(newMinZ, newMaxZ);

            extendedShape.and(createAABB(minX, aabb.minY, minZ, maxX, aabb.maxY, maxZ));
        }

        return extendedShape;
    }

    private static ExtendedShape rotate270(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.get()) {
            double newMinX = 0.5 - (aabb.maxZ - 0.5);
            double newMaxX = 0.5 - (aabb.minZ - 0.5);
            double newMinZ = 0.5 + (aabb.minX - 0.5);
            double newMaxZ = 0.5 + (aabb.maxX - 0.5);

            double minX = Math.min(newMinX, newMaxX);
            double maxX = Math.max(newMinX, newMaxX);
            double minZ = Math.min(newMinZ, newMaxZ);
            double maxZ = Math.max(newMinZ, newMaxZ);

            extendedShape.and(createAABB(minX, aabb.minY, minZ, maxX, aabb.maxY, maxZ));
        }

        return extendedShape;
    }


    /**
     * Initial axis should be Y
     */
    public static ExtendedShape rotateAxis(ExtendedShape shape, EnumFacing.Axis axis) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.get()) {
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
