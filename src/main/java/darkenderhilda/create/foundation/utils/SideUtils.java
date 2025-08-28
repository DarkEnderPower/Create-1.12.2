package darkenderhilda.create.foundation.utils;

public class SideUtils {

    public static boolean isClientSide() {
        return ClientUtils.getMc().world.isRemote;
    }
}
