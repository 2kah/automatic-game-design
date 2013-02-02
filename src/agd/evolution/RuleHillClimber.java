package agd.evolution;

import agd.gridgame.*;
import agd.Test;
import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Aug 16, 2008
 * Time: 3:30:38 AM
 */
public class RuleHillClimber {

    public static void main(String[] args) {
        SimpleParameterEvaluator evaluator = new SimpleParameterEvaluator();
        Parameters bestParameters = Parameters.createRandomParameters();
        double bestFitness = Double.NEGATIVE_INFINITY;

        System.out.println("First we search randomly\n\n");

        for (int i = 0; i < 100; i++) {
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

        System.out.println("Starting climbing\n\n\n");

        for (int i = 0; i < 50; i++) {
            Parameters parameters = bestParameters.copy ();
            parameters.mutate ();
            System.out.println("Testing new parameters:\n" + parameters.toString());
            double fitness = evaluator.evaluate(parameters)[0];
            System.out.println("Fitness: " + fitness + " best " + bestFitness);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestParameters = parameters;
                System.out.println("Replaced.");
            }

            System.out.println("\n");
        }           


        System.out.println("Best game found:\n" + bestParameters);
        System.out.println("Fitness:" + bestFitness);
        Easy.save (bestParameters, "bestparameters.xml");
        GridGame game = new GridGame (bestParameters);
        View view = new View (game.getStateDescription());
        KeyboardController controller = new KeyboardController ();
        Test.initializeVisual(view, controller);
        GameResults results = game.play(view, controller, 0);
        System.out.println("Game results: agent " + (results.survived ? "survived" :  "died") +
                ", score: " + results.score + " out of " + game.getScoreToWin());
        game = new GridGame (bestParameters);
        view = new View (game.getStateDescription());
        Test.initializeVisual(view, controller);
        results = game.play(view, controller, 0);
        System.out.println("Game results: agent " + (results.survived ? "survived" :  "died") +
                ", score: " + results.score + " out of " + game.getScoreToWin());
        game = new GridGame (bestParameters);
        view = new View (game.getStateDescription());
        Test.initializeVisual(view, controller);
        results = game.play(view, controller, 0);
        System.out.println("Game results: agent " + (results.survived ? "survived" :  "died") +
                ", score: " + results.score + " out of " + game.getScoreToWin());

    }

}
