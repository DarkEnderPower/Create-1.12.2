package darkenderhilda.create.foundation.utility;

import net.minecraft.client.Minecraft;

public class AnimationTickHolder {

	public static int ticks;

	public static void tick() {
		if (!Minecraft.getMinecraft().isGamePaused()) {
			ticks = (ticks + 1) % 1_728_000; // wrap around every 24 hours so we maintain enough floating point precision
		}
	}

	public static int getTicks() {
		return ticks;
	}

	public static float getRenderTime() {
		return getTicks() + Minecraft.getMinecraft().getRenderPartialTicks();
	}
}
