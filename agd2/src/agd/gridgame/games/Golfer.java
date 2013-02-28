package agd.gridgame.games;

import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Nov 11, 2009
 * Time: 11:26:24 AM
 */
public class Golfer extends Parameters {

    public Golfer () {
        super ();
        intParams[Parameters.SCORELIMIT] = 9;
        intParams[Parameters.NUMBER_OF_GREEN_THINGS] = 18;
        intParams[Parameters.NUMBER_OF_RED_THINGS] = 9;
        intParams[Parameters.NUMBER_OF_BLUE_THINGS] = 9;

        collisionEffects[RED][AGENT] = PUSH;

        collisionScoreEffects[RED][GREEN] = +1;
        collisionEffects[RED][GREEN] = DEATH;
        collisionScoreEffects[BLUE][GREEN] = +1;
        collisionEffects[BLUE][GREEN] = DEATH;

        intParams[BLUE_MOVEMENT_LOGIC] = FLEE;
        intParams[BLUE_OBJECTIVE] = AGENT;
    }
}
