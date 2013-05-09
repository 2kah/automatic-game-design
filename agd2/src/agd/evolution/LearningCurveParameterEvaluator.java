package agd.evolution;

import agd.controllers.RMLPControllerPlus;
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
    final int generations = 50;

    double idealCurve[];

    public LearningCurveParameterEvaluator()
    {
        idealCurve = new double[generations];

        for(int i = 0; i < generations; i++)
        {
            idealCurve[i] = (i+1) / (double) generations;
        }
    }

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

        // if not, calculate the difference between the learning curve of controller and ideal curve
        PlayerEvolver pe = new PlayerEvolver (parameters, new RMLPControllerPlus(), 10, 5);
        for (int generation = 0; generation < generations; generation++) {
            pe.oneMoreGeneration();
            fitnesses[generation] = pe.getBestFitness();
        }

        //sum of squares
        double squaresAccumulator = 0;
        for(int i = 0; i < generations; i++)
        {
            squaresAccumulator += Math.pow(fitnesses[i] - idealCurve[i], 2);
        }

        //fitness is 1 / sum of squared difference
        return new double[]{1 / squaresAccumulator};
    }
}
