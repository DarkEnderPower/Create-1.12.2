package darkenderhilda.create.content.kinetics.speedController;

import darkenderhilda.create.AllBlocks;
import darkenderhilda.create.AllPartialModels;
import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.utility.ClientUtils;
import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

public class SpeedControllerTESR extends KineticTESR<SpeedControllerTileEntity> {

    @Override
    protected void renderMe(SpeedControllerTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderShaft(te, x, y, z, partialTicks, WorldUtils.getTEHorizontalFacing(te).getAxis());

        if(!te.hasBracket) {
            return;
        }

        IBlockState state = bracket(WorldUtils.getTEHorizontalFacing(te));

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 1, z + 1);
        ClientUtils.getBlockModelRenderer().renderModelBrightness(ClientUtils.getModelForState(state), state, 1.0F, true);

        GlStateManager.popMatrix();
    }

    private IBlockState bracket(EnumFacing facing) {
        if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
            return AllBlocks.RENDER.getDefaultState().withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SPEED_CONTROLLER_BRACKET_X);
        } else {
            return AllBlocks.RENDER.getDefaultState().withProperty(AllPartialModels.TYPE, AllPartialModels.Type.SPEED_CONTROLLER_BRACKET_Z);
        }
    }
}
