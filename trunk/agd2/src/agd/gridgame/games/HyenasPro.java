package agd.gridgame.games;

import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: juto
 * Date: Nov 13, 2009
 * Time: 3:38:33 PM
 */
public class HyenasPro extends Parameters {

    public HyenasPro () {
        super ();
        intParams[NUMBER_OF_RED_THINGS] = 6; // hyenas
        intParams[NUMBER_OF_BLUE_THINGS] = 2; // hyena hunters
        intParams[NUMBER_OF_GREEN_THINGS] = 4; // hyena traps

        intParams[RED_MOVEMENT_LOGIC] = CHASE;
        intParams[RED_OBJECTIVE] = AGENT;

        intParams[BLUE_MOVEMENT_LOGIC] = CLOCKWISE;

        collisionEffects[RED][RED] = DEATH;
        collisionScoreEffects[RED][RED] = 1;

        collisionEffects[RED][BLUE] = DEATH;
        collisionScoreEffects[RED][BLUE] = 1;
        collisionEffects[RED][GREEN] = DEATH;
        collisionScoreEffects[RED][GREEN] = 1;

        collisionEffects[AGENT][RED] = DEATH;
    }

}
