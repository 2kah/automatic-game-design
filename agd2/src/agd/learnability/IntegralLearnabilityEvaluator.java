package agd.learnability;

import agd.controllers.RMLPControllerPlus;
import agd.evolution.ControllerLearner;
import agd.evolution.Evolvable;
import agd.evolution.PlayerEvolver;
import agd.gridgame.Controller;
import agd.gridgame.Parameters;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Feb 19, 2009
 * Time: 10:04:59 AM
 */
public class IntegralLearnabilityEvaluator implements LearnabilityEvaluator {

    final int trialsPerSample = 5;
    final int populationSize = 100;
    final int generations = 100;

    public double[] evaluate(ControllerLearner learner, Parameters parameters) {
        // idea: divide the t-value (should be high) with the average deviation from the regressed straight line (should be low)
        double[] fitnesses = new double[generations]; // best fitness of each generation
        double[] standardDeviations = new double[generations]; // sd of best individual of each generation

        // first, get the learning curve as usual
        runLearningAlgorithm (learner, parameters, fitnesses, standardDeviations);

        // plot learning curve
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("learningCurve");
        for (int i = 0; i < fitnesses.length; i++)
            series.add(i, fitnesses[i]);
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createScatterPlot("Learning curve", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartFrame frame = new ChartFrame("First", chart);
        frame.pack();
        frame.setVisible(true);


        // then, do linear regression on this (for now, just fit the first and last?)
        // y = kx + m
        double[] lineParams = linearRegression (fitnesses);
        double k = lineParams[0];
        double m = lineParams[1];
        
        // then, calculate average deviation from this line
        double sumOfDeviations = 0;
        for (int generation = 0; generation < generations; generation++) {
            double idealFitness = k * generation + m;
            double deviation = Math.abs (idealFitness - fitnesses[generation]);
            sumOfDeviations += deviation;
        }
        double averageDeviation = sumOfDeviations / generations;
        return new double[]{averageDeviation};
    }

    public double[] evaluate (Parameters parameters) {
        Evolvable initial = new RMLPControllerPlus();       
        return evaluate (new PlayerEvolver(parameters, initial), parameters);
    }

    private void runLearningAlgorithm (ControllerLearner learner, Parameters parameters,
                                       double[] fitnesses, double[] standardDeviations) {
        System.out.println("Running learning algorithm...");
        Controller initial = new RMLPControllerPlus();
        learner.init(parameters, initial, populationSize);
        learner.setEvaluationRepetitions(trialsPerSample);

        // run the learning algorithm
        for (int generation = 0; generation < generations; generation++) {
            learner.oneMoreGeneration ();
            fitnesses[generation] = learner.getBestFitness();
            standardDeviations[generation] = learner.getBestFitnessSD();
            System.out.println("Generation " + generation + " fitness " + fitnesses[generation] +
                    " sd " + standardDeviations[generation]);
        }
    }

    private double[] linearRegression (double[] series) {
        double m = series[0];
        double k = (series[series.length - 1] - series[0]) / series.length;        
        return new double[]{k, m};
    }


}
