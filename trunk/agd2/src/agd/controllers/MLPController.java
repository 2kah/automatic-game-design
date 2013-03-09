package agd.controllers;

import agd.gridgame.*;
import agd.gridgame.GameState.Description;
import agd.evolution.Evolvable;
import agd.evolution.MLP;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 14, 2008
 * Time: 8:42:06 PM
 */
public class MLPController implements Evolvable, Controller, Constants {
    private boolean fitnessKnown = false;

    private final MLP mlp;
    private final int[][] inputArea =
            {{-2, 0}, {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -2}, {0, -1}, {0, 1}, {0, 2},
                    {1, -1}, {1, 0}, {1, 1}, {2, 0}};
    private final int numberOfInputs = inputArea.length * 4;

    public boolean getFitnessKnown()
    {
        return this.fitnessKnown;
    }

    public void setFitnessKnown(boolean known)
    {
        this.fitnessKnown = known;
    }

    public MLPController () {
        mlp = new MLP (numberOfInputs + 1, 6, 4);
    }

    public MLPController (MLP mlp) {
        this.mlp = mlp;
    }

    public Evolvable copy() {
        return new MLPController (mlp.copy ());
    }

    public void mutate() {
        mlp.mutate ();
        this.fitnessKnown = false;
    }

    public Evolvable newInstance() {
        return new MLPController ();
    }

    public Action control(Description state) {
        final int area = inputArea.length;
        double[] inputs = new double[numberOfInputs + 1];
        for (int i = 0; i < area; i++) {
            int[] probe = state.getAgentPosition();
            probe[X] += inputArea[i][X];
            probe[Y] += inputArea[i][Y];
            if (state.withinBounds (probe[X], probe[Y])) {
                if (state.wall(probe[X], probe[Y]))
                    inputs[i] = 1;
                if (state.thingThere(RED, probe[X], probe[Y]))
                    inputs[i + area] = 1;
                if (state.thingThere(GREEN, probe[X], probe[Y]))
                    inputs[i + area * 2] = 1;
                if (state.thingThere(BLUE, probe[X], probe[Y]))
                    inputs[i + area * 3] = 1;
            }
        }
        inputs[numberOfInputs] = 1; //bias
        double[] outputs = mlp.propagate(inputs);
        int whichDirection = indexOfHighest (outputs);
        Action action = new Action ();
        action.movement = direction.values()[whichDirection];
        return action;
    }

    public void reset() {
        mlp.reset ();
        this.fitnessKnown = false;
    }

    public int indexOfHighest (double[] array) {
        int highest = 0;
        double highestValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > highestValue) {
                highestValue = array[i];
                highest = i;
            }
        }
        return highest;
    }
}
