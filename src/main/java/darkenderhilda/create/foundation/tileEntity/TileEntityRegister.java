package darkenderhilda.create.foundation.tileEntity;

import darkenderhilda.create.AllTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityRegister {

    private final Class<? extends TileEntity> te;
    private final String registry;
    private TileEntitySpecialRenderer<?> tesr;

    public TileEntityRegister(Class<? extends TileEntity> te, String registry) {
        this.te = te;
        this.registry = registry;
    }

    public TileEntityRegister renderer(TileEntitySpecialRenderer<?> tesr) {
        this.tesr = tesr;
        return this;
    }

    public void register() {
        registerTE(te, registry, tesr);
    }

    private static void registerTE(Class<? extends TileEntity> te, String registry, @Nullable TileEntitySpecialRenderer<?> renderer) {
        GameRegistry.registerTileEntity(te, new ResourceLocation("create", registry));
        if (renderer != null) TileEntityRendererDispatcher.instance.renderers.put(te, renderer);
    }
}
