package agd.learnability;

import agd.controllers.MLPControllerPlus;
import agd.evolution.ControllerLearner;
import agd.evolution.Evolvable;
import agd.evolution.PlayerEvolver;
import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 19, 2009
 * Time: 9:55:06 AM
 */
public class HighscoreLearnabilityEvaluator implements LearnabilityEvaluator {

    final int trialsPerSample = 5;
    final int population = 10;
    final int generations = 100;
    final int sizeOfHighScoreTable = 10;

    public double[] evaluate(ControllerLearner learner, Parameters parameters) {
        // count how often how high values on the high score table are replaced
        int placementCount = 0;
        final double[] highScoreTable = new double[sizeOfHighScoreTable];
        for (int i = 0; i < sizeOfHighScoreTable; i++) {
            highScoreTable[i] = Double.NEGATIVE_INFINITY;
        }
        double[] fitnesses = new double[generations]; // best fitness of each generation
        //double[] standardDeviations = new double[generations]; // sd of best individual of each generation
        for (int generation = 0; generation < generations; generation++) {
            learner.oneMoreGeneration ();
            fitnesses[generation] = learner.getBestFitness();
            //standardDeviations[generation] = learner.getBestFitnessSD();
            int placement = place (fitnesses[generation], highScoreTable);
            placementCount += placement;
            System.out.println("Generation " + generation + " fitness " + fitnesses[generation] +
                    " placement " + placement);//" sd " + standardDeviations[generation]);
        }
        return new double[]{placementCount};
    }

    public double[] evaluate (Parameters parameters) {
        Evolvable initial = new MLPControllerPlus();
        return evaluate (new PlayerEvolver(parameters, initial), parameters);
    }

    /**
     *
     * @param newScore the new score to be inserted
     * @param highScoreTable the high score table
     * @return the placement of the new score in the table (eg. 10 would be top score, 0 means high enough for the table)
     */
    private int place (double newScore, double[] highScoreTable) {
        for (int i = 0; i < highScoreTable.length; i++) {
            if (newScore > highScoreTable[i]) {
                for (int j = highScoreTable.length - 1; j > i; j--) {
                    highScoreTable[j] = highScoreTable[j-1];
                }
                highScoreTable[i] = newScore;
                return highScoreTable.length - i;
            }
        }
        return 0;
    }

}
