package agd.gridgame.games;

import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 23, 2009
 * Time: 5:53:13 PM
 */
public class Hyenas extends Parameters {

    public Hyenas () {
        super ();
        intParams[NUMBER_OF_RED_THINGS] = 6; // hyenas
        intParams[RED_MOVEMENT_LOGIC] = CHASE;
        intParams[RED_OBJECTIVE] = AGENT;
        collisionEffects[RED][RED] = DEATH;
        collisionScoreEffects[RED][RED] = 1;
        collisionEffects[AGENT][RED] = DEATH;
    }

}
