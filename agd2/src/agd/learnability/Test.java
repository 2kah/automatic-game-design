package agd.learnability;

import agd.controllers.*;
import agd.evolution.ControllerLearner;
import agd.evolution.Evolvable;
import agd.evolution.PlayerEvolver;
import agd.gridgame.Parameters;
import wox.serial.Easy;

import java.io.FileWriter;

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

        //LearnabilityEvaluator learnabilityEval = new IntegralLearnabilityEvaluator();
        //learnability = new HighscoreLearnabilityEvaluator();
        //Evolvable controller = new RMLPControllerPlus();
        //ControllerLearner controllerEvolver = null;

        //int generations = 100;
        //controllerEvolver = new PlayerEvolver(parameters, controller, generations, 20);
        //learner = new RandomPlayerSearch(parameters, controller);

        //double[] results = learnabilityEval.evaluate(controllerEvolver, parameters);
        //System.out.println("Learnability: " + results[0]);

        //IntegralLearnabilityEvaluator learnability = (IntegralLearnabilityEvaluator) learnabilityEval;

        generateCurve(parameters, new MLPController(), "MLPController");
        generateCurve(parameters, new MLPControllerPlus(), "MLPControllerPlus");
        //generateCurve(parameters, new PriorityController(), "PriorityController");
        //generateCurve(parameters, new PriorityControllerHP(), "PriorityControllerHP");
        //generateCurve(parameters, new PriorityControllerTLB(), "PriorityControllerTLB");
        generateCurve(parameters, new RMLPController(), "RMLPController");
        generateCurve(parameters, new RMLPControllerPlus(), "RMLPControllerPlus");

    }

    private static void generateCurve(Parameters parameters, Evolvable controllerType, String controllerName)
    {
        int generations = 100;
        double[] learningCurve = new double[generations];
        for(int i = 0; i < learningCurve.length; i++)
            learningCurve[i] = 0;

        int curvesToAverage = 100;

        for(int i = 0; i < curvesToAverage; i++)
        {
            Evolvable controller = controllerType.newInstance();
            ControllerLearner controllerEvolver = new PlayerEvolver(parameters, controller, generations, 20);
            IntegralLearnabilityEvaluator learnability = new IntegralLearnabilityEvaluator();

            learnability.evaluate(controllerEvolver, parameters);
            System.out.println("Evaluation " + i + " complete for " + controllerName);
            double[] tempCurve = learnability.fitnesses;
            for(int j = 0; j < learningCurve.length; j++)
            {
                learningCurve[j] += tempCurve[j];
            }
        }

        for(int i = 0; i < learningCurve.length; i++)
            learningCurve[i] /= curvesToAverage;

        writeToCsv(controllerName + ".csv", learningCurve);
    }

    private static void writeToCsv(String filename, double[] values)
    {
        try
        {
            FileWriter writer = new FileWriter(filename);

            for(int i = 0; i < values.length; i++)
            {
                writer.append(Double.toString(values[i]) + '\n');
            }
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
