package darkenderhilda.create.foundation.shapes;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

import static darkenderhilda.create.foundation.shapes.ShapeUtils.createAABB;

public class ExtendedShape {

    private final List<AxisAlignedBB> shapes = new ArrayList<>();

    private Type type = Type.STATIC;

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
        shapes.addAll(shape.getShapes());
        return this;
    }

    public ExtendedShape and(List<AxisAlignedBB> aabbs) {
        shapes.addAll(aabbs);
        return this;
    }

    /**
     * Allows rotating shape for axes : X, Y, Z
     */
    public ExtendedShape forAxis() {
        type = Type.AXIS;
        return this;
    }

    /**
     * Allows rotating shape for sides : WEST, EAST, NORTH, SOUTH
     */
    public ExtendedShape forSide() {
        type = Type.SIDE;
        return this;
    }

    /**
     * Allows rotating shape for directions : UP, DOWN, WEST, EAST, NORTH, SOUTH
     */
    public ExtendedShape forDirection() {
        type = Type.DIRECTIONAL;
        return this;
    }

    public List<AxisAlignedBB> get(EnumFacing.Axis axis) {
        return rotate(this, axis).shapes;
    }

    public List<AxisAlignedBB> getShapes() {
        return shapes;
    }

    public static ExtendedShape rotate(ExtendedShape shape, EnumFacing.Axis axis) {
        ExtendedShape extendedShape = new ExtendedShape();
        for(AxisAlignedBB aabb : shape.getShapes()) {
            extendedShape.and(ShapeUtils.rotateToAxis(aabb, axis));
        }

        return extendedShape;
    }

    public static ExtendedShape rotate(ExtendedShape shape, EnumFacing facing) {
        ExtendedShape extendedShape = new ExtendedShape();


        return extendedShape;
    }

    /**
     * Works only for Directional and Side types
     */
    public static ExtendedShape mirror(ExtendedShape shape, EnumFacing initialFacing) {
        ExtendedShape extendedShape = new ExtendedShape();


        return extendedShape;
    }

    private enum Type {
        /**
         * Cannot be rotated
         */
        STATIC,
        /**
         * X, Y, Z </br>
         * Initial axis Y
         */
        AXIS,
        /**
         * WEST, EAST, NORTH, SOUTH </br>
         * Initial side NORTH
         */
        SIDE,
        /**
         * UP, DOWN, WEST, EAST, NORTH, SOUTH </br>
         * Initial direction UP
         */
        DIRECTIONAL
    }
}
