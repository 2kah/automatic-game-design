package agd.controllers;

/**
 * Created by IntelliJ IDEA.
 * User: juto
 * Date: Nov 18, 2009
 * Time: 3:53:04 PM
 */
public class PriorityControllerTLB extends PriorityController {

    public PriorityControllerTLB () {
        super ();
        wipe ();

        startingMovementLogic = CHASE;
        startingMovementTarget = GREEN;

        conditions[0] = THING_IN_FRONT_1;
        conditionTargets[0] = RED;
        movementLogics[0] = CLOCKWISE;

        conditions[1] = THING_IN_FRONT_1;
        conditionTargets[1] = RED;
        movementLogics[1] = CLOCKWISE;

        conditions[2] = WALL_IN_FRONT_1;
        movementLogics[2] = RANDOM_SHORT;

        conditions[3] = WALL_IN_FRONT_1;
        movementLogics[3] = CLOCKWISE;

    }

}
