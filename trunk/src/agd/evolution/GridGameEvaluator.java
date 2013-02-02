package agd.evolution;

import agd.gridgame.*;


/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 13, 2008
 * Time: 9:28:57 PM
 */
public class GridGameEvaluator {

    private final int numberOfTrials;

    public GridGameEvaluator(int numberOfTrials) {
        this.numberOfTrials = numberOfTrials;
    }

    public double evaluateOnce(Parameters parameters, Controller controller) {
        controller.reset();
        GridGame game = new GridGame(parameters);
        GameResults results = game.play(null, controller, 0);
        if (!results.survived) {
            return -1;
        }
        double scoreToWin = (double) game.getScoreToWin();
        if (scoreToWin == 0) scoreToWin = 1;
        return Math.max(-1, (results.score / scoreToWin));
    }

    public double evaluate(Parameters parameters, Controller controller) {
        double fitness = 0;
        for (int trial = 0; trial < numberOfTrials; trial++) {
            fitness = fitness + evaluateOnce(parameters, controller);
        }
        return fitness / numberOfTrials;
    }

}
