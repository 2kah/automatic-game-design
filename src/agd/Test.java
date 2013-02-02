package agd;

import agd.gridgame.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 8, 2008
 * Time: 5:14:22 PM
 */
public class Test implements Constants {

    public static void main(String[] args) {
        //Parameters parameters = new TestParameters1 ();
        Parameters parameters = Parameters.createRandomParameters();
        GridGame game = new GridGame (parameters);
        View view = new View (game.getStateDescription());
        KeyboardController controller = new KeyboardController ();
        initializeVisual(view, controller);
        System.out.println(parameters);
        GameResults results = game.play(view, controller, 0);
        System.out.println("Game results: agent " + (results.survived ? "survived" :  "died") +
                ", score: " + results.score + " out of " + game.getScoreToWin());
        
    }

    public static void initializeVisual (View view, KeyAdapter adapter) {
        JFrame frame = new JFrame (" ");
        if (adapter != null) {
            frame.addKeyListener (adapter);
        }
        frame.addWindowListener (new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                System.exit (0);
            }
        });
        frame.getContentPane ().add (view);
        frame.pack ();
        frame.setVisible (true);
        view.setFrame (frame);
    }

    
}
