package agd.gridgame.games;

import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 15, 2008
 * Time: 5:34:30 PM
 */
public class Original extends Parameters {

    public Original() {
        super ();
        //intParams[Parameters.TIMELIMIT] = 50;
        intParams[Parameters.NUMBER_OF_RED_THINGS] = 3;
        intParams[Parameters.NUMBER_OF_GREEN_THINGS] = 2;
        intParams[Parameters.NUMBER_OF_BLUE_THINGS] = 1;
        intParams[Parameters.RED_MOVEMENT_LOGIC] = STILL;
        intParams[Parameters.GREEN_MOVEMENT_LOGIC] = COUNTERCLOCKWISE;
        intParams[Parameters.BLUE_MOVEMENT_LOGIC] = CLOCKWISE;
       // intParams[Parameters.SCORELIMIT] = 2;
        collisionEffects[AGENT][RED] = DEATH;
        collisionEffects[AGENT][BLUE] = TELEPORT;
        collisionEffects[BLUE][RED] = TELEPORT;
        collisionEffects[GREEN][AGENT] = DEATH;
        collisionEffects[BLUE][RED] = TELEPORT;
        collisionEffects[GREEN][RED] = TELEPORT;
        collisionScoreEffects[AGENT][BLUE] = 1;
        collisionScoreEffects[AGENT][GREEN] = -1;
    }

}
