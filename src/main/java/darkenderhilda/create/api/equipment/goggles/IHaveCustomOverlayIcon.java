package darkenderhilda.create.api.equipment.goggles;

import darkenderhilda.create.AllItems;
import net.minecraft.item.ItemStack;

public interface IHaveCustomOverlayIcon {

    default ItemStack getIcon(boolean isPlayerSneaking) {
        return new ItemStack(AllItems.WRENCH);
    }
}
