package darkenderhilda.create;

import darkenderhilda.create.foundation.shapes.ExtendedShape;

public class AllShapes {

    public static final ExtendedShape
    FULL_BLOCK = shape(0, 0, 0, 16, 16, 16),

    CASING_12PX = shape(0, 0, 0, 16, 12, 16),
    CASING_13PX = shape(0, 0, 0, 16, 13, 16),
    SIX_VOXEL_POLE = shape(5, 0, 5, 11, 16, 11),
    SMALL_GEAR = shape(2, 6, 2, 14, 10, 14)
        .add(SIX_VOXEL_POLE),
    LARGE_GEAR = shape(0, 6, 0, 16, 10, 16)
        .add(SIX_VOXEL_POLE),
    MOTOR_BLOCK = shape(3, 0, 3, 13, 14, 13),
    MILLSTONE = shape(0, 0, 0, 16, 6, 16)
        .add(2, 6, 2, 14, 16, 14),
    CRANK = shape(5, 0, 5, 11, 6, 11)
        .add(1, 3, 1, 15, 8, 15);



    public static ExtendedShape shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return ExtendedShape.of(x1, y1, z1, x2, y2, z2);
    }
}
