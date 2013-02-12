package agd.learnability;

import agd.evolution.*;
import agd.gridgame.Parameters;
import agd.gridgame.Controller;
import agd.gridgame.games.Original;
import agd.controllers.MLPControllerPlus;
import agd.controllers.RMLPControllerPlus;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 10, 2009
 * Time: 6:43:08 PM
 */
public class SignificanceLearnabilityEvaluator implements LearnabilityEvaluator {

    //private ControllerLearner learner;
    final int trialsPerSample = 30;
    final int population = 10;
    final int generations = 100;
        private final double[] tDistributionTable0point01 = new double[]{31.821, 6.965, 4.541, 3.747, 3.365, 3.143, 2.998, 2.896,
                                2.821, 2.764, 2.718, 2.681, 2.650, 2.624, 2.602, 2.583, 2.567, 2.552, 2.539, 2.528,
                                2.518, 2.508, 2.500, 2.492, 2.485, 2.479, 2.473, 2.467, 2.462, 2.457, 2.390, 2.358,
    	                        2.326};
        private final double[] tDistributionTable0point05 = new double[] {6.314, 2.920, 2.353, 2.132, 2.015, 1.943,
                1.895, 1.860, 1.833, 1.812, 1.796, 1.782, 1.771, 1.761, 1.753, 1.746, 1.740, 1.734, 1.729, 1.721,
                1.717, 1.714, 1.711, 1.708, 1.706, 1.703, 1.701, 1.699, 1.697, 1.694, 1.691, 1.688, 1.686, 1.684,
                1.682, 1.680, 1.679, 1.677, 1.676, 1.673, 1.671, 1.669, 1.667, 1.664, 1.660, 1.655, 1.653};

        private final double[] tDistributionTable = tDistributionTable0point05;

    public double[] evaluate (Parameters parameters) {
        Evolvable initial = new MLPControllerPlus();
        return evaluate (new PlayerEvolver (parameters, initial), parameters);
    }

    public double[] evaluate(ControllerLearner learner, Parameters parameters) {
        double[] fitnesses = new double[generations]; // best fitness of each generation
        double[] standardDeviations = new double[generations]; // sd of best individual of each generation
        runLearningAlgorithm (learner, parameters, fitnesses, standardDeviations);


        // calculate the fitness improvements
        boolean[] improvement = new boolean[generations];
        int numberOfImprovements = calculateLearningImprovements(fitnesses, standardDeviations, improvement);


        // calculate learnability - for one run only currently!

        double learnability = calculateLearnability (improvement, numberOfImprovements);
        System.out.println("Learnability: " + learnability);
        // run random search
                                          
        // calculate relative learnability

        return new double[]{learnability}; 
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

    private int calculateLearningImprovements (double[] fitnesses, double[] standardDeviations, boolean[] improvement) {
        System.out.println("\nCalculating learning improvements...");
        int lastImprovement = 0;
        int numberOfImprovements = 1;
        improvement[0] = true;

        for (int generation = 1; generation < generations; generation++) {
            double lastFit = fitnesses[lastImprovement];
            double lastSD = standardDeviations[lastImprovement];
            double curFit = fitnesses[generation];
            double curSD = standardDeviations[generation];
            double sumOfSquaredSD = (curSD * curSD) + (lastSD * lastSD);
            double t = ((curFit - lastFit) * Math.sqrt(trialsPerSample)) /
                    Math.sqrt (sumOfSquaredSD);

            double v = ((trialsPerSample - 1) * sumOfSquaredSD * sumOfSquaredSD) /
                    (Math.pow (curSD, 4) + Math.pow (lastSD, 4));
            int degreesOfFreedom = (int) v;
            degreesOfFreedom = Math.max (1, Math.min (degreesOfFreedom, tDistributionTable.length - 1));
            System.out.println("Degrees of freedom: " + degreesOfFreedom);
            if (t >= tDistributionTable[degreesOfFreedom]) {
                // we have made a new statistically significant improvement
                improvement[generation] = true;
                lastImprovement = generation;
                numberOfImprovements++;
            }
            System.out.println("Generation " + generation + " fit " + curFit + " sd " + curSD +
                    " t " + t + " v " + v + " improvement " + improvement[generation]);
        }
        System.out.println("Number of Improvements: " + numberOfImprovements);
        return numberOfImprovements;
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

    public static void main(String[] args) {
        Parameters parameters = new Original();
        Evolvable initial = new RMLPControllerPlus ();
        ControllerLearner learner = new PlayerEvolver (parameters, initial);
        SignificanceLearnabilityEvaluator evaluator = new SignificanceLearnabilityEvaluator();
        evaluator.evaluate (learner, parameters);
    }

}
