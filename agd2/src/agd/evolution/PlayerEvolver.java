package agd.evolution;

import agd.gridgame.*;
import agd.gridgame.games.Original;
import agd.controllers.RuleBasedController;
import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 15, 2008
 * Time: 5:30:21 PM
 */
public class PlayerEvolver implements ControllerLearner {

    private int popsize;
    private int elite;
    private int evaluationRepetitions;
    private Evolvable[] population;
    private double[][] fitness;
    private GridGameEvaluator evaluator;
    private Parameters parameters;
    private double[] bestFitnessDistribution = null;
    private double bestThisGeneration = Double.NEGATIVE_INFINITY;

    public static void main (String[] args) {
        Parameters parameters = new Original();
        //PlayerEvolver pe = new PlayerEvolver (parameters, new RMLPControllerPlus ());
        PlayerEvolver pe = new PlayerEvolver (parameters, new RuleBasedController());

        for (int generation = 0; generation < 100; generation++) {
            pe.oneMoreGeneration ();           
            System.out.println("Generation " + generation + " best " + pe.getBestFitness ());
        }
        Controller controller = (Controller) pe.getBest ();
        Easy.save (controller, "bestcontroller.xml");
        for (int i = 0; i < 5; i++) {
            Play.play (parameters, controller);    
        }

    }

    public PlayerEvolver (Parameters parameters, Evolvable controller) {
        this (parameters, controller, 100, 20);
    }

    public PlayerEvolver (Parameters parameters, Evolvable controller, int popsize, int evaluationRepetitions) {
        init (parameters, controller, popsize, evaluationRepetitions);
    }

    public void init(Parameters parameters, Controller initial, int populationSize) {
        init (parameters, (Evolvable) initial, populationSize, 20);
    }

    public void init (Parameters parameters, Evolvable controller, int populationSize, int evaluationRepetitions) {
        this.popsize = populationSize;
        elite = popsize / 2;
        this.evaluationRepetitions = evaluationRepetitions;
        fitness = new double[popsize][2];
        population = new Evolvable[popsize];
        //System.out.println("Parameters: " + parameters);
        evaluator = new GridGameEvaluator (evaluationRepetitions);
        this.parameters = parameters;
        population[0] = controller.copy ();
        for (int i = 1; i < popsize; i++) {
            population[i] = controller.newInstance();
            population[i].mutate ();
        }
    }

    public void oneMoreGeneration () {
        bestFitnessDistribution = null;
        bestThisGeneration = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < elite; i++) {
            evaluate (i);
        }
        for (int i = elite; i < population.length; i++) {
            population[i] = population[i - elite].copy ();
            population[i].mutate ();
            evaluate (i);
        }
        shuffle ();
        sortPopulationByFitness ();
    }

    public double getBestFitness () {
        return fitness[0][0];
    }

    public Evolvable getBest () {
        return population[0].copy ();
    }

    private void evaluate (int which) {
        fitness[which] = evaluator.evaluate(parameters, (Controller) population[which]);
        double[] fitnessDistribution = evaluator.getTrialFitnesses();
        if (bestFitnessDistribution == null || fitness[which][0] > bestThisGeneration) {
            bestFitnessDistribution = fitnessDistribution;
        }
    }

    private void sortPopulationByFitness () {
        for (int i = 0; i < population.length; i++) {
            for (int j = i + 1; j < population.length; j++) {
                if (fitness[i][0] < fitness[j][0]) {
                    swap (i, j);
                }
            }
        }
    }

    private void shuffle () {
        for (int i = 0; i < popsize; i++) {
            int other = (int) (Math.random () * popsize);
            swap (i, other);
        }
    }

    private void swap (int one, int two) {
        Evolvable temp = population[one];
        double[] tempFit = fitness[one];
        population[one] = population[two];
        fitness[one] = fitness[two];
        population[two] = temp;
        fitness[two] = tempFit;
    }


    public int getEvaluationRepetitions() {
        return evaluationRepetitions;
    }

    public double[] getBestFitnessDistribution () {
        return bestFitnessDistribution;
    }

    public void setEvaluationRepetitions(int evaluationRepetitions) {
        evaluator = new GridGameEvaluator (evaluationRepetitions);
        this.evaluationRepetitions = evaluationRepetitions;
    }

    public double getBestFitnessSD() {
        return fitness[0][1];
    }
}
