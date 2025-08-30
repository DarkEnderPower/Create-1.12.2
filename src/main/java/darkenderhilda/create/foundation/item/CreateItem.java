package darkenderhilda.create.foundation.item;

import darkenderhilda.create.AllCreativeModeTabs;
import darkenderhilda.create.Create;
import darkenderhilda.create.AllItems;
import darkenderhilda.create.foundation.register.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class CreateItem extends Item implements IHasModel {

    public CreateItem(String name) {
        super();
        setRegistryName(Create.ID, name);
        setTranslationKey(this.getRegistryName().toString());
        if(registerInCreative())
            setCreativeTab(getTab());

        AllItems.ITEMS.add(this);
    }

//    public CreateItem(ItemProperties itemProperties) {
//
//    }

    protected CreativeTabs getTab() {
        return AllCreativeModeTabs.CREATE_TAB;
    }

    protected boolean registerInCreative() {
        return true;
    }

    @Override
    public void registerModel() {
        Create.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
