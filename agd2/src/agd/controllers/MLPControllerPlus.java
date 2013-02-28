package agd.controllers;

import agd.gridgame.Controller;
import agd.gridgame.Constants;
import agd.gridgame.Action;
import agd.gridgame.GameState;
import agd.evolution.Evolvable;
import agd.evolution.MLP;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 16, 2008
 * Time: 8:24:38 PM
 */
public class MLPControllerPlus implements Evolvable, Controller, Constants {

    private final MLP mlp;
    private final int[][] inputArea =
            {{-2, 0}, {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -2}, {0, -1}, {0, 1}, {0, 2},
                    {1, -1}, {1, 0}, {1, 1}, {2, 0}};
    private final int numberOfInputs = inputArea.length * 4 + direction.values().length * 3;

    public MLPControllerPlus () {
        mlp = new MLP (numberOfInputs + 1, 10, 4);
    }

    public MLPControllerPlus (MLP mlp) {
        this.mlp = mlp;
    }

    public Evolvable copy() {
        return new MLPControllerPlus (mlp.copy ());
    }

    public void mutate() {
        mlp.mutate ();
    }

    public Evolvable newInstance() {
        return new MLPControllerPlus ();
    }

    public Action control(GameState.Description state) {
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
        for (int color = 0; color < 3; color++) {
            direction nearest = state.getDirectionToClosestThing(color);
            if (nearest != null) {
                inputs[area * 4 + color * 4 + nearest.ordinal()] = 1;
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
