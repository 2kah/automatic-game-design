package agd.gridgame;

import agd.evolution.Evolvable;
import agd.evolution.Recombinable;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jun 1, 2008
 * Time: 12:18:57 AM
 */
public class Parameters implements Constants, Evolvable, Recombinable {

    // constants denoting indexes into the integer parameter array
    public static final int TIMELIMIT = 0;
    public static final int NUMBER_OF_RED_THINGS = 1;
    public static final int NUMBER_OF_GREEN_THINGS = 2;
    public static final int NUMBER_OF_BLUE_THINGS = 3;
    public static final int RED_MOVEMENT_LOGIC = 4;
    public static final int GREEN_MOVEMENT_LOGIC = 5;
    public static final int BLUE_MOVEMENT_LOGIC = 6;
    public static final int SCORELIMIT = 7;
    // min and max values for each int parameter
    public final int[] minIntParams = {10, 0, 0, 0, 0, 0, 0, 1};
    public final int[] maxIntParams = {100, 20, 20, 20,
            NUMBER_OF_MOVEMENT_TYPES - 1, NUMBER_OF_MOVEMENT_TYPES - 1, NUMBER_OF_MOVEMENT_TYPES - 1, 10};

    // the actual arrays that denote position in rule and parameter space
    public int[] intParams = new int[8];
    // read like this: when two things or a thing and an agent collides,
    // the effect on the first of these is found as collisionEffects[first][second]
    // indexing is: 0-2 color of thing, 3 agent
    public int[][] collisionEffects = new int[4][4];
    public int[][] collisionScoreEffects = new int[4][4];

    public int getTimeLimit () {
        return intParams[TIMELIMIT];
    }

    public int getNumberOfThings(int color) {
        return intParams[NUMBER_OF_RED_THINGS + color];
    }

    public int getMovementLogic (int color) {
        return intParams[RED_MOVEMENT_LOGIC + color];
    }

    public int collisionEffectForAgent (int color) {
        return collisionEffects[AGENT][color];
    }

    public int collisionEffectForThing (int firstThingColor, int secondThingColor) {
        return collisionEffects[firstThingColor][secondThingColor];
    }

    public int scoreEffectForThing (int firstThingColor, int secondThingColor) {
        return collisionScoreEffects[firstThingColor][secondThingColor];
    }

    public int getScoreToWin () {
        return intParams[SCORELIMIT];
    }

    public Parameters copy() {
        Parameters other = new Parameters ();
        System.arraycopy(this.intParams, 0, other.intParams, 0, this.intParams.length);
        for (int i = 0; i < 4; i++) {
            System.arraycopy(this.collisionEffects[i], 0, other.collisionEffects[i], 0,
                    this.collisionEffects[i].length);
            System.arraycopy(this.collisionScoreEffects[i], 0, other.collisionScoreEffects[i], 0,
                    this.collisionScoreEffects[i].length);
        }
        return other;
    }


    public Recombinable recombine(Recombinable otherParent) {
        Parameters other = (Parameters) otherParent;
        Parameters offspring = new Parameters ();
        for (int i = 0; i < intParams.length; i++) {
            offspring.intParams[i] = Math.random () < 0.5 ? this.intParams[i] : other.intParams[i];
        }
        for (int i = 0; i < collisionEffects.length; i++) {
            for (int j = 0; j < collisionEffects[i].length; j++) {
                offspring.collisionEffects[i][j] = (Math.random () < 0.5) ?
                        this.collisionEffects[i][j] : other.collisionEffects[i][j];
                offspring.collisionScoreEffects[i][j] = (Math.random () < 0.5) ?
                        this.collisionScoreEffects[i][j] : other.collisionScoreEffects[i][j];
            }
        }
        return offspring;
    }


    public void mutate() {
        changeOneThing ();
        while (Math.random () < 0.8) {
            changeOneThing ();
        }
    }

    private void changeOneThing () {
        int what = (int) (Math.random () * 3);
        switch (what) {
            case 0:
                changeIntParam ();
                break;
            case 1:
                changeCollisionEffect ();
                break;
            case 2:
                changeScoreEffect ();
                break;
        }
    }

    private void changeIntParam () {
        int which = (int) (Math.random () * intParams.length);
        changeIntParam(which);
    }

    private void changeIntParam (int which) {
        intParams[which] = minIntParams[which] + (int) (Math.random () * (maxIntParams[which] - minIntParams[which] + 1));

    }

    private void changeCollisionEffect () {
        int i = (int) (Math.random () * collisionEffects.length);
        int j = (int) (Math.random () * collisionEffects[i].length);
        changeCollisionEffect (i, j);
    }

    private void changeCollisionEffect (int i, int j) {
        if (i == j) {
            collisionEffects[i][j] = NONE;
        }
        else {
            collisionEffects[i][j] = (int) (Math.random () * NUMBER_Of_COLLISION_EFFECTS);
        }
    }

    private void changeScoreEffect () {
        int i = (int) (Math.random () * collisionScoreEffects.length);
        int j = (int) (Math.random () * collisionScoreEffects[i].length);
        changeScoreEffect (i, j);
    }


    private void changeScoreEffect (int i, int j) {
        if (i == j) {
            collisionScoreEffects[i][j] = 0;
        }
        else {
            collisionScoreEffects[i][j] = -1 + (int) (Math.random () * 3);
        }
    }

    public static Parameters createRandomParameters () {
        Parameters parameters = new Parameters ();
        for (int i = 0; i < parameters.intParams.length; i++) {
            parameters.changeIntParam (i);
        }
        for (int i = 0; i < parameters.collisionEffects.length; i++) {
            for (int j = 0; j < parameters.collisionEffects[0].length; j++) {
                parameters.changeCollisionEffect (i, j);
                parameters.changeScoreEffect (i, j);
            }
        }

        return parameters;
    }

    public String toString () {
        StringBuffer sb = new StringBuffer("Rules:\n");
        sb.append (" score " + intParams[SCORELIMIT] + " must be reached within " + intParams[TIMELIMIT] + " time steps\n");
        sb.append (" red: " + intParams[NUMBER_OF_RED_THINGS] +
                ", " + movementTypeNames[intParams[RED_MOVEMENT_LOGIC]] + "\n");
        sb.append (" green: " + intParams[NUMBER_OF_GREEN_THINGS] +
                ", " + movementTypeNames[intParams[GREEN_MOVEMENT_LOGIC]] + "\n");
        sb.append (" blue: " + intParams[NUMBER_OF_BLUE_THINGS] +
                ", " + movementTypeNames[intParams[BLUE_MOVEMENT_LOGIC]] + "\n");
        for (int first = 0; first < collisionEffects.length; first++) {
            for (int second = first; second < collisionEffects.length; second++) {
                if (collisionEffects[first][second] != NONE || collisionEffects[second][first] != NONE ||
                        collisionScoreEffects[first][second] != 0 || collisionScoreEffects[second][first] != 0) {
                    sb.append(" collision: " + colorNames[first] + ", " + colorNames[second] + " -> ");
                    sb.append(collisionEffectNames[collisionEffects[first][second]] + ", " +
                        collisionEffectNames[collisionEffects[second][first]] + ", " +
                        collisionScoreEffects[first][second] + ", " + collisionScoreEffects[second][first] + "\n");
                }
            }
        }
        return sb.toString ();
    }

}
