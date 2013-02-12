package agd.evolution;

import agd.gridgame.Parameters;
import agd.gridgame.Controller;
import agd.gridgame.games.Original;
import agd.controllers.SelectiveRMLPController;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Nov 24, 2008
 * Time: 6:29:08 PM
 */
public class MemeticClimber {

    final int memeticSteps = 50;
    final int evaluationRepetitions = 20;
    private HierarchicallyMutable best;
    private double bestFitness = Double.NEGATIVE_INFINITY;
    final private GridGameEvaluator evaluator;
    final private Parameters parameters;

    public MemeticClimber (Parameters parameters, Evolvable controller) {
        this.parameters = parameters;
        evaluator = new GridGameEvaluator (evaluationRepetitions);
        this.best = (HierarchicallyMutable) controller;
    }

    public void oneMoreGeneration () {
        bestFitness= evaluator.evaluate (parameters, (Controller) best)[0];
        HierarchicallyMutable contender = best.copy ();
        contender.mutate (1);
        double contenderFitness = 0;//= evaluator.evaluate(parameters, (Controller) contender);
        for (int step = 0; step < memeticSteps; step++) {
            contenderFitness = evaluator.evaluate(parameters, (Controller) contender)[0];
            HierarchicallyMutable subContender = contender.copy ();
            subContender.mutate (0);
            double subContenderFitness = evaluator.evaluate(parameters, (Controller) subContender)[0];
            if (subContenderFitness >= contenderFitness) {
                contender = subContender;
                contenderFitness = subContenderFitness;
                System.out.print(":");
            }
            else {
                System.out.print(".");
            }
        }
        if (contenderFitness >= bestFitness) {
            best = contender;
            bestFitness = contenderFitness;
        }
    }

    public static void main(String[] args) {
        Parameters parameters = new Original();
        MemeticClimber mc = new MemeticClimber (parameters, new SelectiveRMLPController());
        for (int i = 0; i < 100; i++) {
            mc.oneMoreGeneration();
            System.out.println("Generation " + i + " best " + mc.bestFitness);
        }
        System.out.println(mc.best);
    }

}
