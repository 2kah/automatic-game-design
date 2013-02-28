package agd.gridgame.games;

import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Nov 11, 2009
 * Time: 12:53:36 AM
 */
public class TrafficLightBuffet extends Parameters {

    public TrafficLightBuffet() {
        super ();
        intParams[Parameters.SCORELIMIT] = 10;
        intParams[Parameters.NUMBER_OF_GREEN_THINGS] = 10;
        intParams[Parameters.NUMBER_OF_RED_THINGS] = 10;
        intParams[Parameters.GREEN_MOVEMENT_LOGIC] = STILL;
        intParams[Parameters.RED_MOVEMENT_LOGIC] = STILL;

        collisionEffects[RED][AGENT] = DEATH;
        collisionScoreEffects[RED][AGENT] = -1;
        collisionEffects[GREEN][AGENT] = DEATH;
        collisionScoreEffects[GREEN][AGENT] = 1;

        


    }

}
