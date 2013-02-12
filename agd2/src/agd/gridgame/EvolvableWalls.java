package agd.gridgame;

import agd.evolution.Evolvable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Nov 19, 2008
 * Time: 9:27:20 PM
 */
public class EvolvableWalls implements Evolvable, Walls, Constants {

    static final int width = 16;
    static final int height = 15;
    static final int maxNumberOfStretches = 20;
    static final int maxStretchLength = 8;
    private final boolean[][] walls = new boolean[width][height];
    private final Stretch[] borders = {
            new Stretch (0, 0, direction.right, width),
            new Stretch (width-1, 0, direction.down, height),
            new Stretch (width-1, height-1, direction.left, width),
            new Stretch (0, height-1, direction.up, width)};
    private final List<Stretch> stretches;

    public EvolvableWalls () {
        stretches = new ArrayList<Stretch>();
        redraw ();
    }

    public boolean wall(int x, int y) {
        return walls[x][y];
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public Evolvable copy() {
        EvolvableWalls other = new EvolvableWalls ();
        Iterator<Stretch> it = this.stretches.iterator ();
        while (it.hasNext ()) {
            Stretch stretch = it.next ();
            other.stretches.add (stretch.copy ());
        }
        return other;
    }

    public void mutate() {
        boolean add = Math.random () < (stretches.size () / ((double) maxNumberOfStretches));
        if (add) {
            stretches.add (new Stretch (
                    (int) (Math.random () * (width - 2)) + 1,
                    (int) (Math.random () * (height - 2)) + 1,
                    direction.values()[(int) Math.random () * direction.values().length],
                    (int) (Math.random () * maxStretchLength - 1) + 1));
        }
        else {
            stretches.remove((int) (Math.random () * stretches.size ()));
        }
        redraw ();
    }

    public Evolvable newInstance() {
        return new EvolvableWalls ();
    }

    private void redraw () {
        // clear the area
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                walls[i][j] = false;
            }
        }
        // draw the borders
        for (Stretch stretch : borders)
            drawStretch (stretch);
        // draw the evolved stretches of wall
        for (Stretch stretch : stretches)
            drawStretch (stretch);
    }

    private void drawStretch (Stretch stretch) {
        int x = stretch.x;
        int y = stretch.y;
        for (int i = 0; i < stretch.length; i++) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                break;
            }
            walls[x][y] = true;
            switch (stretch.direction) {
                case up:
                    y++;
                    break;
                case down:
                    y--;
                    break;
                case right:
                    x++;
                    break;
                case left:
                    x--;
                    break;
            }
        }
    }


    class Stretch {

        public Stretch (int x, int y, direction direction, int length) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.length = length;
        }

        int x;
        int y;
        direction direction;
        int length;

        public Stretch copy() {
            return new Stretch (x, y, direction, length);
        }
    }
}
