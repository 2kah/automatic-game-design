package agd.evolution;

import agd.gridgame.Parameters;
import wox.serial.Easy;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 17, 2008
 * Time: 7:12:49 PM
 */
public class ParameterEvolver implements EA {

    static final int popsize = 20;
    static final int elite = popsize / 2;
    //static final int evaluationRepetitions = 20;
    final private Parameters[] population = new Parameters[popsize];
    final private double[] fitness = new double[popsize];
    final private ParameterEvaluator evaluator = new LearningCurveParameterEvaluator ();

    /**
     * Initialises the population using the given Parameters array and evaluates population fitness
     *
     * @param initial array of Parameters that will form the starting population
     */
    public ParameterEvolver (Parameters[] initial) {
        for (int i = 0; i < population.length; i++) {
            population[i] = initial[i % initial.length].copy ();
            fitness[i] = evaluator.evaluate(population[i])[0];
        }
        shuffle ();
        sortPopulationByFitness ();
    }

    public ParameterEvolver () {
        for (int i = 0; i < population.length; i++) {
            population[i] = Parameters.createRandomParameters();
            fitness[i] = evaluator.evaluate(population[i])[0];
        }
        shuffle ();
        sortPopulationByFitness ();
    }

    /**
     * Creates a new generation by mutating the least fit members of the previous one
     */
    public void oneMoreGeneration () {
        for (int i = 0; i < elite; i++) {
            if (!population[i].getFitnessKnown())
            {
                // evaluates the parameters (using LearningCurveParameterEvaluator)
                fitness[i] = evaluator.evaluate(population[i])[0];
            }
        }
        for (int i = elite; i < population.length; i++) {
            population[i] = population[i % elite].copy ();
            population[i].mutate ();
            fitness[i] = evaluator.evaluate(population[i])[0];
        }
        shuffle ();
        sortPopulationByFitness ();
    }

    /**
     * Sorts population from high to low fitness
     */
    private void sortPopulationByFitness () {
        for (int i = 0; i < population.length; i++) {
            for (int j = i + 1; j < population.length; j++) {
                if (fitness[i] < fitness[j]) {
                    swap (i, j);
                }
            }
        }
    }

    /**
     * Shuffles the population by swapping every individual with a random other (and their fitness)
     */
    private void shuffle () {
        for (int i = 0; i < popsize; i++) {
            int other = (int) (Math.random () * popsize);
            swap (i, other);
        }
    }

    /**
     * Swaps 2 members of the population
     *
     * @param one index of the first member
     * @param two index of the second member
     */
    private void swap (int one, int two) {
        if (one == two) return;
        Parameters temp = population[one];
        double tempFit = fitness[one];
        population[one] = population[two];
        fitness[one] = fitness[two];
        population[two] = temp;
        fitness[two] = tempFit;
    }

    public double getBestFitness () {
        return fitness[0];
    }

    /**
     *
     * @return the Parameters with the highest fitness in the population
     */
    public Parameters getBest () {
        return population[0];
    }

    /**
     * Evolves game parameters
     *
     * @param args No arguments taken (empty String array)
     */
    public static void main(String[] args) {
        Parameters[] initial = new Parameters[popsize];
        System.out.println("Generating sensible starting positions randomly");
        for (int i = 0; i < initial.length; i++) {
            initial[i] = randomParameterSearch (20, new LearningCurveParameterEvaluator());
        }

        File f = new File("Games");
        try
        {
            if(!f.mkdir())
                System.out.println("Games Directory not created");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Starting evolution");
        ParameterEvolver pe = new ParameterEvolver (initial);
        int generation = 0;
        int gameNumber = 0;
        Parameters lastParams = null;
        //evolve for 100 generations
        while (generation < 100) {
            //ensure genetic diversity by removing duplicates from the population, aka filtration
            pe.removeDuplicates();

            pe.oneMoreGeneration();

            double currentFitness = pe.getBestFitness();
            Parameters currentParams = pe.getBest();
            System.out.println("Generation " + generation++ + ", best: " + currentFitness);
            //only output if the game is different and is winnable
            if(currentParams.compareTo(lastParams) != 0)
            {
                if(currentParams.winnable)
                {
                    System.out.println(currentParams);
                    Easy.save  (currentParams, "Games/game" + String.format("%03d", gameNumber) + ".xml");
                    gameNumber++;
                }
                else
                    System.out.println("New game not output as it is not winnable");
                lastParams = currentParams;
            }
        }
    }

    /**
     * Compares every member of the population to every other, if any two are the same then one is replaced with a random individual
     */
    private void removeDuplicates() {
        boolean duplicateRemoved = false;
        for(int i = 0; i < population.length-1; i++)
        {
            for(int j = i+1; j < population.length; j++)
            {
                if(population[i].compareTo(population[j]) == 0)
                {
                    population[i] = randomParameterSearch(20, new LearningCurveParameterEvaluator());
                    fitness[i] = evaluator.evaluate(population[i])[0];
                    duplicateRemoved = true;
                }
            }
        }

        if(duplicateRemoved)
        {
            shuffle ();
            sortPopulationByFitness ();
        }
    }

    /**
     * Generates a number of random Parameters and returns the best
     *
     * @param steps number of times to generate random Parameters
     * @param evaluator a ParameterEvaluator of some sort (eg. LearningCurveParameterEvaluator)
     * @return the best Parameters found
     */
    public static Parameters randomParameterSearch (int steps, ParameterEvaluator evaluator) {
        Parameters best = null;
        double bestFitness = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < steps; i++) {
            Parameters temp = Parameters.createRandomParameters();
            double tempFitness = evaluator.evaluate(temp)[0];
            if (tempFitness > bestFitness) {
                bestFitness = tempFitness;
                best = temp;
            }
        }
        System.out.println("Best randomly generated parameter fitness: " + bestFitness);
        return best;
    }

}
