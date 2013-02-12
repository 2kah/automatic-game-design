package agd.controllers;

/**
 * Created by IntelliJ IDEA.
 * User: juto
 * Date: Nov 16, 2009
 * Time: 5:31:06 PM
 */
public class PriorityControllerHP extends PriorityController {

    public PriorityControllerHP() {
        super ();
        wipe ();

        startingMovementLogic = FLEE;
        startingMovementTarget = RED;

        // in front of a hyena hunter? then go for it!
        conditions[0] = THING_IN_FRONT_AT_ALL;
        conditionTargets[0] = BLUE;
        movementLogics[0] = CHASE;
        movementTargets[0] = BLUE;

        // but if the hyenas are to close, maybe better to run from them after all...
        conditions[1] = THING_IN_VICINITY_2;
        conditionTargets[1] = RED;
        movementLogics[1] = FLEE;
        movementTargets[1] = RED;

        // close to a hyena trap? even better, go for it!
        conditions[2] = THING_IN_VICINITY_2;
        conditionTargets[2] = GREEN;
        movementLogics[2] = CHASE;
        movementTargets[2] = GREEN;

        // hit a wall? don't just stand there, turn counterclockwise
        // higher probability of running into a hyena hunter that way

        conditions[3] = WALL_IN_FRONT_1;
        movementLogics[3] = COUNTERCLOCKWISE;

    }

}
