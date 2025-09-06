package darkenderhilda.create.content.kinetics.saw;

import darkenderhilda.create.content.kinetics.base.KineticTESR;
import darkenderhilda.create.foundation.block.SafeTileEntityRenderer;

public class SawRenderer extends SafeTileEntityRenderer<SawTileEntity> {

//    @Override
//    protected void renderMe(SawTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//        EnumFacing facing = WorldUtils.getTEFacing(te);
//        if(facing.getAxis() != EnumFacing.Axis.Y) {
//            renderShaftHalf(te, x, y, z, partialTicks, facing.getOpposite());
//        } else {
//            renderShaft(te, x, y, z, partialTicks, WorldUtils.stateFormTE(te).getValue(FLIPPED) ? EnumFacing.Axis.X : EnumFacing.Axis.Z);
//        }
//
//        renderBlade(te, facing, x, y, z);
//    }

    @Override
    protected void renderWithGL(SawTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {

    }

    private void renderShaft() {

    }

//    protected void renderBlade(SawTileEntity te, EnumFacing facing, double x, double y, double z) {
//        float speed = te.getSpeed();
//        IBlockState blade;
//        if(SawBlock.isHorizontal(WorldUtils.stateFormTE(te))) {
//            if (speed > 0) {
//                blade = AllPartialModels.Model.SAW_BLADE_HORIZONTAL_ACTIVE.getState();
//            } else if (speed < 0) {
//                blade = AllPartialModels.Model.SAW_BLADE_HORIZONTAL_REVERSED.getState();
//            } else {
//                blade = AllPartialModels.Model.SAW_BLADE_HORIZONTAL_INACTIVE.getState();
//            }
//
//            GlStateManager.pushMatrix();
//
//            GlStateManager.translate(x, y, z + 1);
//
//            if(facing == EnumFacing.SOUTH) {
//                GlStateManager.rotate(270, 0, 1, 0);
//                GlStateManager.translate(-1, 0,0);
//            } else if(facing == EnumFacing.WEST) {
//                GlStateManager.rotate(180, 0, 1, 0);
//                GlStateManager.translate(-1, 0,1);
//            } else if(facing == EnumFacing.NORTH) {
//                GlStateManager.rotate(90, 0, 1, 0);
//                GlStateManager.translate(0, 0,1);
//            }
//
//            ClientUtils.getBlockModelRenderer().renderModelBrightness(ClientUtils.getModelForState(blade), blade, 1.0F, true);
//
//            GlStateManager.popMatrix();
//
//        } else {
//            if (te.getSpeed() > 0) {
//                blade = AllPartialModels.Model.SAW_BLADE_VERTICAL_ACTIVE.getState();
//            } else if (speed < 0) {
//                blade = AllPartialModels.Model.SAW_BLADE_VERTICAL_REVERSED.getState();
//            } else {
//                blade = AllPartialModels.Model.SAW_BLADE_VERTICAL_INACTIVE.getState();
//            }
//            GlStateManager.pushMatrix();
//            GlStateManager.translate(x, y, z + 1);
//            GlStateManager.rotate(90, 0, 0, 1);
//            GlStateManager.translate(0, -1,0);
//            if(!WorldUtils.stateFormTE(te).getValue(FLIPPED)) {
//                GlStateManager.rotate(90, 1, 0, 0);
//                GlStateManager.translate(0, -1,0);
//            }
//
//            if(facing == EnumFacing.DOWN) {
//                GlStateManager.rotate(180, 0, 0, 1);
//                GlStateManager.translate(-1, -1,0);
//            }
//
//            ClientUtils.getBlockModelRenderer().renderModelBrightness(ClientUtils.getModelForState(blade), blade, 1.0F, true);
//
//            GlStateManager.popMatrix();
//        }
//    }
}
