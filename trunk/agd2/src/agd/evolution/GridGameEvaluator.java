package agd.evolution;

import agd.gridgame.Controller;
import agd.gridgame.GameResults;
import agd.gridgame.GridGame;
import agd.gridgame.Parameters;


/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 13, 2008
 * Time: 9:28:57 PM
 */
public class GridGameEvaluator {

    private final int numberOfTrials;
    private final double[] trialFitnesses;

    public GridGameEvaluator(int numberOfTrials) {
        this.numberOfTrials = numberOfTrials;
        trialFitnesses = new double[numberOfTrials];
    }

    /**
     * Evaluates the fitness of the given Parameters once, using the given Controller. Fitness is either -1 for failure
     * or the score obtained / score to win. So fitness is effectively how close the controller gets to winning, where
     * -1 is total failure and 1 is success.
     *
     * @param parameters game Parameters to evaluate
     * @param controller controller to play the game during evaluation
     * @return the fitness for this evaluation, -1 if player doesn't survive or gets negative score, otherwise the
     * score obtained / score needed to win
     */
    public double evaluateOnce(Parameters parameters, Controller controller) {
        controller.reset();
        GridGame game = new GridGame(parameters);
        GameResults results = game.play(null, controller, 0);
        if (!results.survived) {
            return -1;
        }
        double scoreToWin = (double) parameters.getScoreToWin();
        //prevent division by zero
        if (scoreToWin == 0) scoreToWin = 1;
        return Math.max(-1, (results.score / scoreToWin));
    }

    /**
     * Evaluates the fitness of the given Parameters a number of times and returns the average fitness and standard
     * deviation from that fitness. Effectively, the fitness returned is 'how close does the given controller get to
     * winning on average'.
     *
     * @param parameters game Parameters to evaluate
     * @param controller controller to use during evaluation
     * @return average fitness over the number of trials (cumulative fitness / number of trials)
     */
    public double[] evaluate(Parameters parameters, Controller controller) {
        double fitnessSum = 0;
        for (int trial = 0; trial < numberOfTrials; trial++) {
            trialFitnesses[trial] =  evaluateOnce(parameters, controller);
            fitnessSum = fitnessSum + trialFitnesses[trial];
        }
        double meanFitness = fitnessSum / numberOfTrials;
        double deviationSum = 0;
        for (int trial = 0; trial < numberOfTrials; trial++) {
            deviationSum += (trialFitnesses[trial] - meanFitness) * (trialFitnesses[trial] - meanFitness);
        }
        double sd = deviationSum / numberOfTrials;
        return new double[]{meanFitness , sd};
    }

    public double[] getTrialFitnesses () {
        double[] fitnesses = new double[numberOfTrials];
        System.arraycopy(trialFitnesses, 0, fitnesses, 0, numberOfTrials);
        return fitnesses;
    }

}
