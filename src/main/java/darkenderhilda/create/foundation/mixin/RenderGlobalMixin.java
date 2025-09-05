package darkenderhilda.create.foundation.mixin;

import darkenderhilda.create.foundation.shapes.IHasExtendedShape;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {

    @Shadow
    private WorldClient world;

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    private void drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks, CallbackInfo ci) {
        if (execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            IBlockState state = world.getBlockState(blockpos);
            Block block = state.getBlock();
            if(block instanceof IHasExtendedShape) {
                GlStateManager.pushMatrix();

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.glLineWidth(2.0F);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);

                if (state.getMaterial() != Material.AIR && this.world.getWorldBorder().contains(blockpos)) {
                    double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
                    double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
                    double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;

                    GlStateManager.translate(blockpos.getX(), blockpos. getY(), blockpos.getZ());
                    for(AxisAlignedBB aabb : ((IHasExtendedShape) block).getShape(state)) {
                        drawSelectionBoundingBox(aabb.grow(0.0020000000949949026D).offset(-d3, -d4, -d5), 0.0F, 0.0F, 0.0F, 0.4F);
                    }
                }

                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();

                GlStateManager.popMatrix();
                ci.cancel();
            }
        }
    }

    @Shadow
    public static void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
    }
}
