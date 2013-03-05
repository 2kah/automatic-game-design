package agd.learnability;

import agd.gridgame.Parameters;
import wox.serial.Easy;
import agd.evolution.*;
import agd.controllers.*;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 27, 2009
 * Time: 12:06:09 PM
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Parameters parameters = null;
        try {
            parameters = (Parameters) Class.forName (args[0]).newInstance ();
        }
        catch (ClassNotFoundException e) {
            System.out.println (args[0] + " is not a class name; trying to load a wox definition with that name.");
            parameters = (Parameters) Easy.load (args[0]);
        }
        System.out.println(parameters);

        LearnabilityEvaluator learnabilityEval = null;
        learnabilityEval = new IntegralLearnabilityEvaluator();
        //learnability = new HighscoreLearnabilityEvaluator();
        Evolvable controller = new RMLPControllerPlus();
        ControllerLearner  controllerEvolver = null;
        controllerEvolver = new PlayerEvolver(parameters, controller);
        //learner = new RandomPlayerSearch(parameters, controller);

        double[] results = learnabilityEval.evaluate(controllerEvolver, parameters);
        System.out.println("Learnability: " + results[0]);

    }


}
