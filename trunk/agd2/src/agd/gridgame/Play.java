package agd.gridgame;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 15, 2008
 * Time: 7:33:05 PM
 */
public class Play {

    public static void main(String[] args) throws Exception {
        Parameters parameters = null;
        try {
            parameters = (Parameters) Class.forName (args[0]).newInstance ();
        }
        catch (ClassNotFoundException e) {
            System.out.println (args[0] + " is not a class name; trying to load a wox definition with that name.");
            parameters = (Parameters) Easy.load (args[0]);
        }
        System.out.println(parameters);
        Controller controller = new KeyboardController ();
        if (args.length > 1) {
            try {
                controller = (Controller) Class.forName (args[1]).newInstance ();
            }
            catch (ClassNotFoundException e) {
                System.out.println (args[1] + " is not a class name; trying to load a wox definition with that name.");
                parameters = (Parameters) Easy.load (args[1]);
            }
        }
        System.out.println(controller);
        play (parameters, controller);

    }

    public static void play (Parameters parameters, Controller controller) {
        GridGame game = new GridGame (parameters);
        View view = new View (game.getStateDescription());
        initializeVisual(view, (controller instanceof KeyAdapter ? (KeyAdapter) controller : null));
        GameResults results = game.play(view, controller, 500);      
        System.out.println("Game results: agent " + (results.survived ? "survived" :  "died") +
                ", score: " + results.score ); //+ " out of " + game.getScoreToWin());
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
