package agd.test;

import agd.gridgame.Parameters;
import agd.learnability.*;
import agd.evolution.*;
import agd.controllers.RMLPControllerPlus;
import agd.controllers.PriorityController;
import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 24, 2009
 * Time: 4:39:14 PM
 */
public class TestLearnability {
                               
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

        Evolvable rmlp = new RMLPControllerPlus();
        Evolvable priority = new PriorityController();
        Evolvable initial = rmlp;

         System.out.println(initial);

        ControllerLearner evolver = new PlayerEvolver (parameters, initial);
        ControllerLearner random = new RandomPlayerSearch (parameters, initial);
        ControllerLearner learner = evolver;

        System.out.println(learner);


        LearnabilityEvaluator hle = new HighscoreLearnabilityEvaluator();
        LearnabilityEvaluator ile = new IntegralLearnabilityEvaluator();
        LearnabilityEvaluator sle = new SignificanceLearnabilityEvaluator();
        LearnabilityEvaluator ule = new ULearnabilityEvaluator();

        LearnabilityEvaluator evaluator = ule;

        System.out.println(evaluator);


        double[] results = evaluator.evaluate(learner, parameters);
        System.out.println("Learnability: " + results[0]);


    }



}
