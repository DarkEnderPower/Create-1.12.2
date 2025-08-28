package darkenderhilda.create.content.kinetics.base;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IRotate {

    enum SpeedLevel {
        NONE(ChatFormatting.DARK_GRAY, 0x000000, 0),
        SLOW(ChatFormatting.GREEN, 0x22FF22, 10),
        MEDIUM(ChatFormatting.AQUA, 0x0084FF, 20),
        FAST(ChatFormatting.LIGHT_PURPLE, 0xFF55FF, 30);

        private final ChatFormatting textColor;
        private final int color;
        private final int particleSpeed;

        SpeedLevel(ChatFormatting textColor, int color, int particleSpeed) {
            this.textColor = textColor;
            this.color = color;
            this.particleSpeed = particleSpeed;
        }

        public ChatFormatting getTextColor() {
            return textColor;
        }

        public int getColor() {
            return color;
        }

        public int getParticleSpeed() {
            return particleSpeed;
        }

        public float getSpeedValue() {
            switch (this) {
                case FAST:
                    return 100;//AllConfigs.server().kinetics.fastSpeed.get().floatValue();
                case MEDIUM:
                    return 50;//AllConfigs.server().kinetics.mediumSpeed.get().floatValue();
                case SLOW:
                    return 1;
                case NONE:
                default:
                    return 0;
            }
        }

    }

    boolean hasShaftTowards(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face);

    EnumFacing.Axis getRotationAxis(IBlockState state);

    default SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.NONE;
    }
}
