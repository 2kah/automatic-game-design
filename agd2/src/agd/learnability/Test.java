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

        int curvesToAverage = 10;
        double[][] curves = new double[curvesToAverage][generations];

        for(int i = 0; i < curvesToAverage; i++)
        {
            Evolvable controller = controllerType.newInstance();
            ControllerLearner controllerEvolver = new PlayerEvolver(parameters, controller, generations, 20);
            IntegralLearnabilityEvaluator learnability = new IntegralLearnabilityEvaluator();

            learnability.evaluate(controllerEvolver, parameters);
            System.out.println("Evaluation " + i + " complete for " + controllerName);
            curves[i] = learnability.fitnesses;
        }

        double[] averageCurve = new double[generations];
        // initialise to 0
        for(int i = 0; i < averageCurve.length; i++)
            averageCurve[i] = 0;

        // calculate the average learning curve
        for(int i = 0; i < averageCurve.length; i++)
        {
            for(int j = 0; j < curvesToAverage; j++)
                averageCurve[i] += curves[j][i];

            averageCurve[i] /= curvesToAverage;
        }

        double[] maxDifferences = new double[curvesToAverage];
        for(int i = 0; i < maxDifferences.length; i++)
            maxDifferences[i] = 0;

        // find the learning curve with the greatest step
        for(int i = 0; i < curvesToAverage; i++)
        {
            for(int j = 0; j < generations-1; j++)
            {
                double difference = curves[i][j] - curves[i][j+1];
                if(difference > maxDifferences[i])
                    maxDifferences[i] = difference;
            }
        }
        int mostInterestingCurve = 0;
        double maxDifference = 0;
        for(int i = 0; i < maxDifferences.length; i++)
        {
            if(maxDifferences[i] > maxDifference)
            {
                maxDifference = maxDifferences[i];
                mostInterestingCurve = i;
            }
        }

        writeToCsv(controllerName + ".csv", averageCurve);
        writeToCsv(controllerName + "MostInteresting.csv", curves[mostInterestingCurve]);
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
