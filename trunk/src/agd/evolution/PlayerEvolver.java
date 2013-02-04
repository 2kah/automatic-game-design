package agd.evolution;

import agd.gridgame.Controller;
import agd.gridgame.Parameters;
import agd.gridgame.Play;
import agd.gridgame.TestParameters1;
import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 15, 2008
 * Time: 5:30:21 PM
 */
public class PlayerEvolver implements EA {

    final int popsize;
    final int elite;
    final int evaluationRepetitions;
    final private Evolvable[] population;
    final private double[] fitness;
    final private GridGameEvaluator evaluator;
    final private Parameters parameters;

    public static void main (String[] args) {
        Parameters parameters = new TestParameters1 ();
        //PlayerEvolver pe = new PlayerEvolver (parameters, new RMLPControllerPlus ());
        PlayerEvolver pe = new PlayerEvolver (parameters, new RuleBasedController ());

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

    /**
     * Sets up a PlayerEvolver, which will evolve a player controller using the given controller as a starting point.
     * The player will be evolved towards being able to play the game described by the given Parameters.
     *
     * @param parameters game parameters for the player to play
     * @param controller controller which implements Evolvable, this controller will evolve to play the game described
     *                   by the given Parameters
     * @param popsize size of the population of controllers
     * @param evaluatorTrials number of trials for the evaluator to perform when determining fitness
     */
    public PlayerEvolver (Parameters parameters, Evolvable controller, int popsize, int evaluatorTrials) {
        this.popsize = popsize;
        elite = popsize / 2;
        this.evaluationRepetitions = 10;
        fitness = new double[popsize];
        population = new Evolvable[popsize];
        //System.out.println("Parameters: " + parameters);
        evaluator = new GridGameEvaluator (evaluatorTrials);
        this.parameters = parameters;
        for (int i = 0; i < popsize; i++) {
            population[i] = controller.copy();
            population[i].mutate ();
            evaluate(i);
        }
        shuffle ();
        sortPopulationByFitness ();
    }

    /**
     * Creates a new generation by evaluating the first half of the population (most fit if sorted) and then mutating
     * them to create the second half
     */
    public void oneMoreGeneration () {
        //TODO: optimise by not evaluating those which have already been
        for (int i = 0; i < elite; i++) {
            evaluate (i);
        }
        //this uses the most fit half of the previous generation, and mutates them to create the other half
        //TODO: might be better to implement some kind of crossover, or at least mutate the whole previous generation and then select the most fit half
        for (int i = elite; i < population.length; i++) {
            population[i] = population[i - elite].copy ();
            population[i].mutate ();
            evaluate (i);
        }
        shuffle ();
        sortPopulationByFitness ();
    }

    public double getBestFitness () {
        return fitness[0];
    }

    public Evolvable getBest () {
        return population[0].copy ();
    }

    private void evaluate (int which) {
        fitness[which] = evaluator.evaluate(parameters, (Controller) population[which]);
    }

    private void sortPopulationByFitness () {
        for (int i = 0; i < population.length; i++) {
            for (int j = i + 1; j < population.length; j++) {
                if (fitness[i] < fitness[j]) {
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
        double tempFit = fitness[one];
        population[one] = population[two];
        fitness[one] = fitness[two];
        population[two] = temp;
        fitness[two] = tempFit;
    }

}
