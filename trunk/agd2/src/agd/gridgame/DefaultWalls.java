package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Nov 18, 2008
 * Time: 10:05:07 PM
 */
public class DefaultWalls implements Walls {

    private static final boolean[][] walls = {    // tilted ninety degrees!!!
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, true, false, false, false, false, false, true, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, true},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}};

    public boolean wall(int x, int y) {
        return walls[x][y];
    }

    public int height() {
        return walls[0].length;
    }

    public int width() {
        return walls.length;
    }
}
