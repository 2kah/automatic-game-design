package agd.controllers;

import agd.gridgame.*;
import agd.evolution.Evolvable;
import agd.evolution.RMLP;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 15, 2008
 * Time: 7:23:53 PM
 */
public class RMLPController implements Evolvable, Controller, Constants {

    private final RMLP rmlp;
    private final int[][] inputArea =
            {{-2, 0}, {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -2}, {0, -1}, {0, 1}, {0, 2},
                    {1, -1}, {1, 0}, {1, 1}, {2, 0}};
    private final int numberOfInputs = inputArea.length * 4;

    public RMLPController () {
        rmlp = new RMLP (numberOfInputs + 1, 6, 4);
    }

    public RMLPController (RMLP rmlp) {
        this.rmlp = rmlp;
    }

    public Evolvable copy() {
        return new RMLPController (rmlp.copy ());
    }

    public void mutate() {
        rmlp.mutate ();
    }

    public Evolvable newInstance() {
        return new RMLPController ();
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
        inputs[numberOfInputs] = 1; //bias
        double[] outputs = rmlp.propagate(inputs);
        int whichDirection = indexOfHighest (outputs);
        Action action = new Action ();
        action.movement = Constants.direction.values()[whichDirection];
        return action;
    }

    public void reset() {
        rmlp.reset ();
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
