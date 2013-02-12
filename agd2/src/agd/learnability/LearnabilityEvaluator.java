package agd.learnability;

import agd.evolution.ParameterEvaluator;
import agd.evolution.ControllerLearner;
import agd.gridgame.Parameters;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 19, 2009
 * Time: 9:45:51 AM
 */
public interface LearnabilityEvaluator extends ParameterEvaluator {

    public double[] evaluate(ControllerLearner learner, Parameters parameters);        

}
