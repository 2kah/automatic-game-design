package agd.gridgame.games;

import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 23, 2009
 * Time: 5:04:11 PM
 */
public class Terrorists extends Parameters {

    public Terrorists() {
        super ();
        intParams[Parameters.SCORELIMIT] = 2;
        intParams[Parameters.NUMBER_OF_RED_THINGS] = 5;  // bombs
        intParams[Parameters.NUMBER_OF_GREEN_THINGS] = 3;  // terrorists
        intParams[Parameters.NUMBER_OF_BLUE_THINGS] = 20;  // civilians
        intParams[Parameters.RED_MOVEMENT_LOGIC] = STILL;
        intParams[Parameters.GREEN_MOVEMENT_LOGIC] = CHASE;
        intParams[Parameters.BLUE_MOVEMENT_LOGIC] = FLEE;
        intParams[Parameters.GREEN_OBJECTIVE] = BLUE;
        intParams[Parameters.BLUE_OBJECTIVE] = GREEN;
        // terrorists kill civilians
        collisionEffects[BLUE][GREEN] = DEATH;
        collisionScoreEffects[BLUE][GREEN] = -1;
        // bombs kill civilians
        collisionEffects[BLUE][RED] = DEATH;
        collisionScoreEffects[BLUE][RED] = -1;
        // bombs kill agent
        collisionEffects[AGENT][RED] = DEATH;
        collisionScoreEffects[AGENT][RED] = -1;
        // agent kills terrorists
        collisionEffects[GREEN][AGENT] = DEATH;
        collisionScoreEffects[GREEN][AGENT] = +1;
        // agent kills civilians
        collisionEffects[BLUE][AGENT] = DEATH;
        collisionScoreEffects[BLUE][AGENT] = -1;
    }


}
