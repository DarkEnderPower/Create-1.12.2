package darkenderhilda.create.foundation.utility;

public class SideUtils {

    public static boolean isClientSide() {
        return ClientUtils.getMc().world.isRemote;
    }
}
