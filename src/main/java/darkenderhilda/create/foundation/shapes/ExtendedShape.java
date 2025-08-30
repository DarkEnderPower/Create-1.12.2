package darkenderhilda.create.foundation.shapes;

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
            case DOWN: return mirror(this, EnumFacing.Axis.Y).shapes;
            case NORTH: return mirror(UpToSouth(this), EnumFacing.Axis.Z).shapes;
            case SOUTH: return UpToSouth(this).shapes;
            case WEST: return mirror(UpToEast(this), EnumFacing.Axis.X).shapes;
            case EAST: return UpToEast(this).shapes;
            default: return shapes;
        }
    }

    public static ExtendedShape UpToSouth(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.getShape()) {
            extendedShape.and(ShapeUtils.rotateYtoZ(aabb));
        }
        return extendedShape;
    }

    public static ExtendedShape UpToEast(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.getShape()) {
            extendedShape.and(ShapeUtils.rotateYtoX(aabb));
        }

        return extendedShape;
    }

    public static ExtendedShape mirror(ExtendedShape shape, EnumFacing.Axis axis) {
        ExtendedShape extendedShape = new ExtendedShape();
        if(axis == EnumFacing.Axis.X) {
            for (AxisAlignedBB aabb: shape.shapes) {
                extendedShape.and(ShapeUtils.mirrorX(aabb));
            }
        } else if(axis == EnumFacing.Axis.Y) {
            for (AxisAlignedBB aabb: shape.shapes) {
                extendedShape.and(ShapeUtils.mirrorY(aabb));
            }
        } else {
            for (AxisAlignedBB aabb: shape.shapes) {
                extendedShape.and(ShapeUtils.mirrorZ(aabb));
            }
        }

        return extendedShape;
    }

    public static ExtendedShape rotateForAxis(ExtendedShape shape, EnumFacing.Axis axis) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.getShape()) {
            extendedShape.and(ShapeUtils.rotateToAxis(aabb, axis));
        }

        return extendedShape;
    }
}
