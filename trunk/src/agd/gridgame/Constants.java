package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 11, 2008
 * Time: 5:42:18 PM
 */
public interface Constants {

    // of course
    public final int X = 0, Y = 1;

    static enum direction {left, up, right, down}

    // colors of things
    public final int RED = 0;
    public final int GREEN = 1;
    public final int BLUE = 2;
    // the "color" of the agent
    public final int AGENT = 3;
    public final String[] colorNames = {"Red", "Green", "Blue", "Agent"};

    // Movement types for things
    public final int STILL = 0;
    public final int RANDOM_SHORT = 1;
    public final int RANDOM_LONG = 2;
    public final int CLOCKWISE = 3;
    public final int COUNTERCLOCKWISE = 4;
    public final int NUMBER_OF_MOVEMENT_TYPES = 5;
    public final String[] movementTypeNames = {"still", "random short", "random long", "clockwise", "counterclockwise"};

    // collision effects
    public final int NONE = 0;
    public final int DEATH = 1;
    public final int TELEPORT = 2;
    public final int NUMBER_Of_COLLISION_EFFECTS = 3;
    public final String[] collisionEffectNames = {"none", "death", "teleport"};

}
