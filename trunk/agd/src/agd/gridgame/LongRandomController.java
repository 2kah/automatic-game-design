package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 25, 2008
 * Time: 7:06:17 PM
 */

/**
 * A controller which selects a random direction to move in and a random number of steps to move in that direction
 */
public class LongRandomController implements Controller, Constants {

    private int stepsLeft = 0;
    private direction current = null;

    /**
     *
     * @param state a game state (eg. the current state of the current game)
     * @return a random action, which may be part of a sequence of planned actions to move in a particular direction
     */
    public Action control(GameState.Description state) {
        if (stepsLeft < 1) {
            current = direction.values()[(int) (Math.random () * direction.values().length)];
            stepsLeft = (int) (Math.random () * 10);
        }
        Action action = new Action ();

        action.movement = current;

        return action;
    }

    public void reset() {
        stepsLeft = 0;
    }
}
