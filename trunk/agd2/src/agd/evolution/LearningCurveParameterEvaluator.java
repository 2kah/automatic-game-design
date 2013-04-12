package agd.evolution;

import agd.controllers.MLPControllerPlus;
import agd.gridgame.LongRandomController;
import agd.gridgame.Parameters;
import agd.gridgame.RandomController;

/**
 * Created with IntelliJ IDEA.
 * User: 2kah
 * Date: 04/04/13
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class LearningCurveParameterEvaluator implements ParameterEvaluator
{
    final double maximumRandomFitness = 0.3;

    int generations = 50;

    public double[] evaluate(Parameters parameters)
    {
        parameters.setFitnessKnown(true);
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
            return new double[]{-1};
        }

        //this will store the learning curve
        double[] fitnesses = new double[generations];

        // if not, return the highest score reached after 50 generations - the random cutoff
        PlayerEvolver pe = new PlayerEvolver (parameters, new MLPControllerPlus(), 10, 5);
        for (int generation = 0; generation < generations; generation++) {
            pe.oneMoreGeneration();
            fitnesses[generation] = pe.getBestFitness();
        }

        //TODO: return how similar the learning curve is to the ideal

        return new double[]{pe.getBestFitness()};
    }
}