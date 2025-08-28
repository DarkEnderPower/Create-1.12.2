package darkenderhilda.create;

import darkenderhilda.create.foundation.shapes.ExtendedShape;

public class AllShapes {


    public static final ExtendedShape
            FULL_BLOCK = ExtendedShape.of(0, 0, 0, 16, 16, 16),
            CASING_13PX = ExtendedShape.of(0, 0, 0, 16, 13, 16),
            SIX_VOXEL_POLE = ExtendedShape.of(5, 0, 5, 11, 16, 11).forAxis(),
            SMALL_GEAR = ExtendedShape.of(2, 6, 2, 14, 10, 14).and(SIX_VOXEL_POLE).forAxis(),
            LARGE_GEAR = ExtendedShape.of(0, 6, 0, 16, 10, 16).and(SIX_VOXEL_POLE).forAxis();
}
