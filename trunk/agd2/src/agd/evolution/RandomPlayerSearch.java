package agd.evolution;

import agd.gridgame.Parameters;
import agd.gridgame.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 27, 2009
 * Time: 12:13:08 PM
 */
public class RandomPlayerSearch implements ControllerLearner {

    private int populationSize = 0;
    //private int evaluationRepetitions = 0;
    private Evolvable initial = null;
    private Parameters parameters = null;
    private GridGameEvaluator evaluator = null;
    private double bestFitness = Double.NEGATIVE_INFINITY;
    private double bestFitnessSD = 0;
    private double[] bestFitnessDistribution = null;
    private Evolvable bestEvolvable;


    public RandomPlayerSearch (Parameters parameters, Evolvable initial) {
        init (parameters, (Controller) initial, 100, 20);
    }

    public void init(Parameters parameters, Controller initial, int populationSize, int evaluationRepetitions) {
        this.populationSize = populationSize;
        this.initial = (Evolvable) initial;
        //this.evaluationRepetitions = evaluationRepetitions;
        this.parameters = parameters;
        evaluator = new GridGameEvaluator(evaluationRepetitions);
    }


    public void init(Parameters parameters, Controller initial, int populationSize) {
        init (parameters, initial, populationSize, 20);
    }

    public void setEvaluationRepetitions(int evaluationRepetitions) {
        //this.evaluationRepetitions = evaluationRepetitions;
        evaluator = new GridGameEvaluator(evaluationRepetitions);
    }

    public double getBestFitnessSD() {
        return bestFitnessSD;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public Evolvable getBest() {
        return bestEvolvable;
    }

    public double[] getBestFitnessDistribution () {
        return bestFitnessDistribution;
    }

    public void oneMoreGeneration() {
        double[] best = new double[]{Double.NEGATIVE_INFINITY, 0};
        for (int sample = 0; sample < populationSize; sample++) {
            Controller controller = (Controller) initial.newInstance ();
            double[] results = evaluator.evaluate(parameters, controller);
            if (results[0] > best[0]) {
                best[0] = results[0];
                best[1] = results[1];
                bestEvolvable = (Evolvable) controller;
                bestFitnessDistribution = evaluator.getTrialFitnesses();
            }
        }
        bestFitness = best[0];
        bestFitnessSD = best[1];
    }
}
