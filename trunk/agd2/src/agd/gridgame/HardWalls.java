package agd.gridgame;

/**
 * Created by IntelliJ IDEA.
 * User: juto
 * Date: Nov 13, 2009
 * Time: 3:48:09 PM
 */
public class HardWalls implements Walls {

    private static final boolean[][] walls = {    // tilted ninety degrees!!!
            {true , true , true , true , true , true , true , true , true , true , true , true , true , true , true },
            {true , false, false, false, false, false, false, false, false, false, false, false, true , false, true },
            {true , false, false, false, false, false, false, false, false, false, false, false, true , false, true },
            {true , false, false, true , true , true , false, false, true , false, false, false, true , false, true },
            {true , false, false, true , false, false, false, false, false, false, false, false, true , false, true },
            {true , false, false, true , false, false, false, false, false, false, false, false, false, false, true },
            {true , false, false, false, false, false, false, false, false, false, true , false, false, false, true },
            {true , false, false, false, true , false, false, false, false, false, true , false, false, false, true },
            {true , true , true , false, false, false, false, false, false, true , true , true , true , false, true },
            {true , false, false, false, false, false, false, false, false, true , false, false, false, false, true },
            {true , false, false, false, false, true , false, false, false, true , false, false, false, false, true },
            {true , false, false, false, true , false, false, false, false, true , false, false, false, false, true },
            {true , false, false, true , false, false, false, false, false, true , false, true , true , false, true },
            {true , false, true , false, false, false, false, false, false, false, false, false, false, false, true },
            {true , false, false, false, false, false, false, false, false, false, false, false, false, false, true },
            {true , true , true , true , true , true , true , true , true , true , true , true , true , true , true }};

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
