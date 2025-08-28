package darkenderhilda.create.content.equipment.goggle;

import darkenderhilda.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class GoggleOverlayRenderer {


    @SubscribeEvent
    public static void lookingAtBlocksThroughGogglesShowsTooltip(RenderGameOverlayEvent.Post event) {
        if(true) return;

        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }

        RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }



        Minecraft mc = Minecraft.getMinecraft();
        WorldClient world = mc.world;
        BlockPos pos = objectMouseOver.getBlockPos();

        TileEntity te = world.getTileEntity(pos);

        boolean goggleInformation = te instanceof IHaveGoggleInformation;

        if(!goggleInformation) return;



        List<String> tooltip = new ArrayList<>();

        IHaveGoggleInformation gte = (IHaveGoggleInformation) te;

        if(!gte.addToGoggleTooltip(tooltip, mc.player.isSneaking())) {
            return;
        }

        GlStateManager.pushMatrix();

        int y = 100;
        for (String s : tooltip) {
            mc.fontRenderer.drawStringWithShadow(s, 10, y, -1);
            y += 20;
        }


        GlStateManager.popMatrix();

    }


}
