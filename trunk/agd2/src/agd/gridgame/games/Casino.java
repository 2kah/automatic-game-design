package agd.gridgame.games;

import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 23, 2009
 * Time: 5:45:57 PM
 */
public class Casino extends Parameters {

    public Casino() {
        super ();
        intParams[Parameters.NUMBER_OF_RED_THINGS] = 10;
        intParams[Parameters.NUMBER_OF_GREEN_THINGS] = 10;
        intParams[Parameters.NUMBER_OF_BLUE_THINGS] = 10;
        intParams[Parameters.RED_MOVEMENT_LOGIC] = RANDOM_LONG;
        intParams[Parameters.GREEN_MOVEMENT_LOGIC] = RANDOM_SHORT;
        intParams[Parameters.BLUE_MOVEMENT_LOGIC] = CLOCKWISE;
        
        collisionScoreEffects[RED][GREEN] = -1;
        collisionScoreEffects[RED][BLUE] = 1;


    }
}
