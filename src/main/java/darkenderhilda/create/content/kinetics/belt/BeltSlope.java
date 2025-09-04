package darkenderhilda.create.content.kinetics.belt;

import net.minecraft.util.IStringSerializable;

public enum BeltSlope implements IStringSerializable {
	HORIZONTAL("horizontal"),
	UPWARD("upward"),
	DOWNWARD("downward"),
	VERTICAL("vertical"),
	SIDEWAYS("sideways");

	private final String name;

	BeltSlope(String name) {
        this.name = name;
    }

	@Override
	public String getName() {
		return name;
	}

	public boolean isDiagonal() {
		return this == UPWARD || this == DOWNWARD;
	}
}
