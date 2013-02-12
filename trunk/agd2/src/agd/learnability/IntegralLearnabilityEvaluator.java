package agd.learnability;

import agd.evolution.ControllerLearner;
import agd.evolution.Evolvable;
import agd.controllers.RMLPControllerPlus;
import agd.evolution.PlayerEvolver;
import agd.gridgame.Parameters;
import agd.gridgame.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 19, 2009
 * Time: 10:04:59 AM
 */
public class IntegralLearnabilityEvaluator implements LearnabilityEvaluator {

    final int trialsPerSample = 5;
    final int population = 10;
    final int generations = 100;

    public double[] evaluate(ControllerLearner learner, Parameters parameters) {
        // idea: divide the t-value (should be high) with the average deviation from the regressed straight line (should be low)
        double[] fitnesses = new double[generations]; // best fitness of each generation
        double[] standardDeviations = new double[generations]; // sd of best individual of each generation

        // first, get the learning curve as usual
        runLearningAlgorithm (learner, parameters, fitnesses, standardDeviations);
        // then, do linear regression on this (for now, just fit the first and last?)
        // y = kx + m
        double[] lineParams = linearRegression (fitnesses);
        double k = lineParams[0];
        double m = lineParams[1];
        
        // then, calculate average deviation from this line
        double sumOfDeviations = 0;
        for (int generation = 0; generation < generations; generation++) {
            double idealFitness = k * generation + m;
            double deviation = Math.abs (idealFitness - fitnesses[generation]);
            sumOfDeviations += deviation;
        }
        double averageDeviation = sumOfDeviations / generations;
        return new double[]{averageDeviation};
    }

    public double[] evaluate (Parameters parameters) {
        Evolvable initial = new RMLPControllerPlus();       
        return evaluate (new PlayerEvolver(parameters, initial), parameters);
    }

    private void runLearningAlgorithm (ControllerLearner learner, Parameters parameters,
                                       double[] fitnesses, double[] standardDeviations) {
        System.out.println("Running learning algorithm...");
        Controller initial = new RMLPControllerPlus();
        learner.init(parameters, initial, population);
        learner.setEvaluationRepetitions(trialsPerSample);

        // run the learning algorithm
        for (int generation = 0; generation < generations; generation++) {
            learner.oneMoreGeneration ();
            fitnesses[generation] = learner.getBestFitness();
            standardDeviations[generation] = learner.getBestFitnessSD();
            System.out.println("Generation " + generation + " fitness " + fitnesses[generation] +
                    " sd " + standardDeviations[generation]);
        }
    }

    private double[] linearRegression (double[] series) {
        double m = series[0];
        double k = (series[series.length - 1] - series[0]) / series.length;        
        return new double[]{k, m};
    }


}
