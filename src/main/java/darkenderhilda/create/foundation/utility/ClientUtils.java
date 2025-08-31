package darkenderhilda.create.foundation.utility;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ClientUtils {

    public static Minecraft getMc() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return getMc().player;
    }

    public static FontRenderer getFontRenderer() {
        return getMc().fontRenderer;
    }

    public static SoundHandler getSoundHandler() {
        return getMc().getSoundHandler();
    }

    public static BlockRendererDispatcher getBlockRenderDispatcher() {
        return getMc().getBlockRendererDispatcher();
    }

    public static BlockModelRenderer getBlockModelRenderer() {
        return getBlockRenderDispatcher().getBlockModelRenderer();
    }

    public static IBakedModel getModelForState(IBlockState state) {
        return getBlockRenderDispatcher().getModelForState(state);
    }

    public static float getPartialTicks() {
        return getMc().getRenderPartialTicks();
    }

    public static BlockPos getRayTraceBlockPos() {
        RayTraceResult mouseOver = getPlayer().rayTrace(Minecraft.getMinecraft().playerController.getBlockReachDistance(), ClientUtils.getPartialTicks());
        if(mouseOver != null) {
            if (mouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                return mouseOver.getBlockPos();
            }
        }

        return null;
    }
}
