package darkenderhilda.create.content.kinetics.simpleRelays;

public interface ICogWheel {

    default boolean isLargeCog() {
        return false;
    }

    default boolean isSmallCog() {
        return !isLargeCog();
    }
}
