package darkenderhilda.create;

import darkenderhilda.create.foundation.block.BlockProperties;
import darkenderhilda.create.foundation.block.CreateBlock;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public class AllPartialModels extends CreateBlock {

    public AllPartialModels(BlockProperties properties) {
        super(properties);
    }

    public enum Type implements IStringSerializable {

        SHAFT_HALF_U("shaft_half_u"),
        SHAFT_HALF_D("shaft_half_d"),
        SHAFT_HALF_N("shaft_half_n"),
        SHAFT_HALF_E("shaft_half_e"),
        SHAFT_HALF_S("shaft_half_s"),
        SHAFT_HALF_W("shaft_half_w"),

        DRILL_HEAD_U("drill_head_u"),
        DRILL_HEAD_D("drill_head_d"),
        DRILL_HEAD_N("drill_head_n"),
        DRILL_HEAD_E("drill_head_e"),
        DRILL_HEAD_S("drill_head_s"),
        DRILL_HEAD_W("drill_head_w"),

        MILLSTONE_COG("millstone_cog"),
        SPEED_CONTROLLER_BRACKET_X("speed_controller_bracket_x"),
        SPEED_CONTROLLER_BRACKET_Z("speed_controller_bracket_z");

        private final String name;

        Type(String name) {
            this.name = name;
          }

        @Override
        public String getName() {
            return name;
        }
    }
    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

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
