package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 11, 2008
 * Time: 5:42:18 PM
 */
public interface Constants {

    // of course
    public final int X = 0, Y = 1;
    public final int timeLimit = 100;
    static enum direction {left, up, right, down}
    static final int[][] directionOffset = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};

    // colors of things
    public final int RED = 0;
    public final int GREEN = 1;
    public final int BLUE = 2;
    // the "color" of the agent
    public final int AGENT = 3;
    public final String[] colorNames = {"Red", "Green", "Blue", "Agent"};
    public final int NUMBER_OF_COLORS = 4;

    // Movement types for things
    public final int STILL = 0;
    public final int RANDOM_SHORT = 1;
    public final int RANDOM_LONG = 2;
    public final int CLOCKWISE = 3;
    public final int COUNTERCLOCKWISE = 4;
    public final int CHASE = 5;
    public final int FLEE = 6;
    public final int NUMBER_OF_MOVEMENT_TYPES = 7;
    public final String[] movementTypeNames = {"still", "random short", "random long", "clockwise",
            "counterclockwise", "chase", "flee"};
    public final boolean[] movementTypeHasTarget = {false, false, false, false, false, true, true};

    // collision effects
    public final int NONE = 0;
    public final int DEATH = 1;
    public final int TELEPORT = 2;
    public final int PUSH = 3;
    public final int NUMBER_Of_COLLISION_EFFECTS = 4;
    public final String[] collisionEffectNames = {"none", "death", "teleport", "push"};

}
