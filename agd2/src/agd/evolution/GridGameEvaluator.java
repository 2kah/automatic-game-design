package agd.evolution;

import agd.gridgame.*;


/**
 * Created by IntelliJ IDEA.
 * User: julian
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

    public double evaluateOnce(Parameters parameters, Controller controller) {
        controller.reset();
        GridGame game = new GridGame(parameters);
        GameResults results = game.play(null, controller, 0);
        if (!results.survived) {
            return -1;
        }
        //double scoreToWin = (double) game.getScoreToWin();
        //if (scoreToWin == 0) scoreToWin = 1;
        return Math.max(-1, (results.score ));
    }

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
