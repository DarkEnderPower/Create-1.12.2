package darkenderhilda.create.api.equipment.goggles;

import java.util.List;

public interface IHaveGoggleInformation extends IHaveCustomOverlayIcon{

    default boolean addToGoggleTooltip(List<String> tooltip, boolean isPlayerSneaking){
        return false;
    }
}
