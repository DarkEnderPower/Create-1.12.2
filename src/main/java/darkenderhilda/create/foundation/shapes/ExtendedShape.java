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
        return new ExtendedShape().add(x1, y1, z1, x2, y2, z2);
    }

    public ExtendedShape add(AxisAlignedBB aabb) {
        shapes.add(aabb);
        return this;
    }

    public ExtendedShape add(double x1, double y1, double z1, double x2, double y2, double z2) {
        shapes.add(createAABB(x1, y1, z1, x2, y2, z2));
        return this;
    }

    public ExtendedShape add(ExtendedShape shape) {
        shapes.addAll(shape.get());
        return this;
    }

    public ExtendedShape add(List<AxisAlignedBB> aabbs) {
        shapes.addAll(aabbs);
        return this;
    }

    public List<AxisAlignedBB> get() {
        return shapes;
    }

    public List<AxisAlignedBB> get(EnumFacing.Axis axis) {
        return rotateForAxis(this, axis).shapes;
    }

    public List<AxisAlignedBB> get(EnumFacing facing) {
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
        for(AxisAlignedBB aabb : shape.get()) {
            extendedShape.add(ShapeUtils.rotateYtoZ(aabb));
        }
        return extendedShape;
    }

    public static ExtendedShape UpToEast(ExtendedShape shape) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.get()) {
            extendedShape.add(ShapeUtils.rotateYtoX(aabb));
        }

        return extendedShape;
    }

    public static ExtendedShape mirror(ExtendedShape shape, EnumFacing.Axis axis) {
        ExtendedShape extendedShape = new ExtendedShape();
        if(axis == EnumFacing.Axis.X) {
            for (AxisAlignedBB aabb: shape.shapes) {
                extendedShape.add(ShapeUtils.mirrorX(aabb));
            }
        } else if(axis == EnumFacing.Axis.Y) {
            for (AxisAlignedBB aabb: shape.shapes) {
                extendedShape.add(ShapeUtils.mirrorY(aabb));
            }
        } else {
            for (AxisAlignedBB aabb: shape.shapes) {
                extendedShape.add(ShapeUtils.mirrorZ(aabb));
            }
        }

        return extendedShape;
    }

    public static ExtendedShape rotateForAxis(ExtendedShape shape, EnumFacing.Axis axis) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.get()) {
            extendedShape.add(ShapeUtils.rotateToAxis(aabb, axis));
        }

        return extendedShape;
    }
}
