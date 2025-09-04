package darkenderhilda.create.content.kinetics.belt;

import net.minecraft.util.IStringSerializable;

public enum BeltPart implements IStringSerializable {
    START("start"),
    MIDDLE("middle"),
    END("end"),
    PULLEY("pulley");

    private final String name;

    BeltPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
