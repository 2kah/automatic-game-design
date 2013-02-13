package agd.gridgame;

import agd.gridgame.GameState.Description;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 2, 2008
 * Time: 3:56:01 PM
 */
public class View extends JPanel implements Constants {

    final int BLOCK = 10;
    private JFrame frame;
    private Description description;


    public View (Description description) {
        this.description = description;
    }

    public void setDescription (Description description) {
        this.description = description;
    }

    public Dimension getPreferredSize () {
        return new Dimension (description.arenaWidth() * BLOCK + 100, description.arenaHeight () * BLOCK + 100);
    }
                                            

    public void paintComponent (Graphics g) {
        //Graphics2D g = (Graphics2D) g1;
        final int width = description.arenaWidth();
        final int height = description.arenaHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width * BLOCK, height * BLOCK);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (description.wall(i, j)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(i * BLOCK, j * BLOCK, BLOCK, BLOCK);
                } 
                if (description.thingThere(RED, i, j)) {
                    g.setColor (Color.RED);
                    g.fillOval(i * BLOCK, j * BLOCK, BLOCK, BLOCK);
                }
                if (description.thingThere(GREEN, i, j)) {
                    g.setColor (Color.GREEN);
                    g.fillOval(i * BLOCK, j * BLOCK, BLOCK, BLOCK);
                }
                if (description.thingThere(BLUE, i, j)) {
                    g.setColor (Color.BLUE);
                    g.fillOval(i * BLOCK, j * BLOCK, BLOCK, BLOCK);
                }
                if (description.agent(i, j)) {
                    g.setColor (Color.CYAN);
                    g.fillOval(i * BLOCK, j * BLOCK, BLOCK, BLOCK);
                }
            }
        }
        updateTitle ();
        //whatTheControllerSees ();
        //directionsToNearest ();
    }

    public void setFrame (JFrame frame) {
        this.frame = frame;
        updateTitle ();
    }

    public void updateTitle () {
        frame.setTitle ("Score: " + description.getScore () + " Time: " + description.getT());
    }

    private final int[][] inputArea =
            {{-2, 0}, {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -2}, {0, -1}, {0, 1}, {0, 2},
                    {1, -1}, {1, 0}, {1, 1}, {2, 0}};
    private final int numberOfInputs = inputArea.length * 4 + direction.values().length * 3;

    private void whatTheControllerSees () {
        Description state = description;
        final int area = inputArea.length;
        double[] inputs = new double[numberOfInputs + 1];
        for (int i = 0; i < area; i++) {
            int[] probe = state.getAgentPosition();
            probe[X] += inputArea[i][X];
            probe[Y] += inputArea[i][Y];
            if (state.withinBounds (probe[X], probe[Y])) {
                if (state.wall(probe[X], probe[Y]))
                    inputs[i] = 1;
                if (state.thingThere(RED, probe[X], probe[Y]))
                    inputs[i + area] = 1;
                if (state.thingThere(GREEN, probe[X], probe[Y]))
                    inputs[i + area * 2] = 1;
                if (state.thingThere(BLUE, probe[X], probe[Y]))
                    inputs[i + area * 3] = 1;
            }
        }
        for (int color = 0; color < 3; color++) {
            direction nearest = state.getDirectionToClosestThing(color);
            inputs[area * 4 + color * 4 + nearest.ordinal()] = 1;
        }
        printInputs (inputs);
    }

    private void printInputs (double[] inputs) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < inputArea.length; j++) {
                System.out.print(inputs[inputArea.length * i + j] + "  ");    
            }
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(inputs[inputArea.length * 4 + i * 4 + j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void directionsToNearest () {
        for (int color = 0; color < 3; color++) {
            direction nearest = description.getDirectionToClosestThing(color);
            System.out.println("nearest " + colorNames[color] + ": " + nearest);
        }
        System.out.println("");
    }

}
