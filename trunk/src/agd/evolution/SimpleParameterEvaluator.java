package agd.evolution;

import agd.gridgame.Parameters;
import agd.gridgame.RandomController;
import agd.gridgame.LongRandomController;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 24, 2008
 * Time: 10:27:05 PM
 */
public class SimpleParameterEvaluator implements ParameterEvaluator {

    final double maximumRandomFitness = 0.3;

    /**
     * Evaluates a set of Parameters by first using a Random and a LongRandom controller to play the game, if either
     * controller does too well then the Parameters fail and a fitness of -1 is returned. Next, an evolved controller
     * is used and the fitness determined by that controller is returned.
     *
     * @param parameters the game Parameters to be evaluated
     * @return an array of doubles containing a single value which is the fitness of the Parameters
     */
    public double[] evaluate(Parameters parameters) {
        // first check that the random controllers fail
        RandomController randomController = new RandomController ();
        LongRandomController longRandomController = new LongRandomController ();
        GridGameEvaluator gameval = new GridGameEvaluator(5);
        double highestRandomFitness = -1;
        for (int i = 0; i < 10; i++) {
            double fitness = gameval.evaluateOnce(parameters, randomController);
            if (fitness > highestRandomFitness) {
                highestRandomFitness = fitness;
            }
            fitness = gameval.evaluateOnce(parameters, longRandomController);
            if (fitness > highestRandomFitness) {
                highestRandomFitness = fitness;
            }
        }
        if (highestRandomFitness > maximumRandomFitness) {
            // these parameters are too easy; don't consider them further!
            System.out.println("Failed random tests");
            return new double[]{-1};
        }
        System.out.print("Passed random tests");
        // if not, return the highest score reached after 50 generations - the random cutoff
        PlayerEvolver pe = new PlayerEvolver (parameters, new MLPControllerPlus (), 10, 5);
        for (int generation = 0; generation < 50; generation++) {
            pe.oneMoreGeneration();
            System.out.print(".");
        }
        System.out.println();
        return new double[]{pe.getBestFitness()}; 
    }
}
