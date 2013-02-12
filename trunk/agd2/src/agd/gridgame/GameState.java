package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 1, 2008
 * Time: 10:50:44 PM
 */
public class GameState implements Constants {

    private boolean alive;
    private int[] agentPosition;
    private Description description = new Description();
    private int t;
    private int score;
    private int[][][] thingPositions = new int[3][][];
    private boolean[][] thingExists = new boolean[3][];
    private direction[][] thingDirections = new direction[3][];
    private final Walls walls;
    private final Parameters parameters;

    public GameState (Parameters parameters) {
        this (parameters, new DefaultWalls ());
    }

    public GameState (Parameters parameters, Walls walls) {
        // create a new state; place agent in center
        alive = true;
        this.walls = walls;
        agentPosition = new int[]{5 + (int) (Math.random () * 5), 6 + (int) (Math.random () * 3)};
        t = 0;
        score = 0;
        for (int color = 0; color < 3; color++) {
            thingPositions[color] = new int[parameters.getNumberOfThings (color)][2];
            thingExists[color] = new boolean[parameters.getNumberOfThings (color)];
            thingDirections[color] = new direction[parameters.getNumberOfThings(color)];
            initThings (thingExists[color], thingPositions[color], thingDirections[color]);
        }
        this.parameters = parameters;

    }

    public GameState (Parameters parameters, boolean alive, int[] agentPosition, int t, int score, int[][][] thingPositions,
                      boolean[][] thingExists, direction[][] thingDirections, Walls walls) {
        this.parameters = parameters;
        this.alive = alive;
        this.agentPosition = agentPosition;
        this.t = t;
        this.score = score;
        this.thingPositions = thingPositions;
        this.thingExists = thingExists;
        this.thingDirections = thingDirections;
        this.walls = walls;
    }

    private void initThings (boolean[] existence, int[][] positions, direction[] directions) {
         for (int i = 0; i < existence.length; i++) {
            int[] newPos = description.findFreePosition ();
            positions[i][0] = newPos[0];
            positions[i][1] = newPos[1];
            existence[i] = true;
            directions[i] = GridGame.randomDirection();
        }
    }

    public GameState copy () {
        int[] agentPositionCopy = new int[]{agentPosition[0], agentPosition[1]};
        int[][][] thingPositionsCopy = new int[3][][];
        boolean[][] thingExistsCopy = new boolean[3][];
        direction[][] thingDirectionsCopy = new direction[3][];
        for (int i = 0; i < thingPositions.length; i++) {
            //System.out.println(thingPositions + " " + thingExists + " " + thingDirections);
            thingExistsCopy[i] = new boolean[thingExists[i].length];
            System.arraycopy (thingExists[i], 0, thingExistsCopy[i], 0, thingExists[i].length);
            thingDirectionsCopy[i] = new direction[thingDirections[i].length];
            System.arraycopy (thingDirections[i], 0, thingDirectionsCopy[i], 0, thingDirections[i].length);
            thingPositionsCopy[i] = new int[thingPositions[i].length][2];
            for (int j = 0; j < thingPositions[i].length; j++) {
                thingPositionsCopy[i][j][0] = thingPositions[i][j][0];
                thingPositionsCopy[i][j][1] = thingPositions[i][j][1];
            }            
        }
        GameState copy = new GameState (parameters, alive, agentPositionCopy, t, score, thingPositionsCopy,
                thingExistsCopy, thingDirectionsCopy, walls);
        return copy;
    }

    public Description getDescription () {
        return description;    
    }

    public void changeScore (int change) {
        score += change;
    }

    public boolean takePlayerAction (Action action) {
        int[] newPos = {agentPosition[0], agentPosition[1]};
        GridGame.move (newPos, action.movement);
        //System.out.println("new pos " + newPos[0] + " " + newPos[1]);
        if (!walls.wall(newPos[0], newPos[1])) {
            agentPosition = newPos;
            return true;
        }
        return false;
    }
    
    public void advanceTime () {
        t++;
    }

    public boolean moveThing (int color, int index, direction dir) {
        int[] pos = description.getThingPosition(color, index);
        GridGame.move (pos, dir);
        if (!walls.wall (pos[0],pos[1])) {
            thingPositions[color][index] = pos;
            return true;
        }
        return false;
    }

    public void setThingDirection (int color, int index, direction direction) {
        thingDirections[color][index] = direction;
    }

    public void setThingPosition (int color, int thing, int[] newPosition) {
        thingPositions[color][thing][0] = newPosition[0];
        thingPositions[color][thing][1] = newPosition[1];
    }

    public void setAgentPosition (int[] newPosition) {
        agentPosition[0] = newPosition[0];
        agentPosition[1] = newPosition[1];
    }

    public void killThing (int color, int thing) {
        thingExists[color][thing] = false;
    }

    public void die () {
        // agent is dead. capisce?
        alive = false;
    }

    public class Description {

        public int arenaHeight () {
            return walls.height ();
        }

        public int arenaWidth () {
            return walls.width ();
        }

        public boolean wall (int x, int y) {
            return walls.wall (x, y);
        }

        public boolean agent (int x, int y) {
            return (agentPosition[0] == x && agentPosition[1] == y);
        }

