package darkenderhilda.create.foundation.register;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllItems;
import darkenderhilda.create.AllPartialModels;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemAndBlockRegistryEvents {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        for(Item item : AllItems.ITEMS) {
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        for(Block block : AllBlocks.BLOCKS) {
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for(Item item : AllItems.ITEMS) {
            if(item instanceof IHasModel) {
                ((IHasModel) item).registerModel();
            }
        }

        for(Block block : AllBlocks.BLOCKS) {
            if(block instanceof  IHasModel) {
                ((IHasModel) block).registerModel();
            }
        }
    }
}
