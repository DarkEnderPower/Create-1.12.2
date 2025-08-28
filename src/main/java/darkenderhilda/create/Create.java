package darkenderhilda.create;

import darkenderhilda.create.content.kinetics.TorquePropagator;
import darkenderhilda.create.foundation.data.CreateRegistrate;
import darkenderhilda.create.foundation.utils.CreateMixinEarly;
import darkenderhilda.create.foundation.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Create.ID, name = Create.NAME, version = Create.VERSION)
public class Create {

    public static final String ID = "create";
    public static final String NAME = "Create";
    public static final String VERSION = "0.1";

    @Mod.Instance
    public static Create instance;

    public static final CreateRegistrate REGISTER = new CreateRegistrate();

    public static final Logger logger = LogManager.getLogger();

    public static final TorquePropagator TORQUE_PROPAGATOR = new TorquePropagator();

    @SidedProxy(
            serverSide = "darkenderhilda.create.foundation.proxy.CommonProxy",
            clientSide = "darkenderhilda.create.foundation.proxy.ClientProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        proxy.preInit(event);
        MinecraftForge.EVENT_BUS.register(new CreateMixinEarly());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static ResourceLocation create(String path) {
        return new ResourceLocation(Create.ID, path);
    }
}
