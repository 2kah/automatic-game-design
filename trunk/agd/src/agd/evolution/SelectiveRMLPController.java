package agd.evolution;

import agd.gridgame.Controller;
import agd.gridgame.Constants;
import agd.gridgame.Action;
import agd.gridgame.GameState;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Nov 21, 2008
 * Time: 7:42:34 PM
 */
public class SelectiveRMLPController implements HierarchicallyMutable, Controller, Constants {

    private final RMLP rmlp;
    private final int numberOfInputs = 10;
    private final int numberOfHiddenNeurons = 20;
    private final Input[] inputs;

    public SelectiveRMLPController () {
        inputs = new Input[numberOfInputs];
        for (int i = 0; i < numberOfInputs; i++) {
            inputs[i] = new Input ();
        }
        rmlp = new RMLP (numberOfInputs + 1, numberOfHiddenNeurons, 4);
    }

    public SelectiveRMLPController (RMLP rmlp, Input[] inputs) {
        this.rmlp = rmlp;
        this.inputs = inputs;
    }

    public void mutate(int level) {
        switch (level) {
            case 0:
                rmlp.mutate();
                break;
            case 1:
                do {
                    inputs[(int) (Math.random () * numberOfInputs)] = new Input ();    
                } while (Math.random () < 0.5);
                break;
            default:
                throw new RuntimeException ("Unknown mutation level");
        }
    }

    public HierarchicallyMutable copy() {
        Input[] newInputs = new Input[numberOfInputs];
        for (int i = 0; i < numberOfInputs; i++) {
            newInputs[i] = inputs[i].copy ();
        }
        return new SelectiveRMLPController (rmlp.copy (), newInputs);
    }

    public HierarchicallyMutable getNewInstance() {
        return new SelectiveRMLPController ();
    }

    public int countNumberOfActiveConnections() {
        return 0; // not applicable here
    }

    public void mutate() {
        mutate (0);
    }

    public Action control(GameState.Description state) {
        double[] inputarray = new double[numberOfInputs + 1];
        for (int i = 0; i < numberOfInputs; i++) {
            inputarray[i] = inputs[i].value(state);
            //System.out.println(inputs[i] + " yields " + inputarray[i]);
        }
        double[] outputs = rmlp.propagate(inputarray);
        int whichDirection = indexOfHighest (outputs);
        Action action = new Action ();
        action.movement = Constants.direction.values()[whichDirection];
        return action;
    }

    public void reset() {
        rmlp.reset ();
    }

    public int indexOfHighest (double[] array) { // could use a softmax here...?
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

    class Input {

        // types
        final int PROBE = 0;     // is there a thing of color c at position p?
        final int DISTANCE = 1;  // what's the distance to the closest thing of color c?
        final int DIRECTION = 2; // is the closest thing of color p in direction d?
        final String[] typeNames = {"probe", "distance", "direction"};
        // "retina size"
        final int maxProbeDistance = 3;

        private int type;
        private int color;       // what are we looking for?
        private int x;           // where?
        private int y;
        private int whichdirection;   // not an enum because of serialization issues

        public Input (int type, int color, int whichdirection, int x, int y) {
            this.type = type;
            this.color = color;
            this.whichdirection = whichdirection;
            this.x = x;
            this.y = y;
        }

        public Input () {
            type = (int) (Math.random () * 3);
            color = (int) (Math.random () * 3);
            whichdirection = (int) (Math.random () * 4);
            x = (int) (Math.random () * maxProbeDistance * 2) - maxProbeDistance;
            y = (int) (Math.random () * maxProbeDistance * 2) - maxProbeDistance;

        }

        private double value (GameState.Description state) {
            switch (type) {
                case (PROBE):
                    int[] pos = state.getAgentPosition();
                    return state.thingThere(color, pos[0] + x, pos[1] + y) ? 1 : 0;
                case (DISTANCE):
                    int[] closest = state.getClosestThingPosition(color);
                    if (closest == null) {
                        return 1;
                    }
                    return state.distanceToAgent(closest[0], closest[1]) / (double) state.arenaHeight();
                case (DIRECTION):
                    direction dir = state.getDirectionToClosestThing(color);
                    return (dir == direction.values()[whichdirection]) ? 1 : 0;
            }
            throw new RuntimeException ("Unknown type");
        }

        private Input copy () {
            return new Input (type, color, whichdirection, x, y);
        }

        public String toString () {
            return typeNames[type] + " " + colorNames[color] + " " + x + " " + y + " " + direction.values()[whichdirection];
        }

    }

    public String toString () {
        StringBuffer sb = new StringBuffer ("SelectiveRMLPController, " + numberOfInputs + " inputs, " +
                numberOfHiddenNeurons + " hiddens\n");
        for (int i = 0; i < inputs.length; i++) {
            sb.append("input" + i + ": " + inputs[i] + "\n");
        }
        return sb.toString ();
    }

}
