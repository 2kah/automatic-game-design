package agd.controllers;

import agd.gridgame.*;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Apr 29, 2010
 * Time: 7:08:26 PM
 */
public class NaiveMonteCarloController implements Controller, Constants {

    final int numberOfRollOuts = 10;

    public Action control(GameState.Description state) {
        // try to take all four actions, check the value of each action

        double upValue = evaluateAction (state.getGameCopy(), direction.up);


        return null;
    }

    public double evaluateAction (GridGame game, direction dir) {
        double resultSum = 0;
        for (int i = 0; i < numberOfRollOuts; i++) {
            GridGame rollGame = game.copy ();
            GameResults results = rollGame.play(null, new RandomController (), 0);
            resultSum += results.score;
        }
        return resultSum / numberOfRollOuts;
    }


    public void reset() {}
}
