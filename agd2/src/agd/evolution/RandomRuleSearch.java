package agd.evolution;

import agd.gridgame.*;
import agd.PlayRandomGame;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Aug 15, 2008
 * Timjava agd.test.TestLearnability agd.gridgame.games.TrafficLightBuffete: 11:58:23 PM
 */
public class RandomRuleSearch {

    public static void main(String[] args) {
        SimpleParameterEvaluator evaluator = new SimpleParameterEvaluator();
        Parameters bestParameters = null;
        double bestFitness = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 30; i++) {
            Parameters parameters = Parameters.createRandomParameters();
            System.out.println("Testing new parameters:\n" + parameters.toString());
            double fitness = evaluator.evaluate(parameters)[0];
            System.out.println("Fitness: " + fitness);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestParameters = parameters;
                System.out.println("Replaced.");
            }
            System.out.println("\n");
        }
        System.out.println("Best game found:\n" + bestParameters);
        System.out.println("Fitness:" + bestFitness);
        GridGame game = new GridGame (bestParameters);
        View view = new View (game.getStateDescription());
        KeyboardController controller = new KeyboardController ();
        PlayRandomGame.initializeVisual(view, controller);
        GameResults results = game.play(view, controller, 0);
        System.out.println("Game results: agent " + (results.survived ? "survived" :  "died") +
                ", score: " + results.score);
    }


}
