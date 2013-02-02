package agd.evolution;

import agd.gridgame.*;
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

    public PlayerEvolver (Parameters parameters, Evolvable controller, int popsize, int evaluationRepetitions) {
        this.popsize = popsize;
        elite = popsize / 2;
        this.evaluationRepetitions = 10;
        fitness = new double[popsize];
        population = new Evolvable[popsize];
        //System.out.println("Parameters: " + parameters);
        evaluator = new GridGameEvaluator (evaluationRepetitions);
        this.parameters = parameters;
        for (int i = 0; i < popsize; i++) {
            population[i] = controller.copy();
            population[i].mutate ();
        }
    }

    public void oneMoreGeneration () {
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
