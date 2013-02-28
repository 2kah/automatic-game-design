package agd.learnability;

import agd.gridgame.Parameters;
import agd.gridgame.Controller;
import agd.evolution.*;
import agd.controllers.MLPControllerPlus;
import agd.controllers.RMLPControllerPlus;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 31, 2009
 * Time: 11:56:19 AM
 */
public class ULearnabilityEvaluator implements LearnabilityEvaluator {

    final int trialsPerSample = 50;
    final int population = 50;
    final int generations = 1000;
    final double zLimit = 1.28;
    enum distributionSelection {single, block};
    final distributionSelection distmode = distributionSelection.block;

    public double[] evaluate (Parameters parameters) {
        Evolvable initial = new MLPControllerPlus();
        return evaluate (new PlayerEvolver(parameters, initial), parameters);
    }

    public static void main(String[] args) {
        new ULearnabilityEvaluator().test();
    }

    public void test() {
        double[] dist1 = {-1, -1, 2, 0, 2, -1, -1, -1, -1};
        double[] dist2 = {-1, -1, 2, 2, 1, 2, -1, 0, 1, -1};
        print (dist1);
        System.out.println();
        print (dist2);
        System.out.println();
        double u = calculateU (dist2, dist1) ;
        System.out.println("u = " + u);
        double z = calculateZ (u, dist1.length, dist2.length);
        System.out.println("z = " + z);
    }

    public double[] evaluate(ControllerLearner learner, Parameters parameters) {

        double[][] fitnessDistributions = new double[generations][trialsPerSample];
        runLearningAlgorithm (learner, parameters, fitnessDistributions);
        // calculate the fitness improvements
        boolean[] improvement = new boolean[generations];
        int numberOfImprovements = calculateLearningImprovements(fitnessDistributions, improvement);
        System.out.println("Number of improvements: " + numberOfImprovements);
        // calculate learnability - for one run only currently!
        double learnability = calculateLearnability (improvement, numberOfImprovements);
        System.out.println("Learnability: " + learnability);
        // run random search

        // calculate relative learnability
        return new double[]{learnability};
    }

    private int calculateLearningImprovements (double[][] fitnessDistributions, boolean[] improvement) {
        System.out.println("\nCalculating learning improvements...");
        int lastImprovement = 0;
        int numberOfImprovements = 1;
        improvement[0] = true;

        for (int generation = 1; generation < generations; generation++) {
            double[][] relevantDistributions = selectDistributions (fitnessDistributions, lastImprovement, generation);
            double[] previous = relevantDistributions[0];
            double[] current = relevantDistributions[1];

            //double u = calculateU (fitnessDistributions[generation], fitnessDistributions[lastImprovement]);
            //double z = calculateZ (u,
            //        fitnessDistributions[generation].length, fitnessDistributions[lastImprovement].length);
            double u = calculateU (current, previous);
            double z = calculateZ (u,
                    current.length, previous.length);


            System.out.println("gen " + generation + " u " + u + " z " + z);
            if (z > zLimit) {
                // we have a statistically significant difference
                System.out.println("* Improvement! *");
                improvement[generation] = true;
                lastImprovement = generation;
                numberOfImprovements++;
            }
            
        }
        return numberOfImprovements;
    }

    private double[][] selectDistributions (double[][] fitnessDistributions,
                                            int lastImprovement, int currentGeneration){

            if (distmode == distributionSelection.single) {
                // only the distribution at the last improvement and the current generation
                return new double[][]{fitnessDistributions[lastImprovement], fitnessDistributions[currentGeneration]};
            }
            if (distmode == distributionSelection.block) {
                System.out.print("Selecting distributions with lastImprovement " + lastImprovement +
                        " currentGeneration " + currentGeneration);
                // full blocks
                int difference = currentGeneration - lastImprovement;
                int startOfPrevious = Math.max (0, lastImprovement-difference);
                System.out.println(" difference " + difference + " startOfPrevious " + startOfPrevious);
                double[] previous = new double[(lastImprovement - startOfPrevious + 1) * trialsPerSample];
                copy (fitnessDistributions, previous, startOfPrevious);
                double[] current = new double[difference * trialsPerSample];
                copy (fitnessDistributions, current, lastImprovement + 1);
                return new double[][]{previous, current};
            }
        throw new RuntimeException ("Unknown distmode");
    }

    private void copy (double[][] from, double[] to, int start) {
        //System.out.println("Attempting to copy fromlength " + from.length + " from0length " + from[0].length
        //        + " tolength " + to.length + " start "+ start);
        int fromIndex = start;
        int toIndex = 0;
        do {
            System.arraycopy (from[fromIndex], 0, to, toIndex, trialsPerSample);
            fromIndex ++;
            toIndex += trialsPerSample;

        } while (toIndex < to.length - 1);
    }

    private double calculateU (double[] distribution1, double[] distribution2) {
        double u = 0;
        for (int i = 0; i < distribution1.length; i++) {
            for (int j = 0; j < distribution2.length; j++) {
                if (distribution1[i] > distribution2[j]
                        || (distribution1[i] == distribution2[j] && Math.random () < 0.5)
                        ) {
                   // System.out.print("+");
                    u++;
                }
                //else
                //    System.out.print("-");
            }
        }
        return u;
    }

    private double calculateZ (double u, int n1, int n2) {
        double mU = (n1 * n2) / 2;
        double sigU = Math.sqrt ((n1 * n2 * (n1 + n2 + 1)) / 12);
        double z = (u - mU) / sigU;
        return z;
    }

    private void runLearningAlgorithm (ControllerLearner learner, Parameters parameters,
                                       double[][] fitnessDistributions) {
        System.out.println("Running learning algorithm...");
        Controller initial = new RMLPControllerPlus();
        learner.init(parameters, initial, population);
        learner.setEvaluationRepetitions(trialsPerSample);

        // run the learning algorithm
        for (int generation = 0; generation < generations; generation++) {
                  learner.oneMoreGeneration ();
                  fitnessDistributions[generation] = learner.getBestFitnessDistribution();
                  System.out.print("Generation " + generation + " fitness " + learner.getBestFitness () +
                          " distribution ");// + fitnessDistributions[generation]);
                  print (fitnessDistributions[generation]);
                    System.out.println();
        }
    }

    private double calculateLearnability (boolean[] improvement, int numberOfImprovements) {
        System.out.println("\nCalculating learnability...");
        double meanGapBetweenImprovements = generations / (double) numberOfImprovements;
        double deviationSum = 0;
        double sdGapBetweenImprovements = 0;
        int lastImprovement = 0;
        if (numberOfImprovements > 1) {
            for (int generation = 1; generation < generations; generation++) {
                if (improvement[generation]) {
                    int difference = generation - lastImprovement;
                    deviationSum += (difference - meanGapBetweenImprovements) *
                            (difference - meanGapBetweenImprovements);
                    lastImprovement = generation;
                    System.out.println("Generation " + generation + " difference " + difference);
                }

            }
            sdGapBetweenImprovements = deviationSum / (numberOfImprovements - 1);
        }
        return 1 / (meanGapBetweenImprovements + Math.sqrt (sdGapBetweenImprovements));
    }

    private void print (double[] array) {
        for (double f : array) {
            System.out.printf("%.3f ", f);
        }
    }
}
