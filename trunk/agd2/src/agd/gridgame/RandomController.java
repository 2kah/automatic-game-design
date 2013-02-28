package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 24, 2008
 * Time: 10:28:01 PM
 */
public class RandomController implements Controller, Constants {
    
    public Action control(GameState.Description state) {
        Action action = new Action ();
        action.movement = direction.values()[(int) (Math.random () * direction.values().length)];
        return action;
    }

    public void reset() {}
}
