package darkenderhilda.create.foundation.utility;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Random;

public class VecHelper {

    public static Vec3d offsetRandomly(Vec3d vec, Random r, float radius) {
        return new Vec3d(vec.x + (r.nextFloat() - .5f) * 2 * radius, vec.y + (r.nextFloat() - .5f) * 2 * radius,
                vec.z + (r.nextFloat() - .5f) * 2 * radius);
    }

    public static Vec3d getCenterOf(Vec3i pos) {
        return new Vec3d(pos).add(.5f, .5f, .5f);
    }
}
