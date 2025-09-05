package darkenderhilda.create;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.CreateBlock;
import darkenderhilda.create.foundation.utility.AngleHelper;
import darkenderhilda.create.foundation.utility.ClientUtils;
import darkenderhilda.create.test.SuperByteBuffer;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import java.util.HashMap;
import java.util.Map;

import static darkenderhilda.create.foundation.block.BlockData.FACING;
import static darkenderhilda.create.foundation.block.BlockData.HORIZONTAL_FACING;

public class AllPartialModels extends CreateBlock {

    public AllPartialModels(BlockProperties properties) {
        super(properties);
    }

    public enum Model implements IStringSerializable {

        SHAFT_HALF("shaft_half"),
        DRILL_HEAD("drill_head"),

        SAW_BLADE_HORIZONTAL_ACTIVE("blade_horizontal_active"),
        SAW_BLADE_HORIZONTAL_INACTIVE("blade_horizontal_inactive"),
        SAW_BLADE_HORIZONTAL_REVERSED("blade_horizontal_reversed"),
        SAW_BLADE_VERTICAL_ACTIVE("blade_vertical_active"),
        SAW_BLADE_VERTICAL_INACTIVE("blade_vertical_inactive"),
        SAW_BLADE_VERTICAL_REVERSED("blade_vertical_reversed"),

        MILLSTONE_COG("millstone_cog"),

        SPEED_CONTROLLER_BRACKET_X("speed_controller_bracket_x"),
        SPEED_CONTROLLER_BRACKET_Z("speed_controller_bracket_z");

        private final String name;

        Model(String name) {
            this.name = name;
          }

        @Override
        public String getName() {
            return name;
        }

        public IBlockState getState() {
            return getStaticModel(name);
        }

        public IBakedModel get() {
            return ClientUtils.getModelForState(getState());
        }

        public SuperByteBuffer renderOn(IBlockState referenceState) {
            return CreateClient.bufferCache.renderPartial(this, referenceState);
        }

        public SuperByteBuffer renderOnDirectional(IBlockState referenceState) {
            EnumFacing facing = referenceState.getValue(FACING);
            return renderOnDirectional(referenceState, facing);
        }

        public SuperByteBuffer renderOnHorizontal(IBlockState referenceState) {
            EnumFacing facing = referenceState.getValue(HORIZONTAL_FACING);
            return renderOnDirectional(referenceState, facing);
        }

        public SuperByteBuffer renderOnDirectional(IBlockState referenceState, EnumFacing facing) {
            SuperByteBuffer renderPartial = CreateClient.bufferCache.renderPartial(this, referenceState);
            renderPartial.rotateCentered(EnumFacing.Axis.X, AngleHelper.rad(AngleHelper.verticalAngle(facing)));
            renderPartial.rotateCentered(EnumFacing.Axis.Y, AngleHelper.rad(AngleHelper.horizontalAngle(facing)));
            return renderPartial;
        }
    }

    public static IBlockState shaftHalf(EnumFacing facing) {
        return getFacingModel("shaft_half_", facing);
    }

    public static IBlockState drillHead(EnumFacing facing) {
        return getFacingModel("drill_head_", facing);
    }

    public static IBlockState speedControllerBracket(EnumFacing.AxisDirection direction) {
        return getDirectionalModel("speed_controller_bracket_", direction);
    }

    public static IBlockState getStaticModel(String name) {
        return AllBlocks.RENDER.getDefaultState().withProperty(AllPartialModels.TYPE, models.get(name));
    }

    public static IBlockState getDirectionalModel(String name, EnumFacing.AxisDirection direction) {
        String s;
        if(direction == EnumFacing.AxisDirection.POSITIVE) {
            s = "x";
        } else {
            s = "z";
        }

        return getStaticModel(name + s);
    }

    public static IBlockState getAxisModel(String name, EnumFacing.Axis axis) {
        return getStaticModel(name + axis.toString());
    }

    public static IBlockState getFacingModel(String name, EnumFacing facing) {
        return getStaticModel(name + facing.getName());
    }

    private static final Map<String, Model> models = new HashMap<>();

    static {
        for(Model model : Model.values()){
            models.put(model.getName(),model);
        }
    }

    //dummy
    public static final PropertyEnum<Model> TYPE = PropertyEnum.create("type", Model.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    protected boolean registerItem() {
        return false;
    }
}
