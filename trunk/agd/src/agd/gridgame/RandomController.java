package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 24, 2008
 * Time: 10:28:01 PM
 */

/**
 * A controller which selects a random move every time the control() function is called
 */
public class RandomController implements Controller, Constants {

    /**
     *
     * @param state a game state (eg. the current state of the current game)
     * @return a valid random action
     */
    public Action control(GameState.Description state) {
        Action action = new Action ();
        action.movement = direction.values()[(int) (Math.random () * direction.values().length)];
        return action;
    }

    public void reset() {}
}
