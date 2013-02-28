package agd.gridgame;

import agd.gridgame.Action;
import agd.gridgame.GameState.Description;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jun 1, 2008
 * Time: 12:20:37 AM
 */
public interface Controller {

    public Action control (Description state);

    public void reset ();

}