        public int getScore () {
            return score;
        }

        public int getT () {
                return t;
        }

        public boolean thingThere (int color, int x, int y) {
            if (thingExists[color] == null) {
                return false;
            }
            for (int i = 0; i < thingExists[color].length; i++) {
                if (thingExists[color][i]) {
                    if (thingPositions[color][i][0] == x && thingPositions[color][i][1] == y) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean agentThere (int x, int y) {
            return (x == agentPosition[X] && y == agentPosition[Y]);
        }

        public int[] findFreePosition () {
            int x, y;
            boolean newPosOK = false;
            do {
                x = (int) (Math.random () * arenaWidth());
                y = (int) (Math.random () * arenaHeight());
                if (!wall (x, y) &&
                    !agent (x, y) &&
                    (distanceToAgent (x, y) > 2) &&
                    !thingThere(RED, x, y) &&
                    !thingThere(GREEN, x, y) &&
                    !thingThere(BLUE, x, y)) {
                    newPosOK = true;
                }
            } while (!newPosOK);
            return new int[]{x, y};
        }

        public int distanceToAgent (int x, int y) {
            return (Math.abs (agentPosition[0] - x) + Math.abs (agentPosition[1] - y));
        }

        public boolean thingExists(int color, int index) {
            return thingExists[color][index];
        }

        public int[] getThingPosition(int color, int index) {
            int[] pos = thingPositions[color][index];
            return new int[]{pos[0], pos[1]};
        }

        public int[] getAgentPosition() {
            return new int[]{agentPosition[0], agentPosition[1]};
        }

        public direction getThingDirection (int color, int index) {
            return thingDirections[color][index];
        }

        public boolean alive () {
            return alive;
        }

        public int numberOfThings (int color) {
            return thingExists[color].length;
        }

        public boolean withinBounds (int x, int y) {
            return (x >= 0 && y >= 0 && x < arenaWidth() && y < arenaHeight());
        }

        public int getDistanceToClosestThing (int color, int fromX, int fromY) {
            int distance = Integer.MAX_VALUE;
            for (int i = 0; i < thingExists[color].length; i++) {
                if (thingExists[color][i]) {
                    int iDistance = Math.abs (thingPositions[color][i][X] - fromX) +
                            Math.abs (thingPositions[color][i][Y] - fromY);
                    if (iDistance < distance || distance == Integer.MAX_VALUE) {
                        distance = iDistance;
                    }
                }
            }
            return distance;
        }

        public int getDistanceToClosestThing (int color) {
            return getDistanceToClosestThing (color, agentPosition[X], agentPosition[Y]);
        }

        public int[] getClosestThingPosition (int color, int fromX, int fromY) {
            int[] position = null;
            int distance = Integer.MIN_VALUE;
            for (int i = 0; i < thingExists[color].length; i++) {
                if (thingExists[color][i]) {
                    int iDistance = Math.abs (thingPositions[color][i][X] - fromX) +
                            Math.abs (thingPositions[color][i][Y] - fromY);
                    if (iDistance < distance || distance == Integer.MIN_VALUE) {
                        position = thingPositions[color][i];
                        distance = iDistance;
                    }
                }
            }
            if (position == null)
                return null;
            return new int[]{position[X], position[Y]};
        }

        public int[] getClosestThingPosition (int color) {
            return getClosestThingPosition (color, agentPosition[X], agentPosition[Y]);
        }

 /*       public int[] getClosestThingPosition (int color) {
            int[] position = null;
            int distance = Integer.MIN_VALUE;
            for (int i = 0; i < thingExists[color].length; i++) {
                if (thingExists[color][i]) {
                    int iDistance = distanceToAgent (thingPositions[color][i][X], thingPositions[color][i][Y]);
                    if (iDistance < distance || distance == Integer.MIN_VALUE) {
                        position = thingPositions[color][i];
                        distance = iDistance;
                    }
                }
            }
            if (position == null)
                return null;
            return new int[]{position[X], position[Y]};
        } */

        public direction getDirectionToClosestThing (int color, int fromX, int fromY) {
            int[] closest = null;
            if (color == AGENT) {
                closest = getAgentPosition();
            }
            else {
                closest = getClosestThingPosition (color);
            }
            if (closest == null) {
                return null;
            }
            int xDiff = fromX - closest[X];
            int yDiff = fromY - closest[Y];
            if (Math.abs (xDiff) > Math.abs (yDiff)) {
                if (xDiff > 0)
                    return direction.left;
                return direction.right;
            }
            else {
                if (yDiff > 0)
                    return direction.up;
                return direction.down;
            }    
        }

        public direction getDirectionToClosestThing (int color) {
            if (color == AGENT) // the agent's direction to the agent?
                return null;
            return getDirectionToClosestThing (color, getAgentPosition()[X], getAgentPosition()[Y]);            
        }

        /*public Description takeSimulatedAction (Action a) {
            GameState copyState = copy ();
            copyState.takePlayerAction(a);
            copyState.advanceTime ();

        } */

        public GridGame getGameCopy () {
            return new GridGame (copy (), parameters);
        }

    }




}
