package agd.test;

import agd.evolution.LearningCurveParameterEvaluator;
import agd.gridgame.Parameters;
import wox.serial.Easy;

/**
 * Created with IntelliJ IDEA.
 * User: 2kah
 * Date: 09/05/13
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class FitnessForGame {

    public static void main(String[] args) throws Exception
    {
        Parameters parameters = null;
        String paramName = "";
        paramName = args[0];
        try {
            parameters = (Parameters) Class.forName (paramName).newInstance ();
        }
        catch (ClassNotFoundException e) {
            System.out.println (paramName + " is not a class name; trying to load a wox definition with that name.");
            parameters = (Parameters) Easy.load(paramName);
        }

        LearningCurveParameterEvaluator evaluator = new LearningCurveParameterEvaluator();

        int fitnessesToAverage = 50;

        double[] fitness = new double[fitnessesToAverage];
        for(int i = 0; i < fitnessesToAverage; i++)
        {
            fitness[i] = evaluator.evaluate(parameters)[0];
        }

        double average = 0;
        for(int i = 0; i < fitnessesToAverage; i++)
        {
            average += fitness[i];
        }

        average /= fitnessesToAverage;

        System.out.println("Fitness for game " + paramName + " = " + average);
    }
}
