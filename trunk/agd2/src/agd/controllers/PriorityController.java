package agd.controllers;

import agd.gridgame.*;
import agd.evolution.Evolvable;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Nov 13, 2009
 * Time: 4:58:17 PM
 */
public class PriorityController implements Evolvable, Controller, Constants {
    private boolean fitnessKnown = false;

    private Action action = new Action ();
    private direction movement = GridGame.randomDirection();

    final int FALSE = 0;
    final int WALL_IN_FRONT_1 = 1;
    final int WALL_IN_FRONT_2 = 2;
    final int THING_IN_FRONT_1 = 3;
    final int THING_IN_FRONT_AT_ALL = 4;
    final int THING_IN_VICINITY_1 = 5;
    final int THING_IN_VICINITY_2 = 6;
    final int NUMBER_OF_CONDITION_TYPES = 7;

    public final String[] conditionNames = {"false", "wall in front (1)", "wall in front (2)",
            "thing in front (1)", "thing in front (at all)", "thing in vicinity (1)", "thing in vicinity (2)"};
    public final boolean[] conditionHasTarget = {false, false, false, true, true, true, true};

    final int NUMBER_OF_LAYERS=5;
    protected int startingMovementLogic;
    protected int startingMovementTarget;
    protected int[] conditions = new int[NUMBER_OF_LAYERS];
    protected int[] conditionTargets = new int[NUMBER_OF_LAYERS];
    protected int[] movementLogics = new int[NUMBER_OF_LAYERS];
    protected int[] movementTargets = new int[NUMBER_OF_LAYERS];

    public boolean getFitnessKnown()
    {
        return this.fitnessKnown;
    }

    public void setFitnessKnown(boolean known)
    {
        this.fitnessKnown = known;
    }

