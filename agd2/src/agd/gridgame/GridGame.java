package agd.gridgame;

import agd.Util;
import agd.gridgame.GameState.Description;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jun 1, 2008
 * Time: 12:18:17 AM
 */
public class GridGame implements Constants {

    private GameState state = null;
    private GameState previousState = null;
    private final Parameters parameters;


    public GridGame (GameState state, Parameters parameters) {
        // used for mid-game initializaton
        this.state = state;
        this.parameters = parameters;
    }

    public GridGame(Parameters parameters) {
        // create a game (an instance of the game engine) with the specified parameters
        this.parameters = parameters;
        resetGameState ();
    }

    public GameResults play (View view, Controller controller, int delay, String outputFile)
    {
        return play(view, controller, delay, true, outputFile);
    }

    public GameResults play (View view, Controller controller, int delay)
    {
        return play(view, controller, delay, false, "");
    }

    private GameResults play (View view, Controller controller, int delay, boolean outputData, String outputFile) {
        //resetGameState ();
        // save the score at each timestep
        ArrayList<Integer> scores = new ArrayList<Integer>();
        // score starts at zero
        scores.add(0);
        boolean end = false;
        while (!end) {
            if (state != null)  {
                previousState = state.copy();
            }
            GameState.Description description = state.getDescription ();
            //System.out.println("T: " + description.getT ());
            Action action = controller.control (description);
            takePlayerAction (action);
            checkAndHandleAgentCollisions (description);
            moveObjects (description);
            checkAndHandleAllCollisions (description);
            if (view != null) {
                view.setDescription (description);
                view.repaint(1);
            }
            advanceTime ();
            scores.add(description.getScore());
            end = checkIfGameHasEnded (description);
            if (delay > 0) {
                try {
                    Thread.sleep (delay);
                }
                catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        }
        if(outputData)
        {
            Util util = new Util();
            util.writeToCsv(outputFile, scores);
        }
        return calculateResults ();
    }

    private void advanceTime () {
        state.advanceTime ();
    }

    public void resetGameState () {
        state = new GameState (parameters);
    }

    private void takePlayerAction (Action action) {
        state.takePlayerAction (action);
    }


    private boolean checkIfGameHasEnded (Description description) {
//        return (description.getT () > timeLimit) ||
//                (!description.alive () );
         return (description.getT () > timeLimit) ||
                (!description.alive () ||
                description.getScore() >= parameters.getScoreToWin());
    }

    private GameResults calculateResults () {
        GameResults results = new GameResults ();
        results.score = state.getDescription().getScore ();
        results.survived = state.getDescription().alive();
        return results;
    }

    public GameState.Description getStateDescription () {
        return state.getDescription();
    }

    private void moveObjects (Description description) {
        for (int color = 0; color < 3; color++) {
            int logic = parameters.getMovementLogic(color);
            for (int i = 0; i < parameters.getNumberOfThings(color); i++) {
                if (description.thingExists(color, i)) {
                    int[] tpos = description.getThingPosition(color, i);
                    direction dir = description.getThingDirection (color, i);
                    switch (logic){
                        case STILL:
                            break;
                        case RANDOM_SHORT:
                            do {
                                dir = randomDirection ();    
                            } while (!state.moveThing (color, i, dir));
                            break;
                        case RANDOM_LONG:
                            while (!state.moveThing (color, i, dir)) {
                                dir = randomDirection ();
                                state.setThingDirection (color, i, dir);
                            }
                            break;
                        case CLOCKWISE:
                            while (!state.moveThing (color, i, dir)) {
                                dir = direction.values()[(dir.ordinal() + 1) % 4];
                                state.setThingDirection (color, i, dir);
                            }
                            break;
                        case COUNTERCLOCKWISE:
                            while (!state.moveThing (color, i, dir)) {
                                dir = direction.values()[(dir.ordinal() + 3) % 4]; 
                                state.setThingDirection (color, i, dir);
                            }
                            break;
                        case CHASE:
                            dir = description.getDirectionToClosestThing(parameters.getObjective(color),
                                    tpos[X], tpos[Y]);
                            if (dir != null) {
                                state.moveThing(color, i, dir);
                            }
                            break;
                        case FLEE:
                            dir = description.getDirectionToClosestThing(parameters.getObjective(color),
                                    tpos[X], tpos[Y]);
                            if (dir != null) {
                                dir = opposite (dir);
                                state.moveThing(color, i, dir);
                            }
                            break;
                        default:
                            throw new RuntimeException ("Unknown movement logic");
                    }
                    // check if this movement has caused a new collision
                }
            }
        }
    }

    private void checkAndHandleAllCollisions (Description description) {
        for (int color = 0; color < 3; color++) {
            for (int index = 0; index < description.numberOfThings(color); index++) {
                // only check collisions for things that actually exist
                if (description.thingExists(color, index)) {
                    // check for collision with agent
                    // we do it again to make sure that agent and things can't pass through each other
                    // yes, things can still pass through each other when colliding head-on; this is a necessary
                    // tradeoff for computational efficiency, and presumably not exploitable
                    checkAndHandleAgentCollisions (description, color, index);
                    // check for collisions with other things
                    checkAndHandleThingCollisions (description, color, index);
                }
            }
        }
    }

    private void checkAndHandleAgentCollisions (Description description) {
        for (int color = 0; color < 3; color++) {
            for (int index = 0; index < description.numberOfThings(color); index++) {
                // only check collisions for things that actually exist
                if (description.thingExists(color, index)) {
                    checkAndHandleAgentCollisions (description, color, index);
                }
            }
        }
    }

    private void checkAndHandleAgentCollisions (Description description, int color, int index) {
        int[] pos = description.getThingPosition(color, index);
        if (description.agent(pos[0], pos[1])) {
            //System.out.println("  collision: agent and " + colorNames[color] + " " + index);
            // effect for agent
            int effect = parameters.collisionEffectForAgent (color);
            //System.out.print("   effect for agent: ");
            switch (effect) {
                case NONE:
                    //System.out.println("none");
                    break;
                case DEATH:
                    //System.out.println("death");
                    state.die ();
                    break;
                case TELEPORT:
                    //System.out.println("teleport");
                    state.setAgentPosition (description.findFreePosition());
                    break;
                case PUSH:
                    if (previousState != null) {
                        for (int look = 0; look < direction.values().length; look++) {
                            int[] offset = directionOffset[look];
                            int[] lookPos = {pos[X] + offset[X], pos[Y] + offset[Y]};
                            if (couldHaveBeenPushedFrom(lookPos, color)) {
                                int pushDir = (look + 2) % direction.values().length;
                                int[] pushOffset = directionOffset[pushDir];
                                int[] newPos = {pos[X] + pushOffset[X],
                                        pos[Y] + pushOffset[Y]};
                                if (!state.getDescription().wall(newPos[X], newPos[Y])) {
                                    state.setAgentPosition(newPos);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    throw new RuntimeException ("Unknown collision effect");
            }
            changeScore (AGENT, color);
            // effect for thing
            handleCollisionForThings(description, color, index, AGENT);
        }
    }

    private void checkAndHandleThingCollisions (Description description, int color, int index) {
        for (int otherColor = 0; otherColor < 3; otherColor++) {
                for (int otherThing = 0; otherThing < description.numberOfThings (otherColor); otherThing++) {
                    // don't check for collisions with yourself or non-existing things
                    if ((color == otherColor && index == otherThing) ||
                            !description.thingExists(otherColor, otherThing)) {
                        continue;
                    }
                    int[] pos = description.getThingPosition(color, index);
                    int[] otherThingPosition = description.getThingPosition(otherColor, otherThing);
                    if (pos[0] == otherThingPosition[0] && pos[1] == otherThingPosition[1]) {
                        //System.out.println("  collision: " + colorNames[color] + " " + index + " and " +
                        //        colorNames[otherColor] + " " + otherThing);
                        handleCollisionForThings(description, color, index, otherColor);
                        handleCollisionForThings(description, otherColor, otherThing, color);
                    }
                }
            }
    }

    private void handleCollisionForThings(Description description,
                                               int firstColor, int firstThing, int secondColor) {
        //System.out.print("   effect for " + colorNames[firstColor] + " " + firstThing +": ");
        int effect = parameters.collisionEffectForThing(firstColor, secondColor);
        switch (effect) {
                case NONE:
                    //System.out.println("none");
                    break;
                case DEATH:
                    //System.out.println("death");
                    state.killThing (firstColor, firstThing);
                    break;
                case TELEPORT:
                    //System.out.println("teleport");
                    state.setThingPosition (firstColor, firstThing, description.findFreePosition());
                    break;
                case PUSH:
                    // the meat!
                    // the approach: find a direction where last step, there was a thing of secondcolor
                    // but this step there is none
                    // and move the first thing there     
                    if (previousState != null) {
                        int[] collisionPosition = state.getDescription().getThingPosition(firstColor, firstThing);
                        for (int look = 0; look < direction.values().length; look++) {
                            int[] offset = directionOffset[look];
                            int[] lookPos = {collisionPosition[X] + offset[X], collisionPosition[Y] + offset[Y]};
                            if (couldHaveBeenPushedFrom(lookPos, secondColor)) {
                                int pushDir = (look + 2) % direction.values().length;
                                int[] pushOffset = directionOffset[pushDir];
                                int[] newPos = {collisionPosition[X] + pushOffset[X],
                                        collisionPosition[Y] + pushOffset[Y]};
                                if (!state.getDescription().wall(newPos[X], newPos[Y])) {
                                    state.setThingPosition(firstColor, firstThing, newPos);
                                    break;
                                }
                            }
                        }


                    }

                    break;
                default:
                    throw new RuntimeException ("Unknown collision effect");
        }
        changeScore (firstColor, secondColor);
    }

    private boolean couldHaveBeenPushedFrom (int[] pos, int color) {
        // if there was something of this color at this position last timestep, but not the current timestep.
        // this is where the push could have originated from
        if (color == AGENT) {
            return previousState.getDescription().agentThere (pos[X], pos[Y]) &&
                    !state.getDescription().agentThere (pos[X], pos[Y]);
        }
        return previousState.getDescription().thingThere(color, pos[X], pos[Y]) &&
                !state.getDescription().thingThere(color, pos[X], pos[Y]);
    }

    private void changeScore (int firstColor, int secondColor) {
        int scoreEffect = parameters.scoreEffectForThing(firstColor, secondColor);
        //System.out.println("    score effect: " + scoreEffect);
        state.changeScore (scoreEffect);
    }

    public static direction randomDirection () {
        double outcome = Math.random ();
        if (outcome < 0.25) return direction.down;
        if (outcome < 0.5) return direction.up;
        if (outcome < 0.75) return direction.left;
        return direction.right;
    }

    public static direction opposite (direction dir) {
        if (dir == direction.right) return direction.left;
        if (dir == direction.left) return direction.right;
        if (dir == direction.up) return direction.down;
        if (dir == direction.down) return direction.up;
        throw new RuntimeException ("Unknown direction: " + dir);
    }

    public static void move (int[] pos, direction dir) {
        switch (dir) {
            case up:
                pos[1]--;
                break;
            case down:
                pos[1]++;
                break;
            case left:
                pos[0]--;
                break;
            case right:
                pos[0]++;
                break;
        }
    }

    public GridGame copy () {
        return new GridGame (state.copy (), parameters);
    }

    public int getScoreToWin () {
        return parameters.getScoreToWin();
    }
}
