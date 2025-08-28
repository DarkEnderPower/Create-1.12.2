package darkenderhilda.create.api.equipment.goggles;

import java.util.List;

public interface IHaveHoveringInformation extends IHaveCustomOverlayIcon {

    default boolean addToTooltip(List<String> tooltip, boolean isPlayerSneaking) {
        return false;
    }
}
