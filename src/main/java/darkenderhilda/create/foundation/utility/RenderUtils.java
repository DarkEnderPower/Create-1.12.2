package darkenderhilda.create.foundation.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderUtils {

    public static void setupBlockBreakingRender(int destroyStage, ResourceLocation[] DESTROY_STAGES) {
        TextureManager tm = Minecraft.getMinecraft().getRenderManager().renderEngine;
        if (destroyStage >= 0) {
            tm.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(30.0F, 30.0F, 30.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else
            tm.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    public static void finishBlockBreakingRender(int destroyStage) {
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
