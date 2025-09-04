package darkenderhilda.create;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.CreateBlock;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import java.util.HashMap;
import java.util.Map;

public class AllPartialModels extends CreateBlock {

    public AllPartialModels(BlockProperties properties) {
        super(properties);
    }

    public enum Model implements IStringSerializable {

        SHAFT_HALF_UP("shaft_half_up"),
        SHAFT_HALF_DOWN("shaft_half_down"),
        SHAFT_HALF_NORTH("shaft_half_north"),
        SHAFT_HALF_SOUTH("shaft_half_south"),
        SHAFT_HALF_WEST("shaft_half_west"),
        SHAFT_HALF_EAST("shaft_half_east"),

        DRILL_HEAD_UP("drill_head_up"),
        DRILL_HEAD_DOWN("drill_head_down"),
        DRILL_HEAD_NORTH("drill_head_north"),
        DRILL_HEAD_SOUTH("drill_head_south"),
        DRILL_HEAD_WEST("drill_head_west"),
        DRILL_HEAD_EAST("drill_head_east"),

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

        public IBlockState get() {
            return getStaticModel(name);
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
