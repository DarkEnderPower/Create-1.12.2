package darkenderhilda.create.foundation.tileEntity;

import darkenderhilda.create.foundation.utils.SideUtils;
import net.minecraft.util.ITickable;

public class SmartBlockEntityTicker
        //implements ITickable
{

    //@Override
    public void update() {
        if(SideUtils.isClientSide())
            updateOnClient();
        else
            updateOnServer();
    }

    private void updateOnClient() {

    }

    private void updateOnServer() {

    }
}
