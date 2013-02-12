package agd.evolution;

import agd.gridgame.Controller;
import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 11, 2009
 * Time: 11:16:03 AM
 */
public interface ControllerLearner extends EA {

    public void init (Parameters parameters, Controller initial, int populationSize);

    public void setEvaluationRepetitions (int evaluationRepetitions);

    public double getBestFitnessSD();

    public double[] getBestFitnessDistribution (); // used for nonparametric tests
}