    public PriorityController () {
        startingMovementLogic = (int) (Math.random () * NUMBER_OF_MOVEMENT_TYPES);
        startingMovementTarget = (int) (Math.random () * (NUMBER_OF_COLORS - 1));
        for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
            conditions[i] = (int) (Math.random () * NUMBER_OF_CONDITION_TYPES);
            conditionTargets[i] = (int) (Math.random () * (NUMBER_OF_COLORS - 1));
            movementLogics[i] = (int) (Math.random () * NUMBER_OF_MOVEMENT_TYPES);
            movementTargets[i] = (int) (Math.random () * (NUMBER_OF_COLORS - 1));
        }
    }



    public Evolvable copy() {
        PriorityController copy = new PriorityController ();
        System.arraycopy (conditions, 0, copy.conditions, 0, conditions.length);
        System.arraycopy (conditionTargets, 0, copy.conditionTargets, 0, conditionTargets.length);
        System.arraycopy (movementLogics, 0, copy.movementLogics, 0, movementLogics.length);
        System.arraycopy (movementTargets, 0, copy.movementTargets, 0, movementTargets.length);
        return copy;
    }

    public void mutate() {
        mutateAtomically ();
        while (Math.random () > 0.5) {
            mutateAtomically ();
        }
        this.fitnessKnown = false;
    }

    private void mutateAtomically () {
        int what = (int) (Math.random () * 5);
        int where = (int) (Math.random () * NUMBER_OF_LAYERS);
        switch (what) {
            case 0:
                if (where > NUMBER_OF_LAYERS / 2) {
                    startingMovementLogic = (int) (Math.random () * NUMBER_OF_MOVEMENT_TYPES);
                }
                else {
                    startingMovementTarget = (int) (Math.random () * (NUMBER_OF_COLORS - 1));
                }
                break;
            case 1:
                conditions[where] = (int) (Math.random () * NUMBER_OF_CONDITION_TYPES);
                break;
            case 2:
                conditionTargets[where] = (int) (Math.random () * (NUMBER_OF_COLORS - 1));
                break;
            case 3:
                movementLogics[where] = (int) (Math.random () * NUMBER_OF_MOVEMENT_TYPES);
                break;
            case 4:
                movementTargets[where] = (int) (Math.random () * (NUMBER_OF_COLORS - 1));
                break;
        }
    }

    protected void wipe () {
        startingMovementLogic = STILL;
        startingMovementTarget = RED;
        for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
            conditions[i] = FALSE;
            conditionTargets[i] = RED;
            movementLogics[i] = STILL;
            movementTargets[i] = RED;
        }
        this.fitnessKnown = false;
    }

    public Evolvable newInstance() {
        return new PriorityController ();
    }

    public Action control(GameState.Description state) {
        // first, set the movement to the result of the default logic
        movement = executeMovementLogic (startingMovementLogic, startingMovementTarget, movement, state);
        //System.out.println("Direction: " + movement);
        // then, step through the logics; for each, if the condition is fulfilled,
        // set movement to the resulting direction
        for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
            //System.out.println("Checking condition " + i + ": if " + conditionNames[conditions[i]] +
                        //(conditionHasTarget[conditions[i]] ? " (" + colorNames[conditionTargets[i]] + ")" : ""));
            if (checkCondition (conditions[i], conditionTargets[i], state, movement)) {
                //System.out.println("True. Execute " + movementTypeNames[movementLogics[i]] +
                        //(movementTypeHasTarget[movementLogics[i]] ? " (" + colorNames[movementTargets[i]] + ")" : ""));
                movement = executeMovementLogic (movementLogics[i], movementTargets[i], movement, state);
                //System.out.println("New direction: " + movement);
            }
        }
        action.movement = movement;
        return action;
    }

    private direction executeMovementLogic (int logic, int target, direction current, GameState.Description state) {
        int[] pos = state.getAgentPosition();
        int[] look;
        int[] offset = directionOffset[current.ordinal()];
        direction dir;
        switch (logic) {
            case STILL:
                // an agent can't be still. so we just have to return the current direction
                return current;
            case RANDOM_SHORT:
                return GridGame.randomDirection();
            case RANDOM_LONG:
                look = new int[]{pos[X] + offset[X], pos[Y] + offset[Y]};
                while (state.wall (look[X], look[Y])) {
                    current = GridGame.randomDirection();
                    offset = directionOffset[current.ordinal()];
                    look = new int[]{pos[X] + offset[X], pos[Y] + offset[Y]};
                }
                return current;
            case CLOCKWISE:

                current = direction.values()[(current.ordinal() + 1) % 4];
                return current;
            case COUNTERCLOCKWISE:
                current = direction.values()[(current.ordinal() + 3) % 4];
                return current;
            case CHASE:
                dir = state.getDirectionToClosestThing (target, pos[X], pos[Y]);
                if (dir != null)
                    return dir;
                else
                    return current;
            case FLEE:
                dir = state.getDirectionToClosestThing (target, pos[X], pos[Y]);
                if (dir != null)
                    return GridGame.opposite(dir);
                else
                    return current;
            default:
                throw new RuntimeException ("Unknown movement logic: " + logic);
        }
    }

    private boolean checkCondition (int condition, int conditionTarget, GameState.Description state, direction current) {
        int[] pos = state.getAgentPosition();
        int[] look;
        int[] offset = directionOffset[current.ordinal()];
        switch (condition) {
            case FALSE:
                return false;
            case WALL_IN_FRONT_1:
                look = new int[]{pos[X] + offset[X], pos[Y] + offset[Y]};

                return state.wall(look[X], look[Y]);
            case WALL_IN_FRONT_2:
                look = new int[]{pos[X] + offset[X] + offset[X], pos[Y] + offset[Y] + offset[Y]};
                return state.wall(look[X], look[Y]);
            case THING_IN_FRONT_1:
                look = new int[]{pos[X] + offset[X], pos[Y] + offset[Y]};
                //System.out.println("pos " + pos[X] + " " + pos[Y] + " look " + look[X] + " " + look[Y]);
                return state.thingThere (conditionTarget, look[X], look[Y]);
            case THING_IN_FRONT_AT_ALL:
                look = new int[]{pos[X], pos[Y]};
                while (true) {
                    look[X] += offset[X];
                    look[Y] += offset[Y];
                    if (state.wall(look[X], look[Y]))
                        return false; // we will find a wall at some point
                    if (state.thingThere (conditionTarget, look[X], look[Y]))
                        return true;
                }
            case THING_IN_VICINITY_1:
                return state.getDistanceToClosestThing (conditionTarget) < 2;
            case THING_IN_VICINITY_2:
                return state.getDistanceToClosestThing (conditionTarget) < 3;
            default:
                throw new RuntimeException ("Unknown condition: " + condition);
        }
    }

    public void reset() {
        movement = GridGame.randomDirection();
        this.fitnessKnown = false;
    }

    public String toString () {
        StringBuffer sb = new StringBuffer ();
        sb.append ("Priority controller:\n" +
                "Base: " + movementTypeNames[startingMovementLogic] +
                (movementTypeHasTarget[startingMovementLogic] ? (" " + colorNames[startingMovementTarget]) : ""));
        for (int i = 0; i < NUMBER_OF_LAYERS; i++) {
            if (conditions[i] != FALSE) {
                sb.append ("\n" + i + ": if " + conditionNames[conditions[i]] +
                        (conditionHasTarget[conditions[i]] ? " (" + colorNames[conditionTargets[i]] + ")" : "") +
                    " then " + movementTypeNames[movementLogics[i]] +
                        (movementTypeHasTarget[movementLogics[i]] ? " (" + colorNames[movementTargets[i]] + ")" : ""));
            }
        }
        return sb.toString ();
    }
}
