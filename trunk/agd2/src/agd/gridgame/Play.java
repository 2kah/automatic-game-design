package agd.gridgame;

import wox.serial.Easy;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class Play {

    public static void main(String[] args) throws Exception {
        Parameters parameters = null;
        String paramName = "";
        if (args.length == 0)
        {
            // Get the parameters from the Games folder
            File dir = new File("Games");
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            };
            File[] gameFiles = dir.listFiles(filter);
            String[] games = new String[gameFiles.length];
            for(int i = 0; i < gameFiles.length; i++)
                games[i] = gameFiles[i].getPath();
            Arrays.sort(games);
            paramName = games[games.length-1];
        }
        else
            paramName = args[0];
        try {
            parameters = (Parameters) Class.forName (paramName).newInstance ();
        }
        catch (ClassNotFoundException e) {
            System.out.println (paramName + " is not a class name; trying to load a wox definition with that name.");
            parameters = (Parameters) Easy.load (paramName);
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

        KeyboardController kc = null;
        if (controller instanceof KeyAdapter)
            kc = (KeyboardController) controller;

        while(true)
        {
            GameResults results = game.play(view, controller, 500);
            System.out.println("Game results: agent " + (results.survived ? "survived" :  "died") +
                    ", score: " + results.score + " out of " + game.getScoreToWin());

            // not a keyboard controller
            if (!(controller instanceof KeyAdapter))
                return;

            System.out.println("Press enter to play again");
            while (!kc.startNewGame) {
                try {
                    Thread.sleep (1);
                }
                catch (Exception e) {
                    e.printStackTrace ();
                }
            }
            kc.startNewGame = false;
            game.resetGameState();
            view.setDescription(game.getStateDescription());
            view.repaint(1);
        }
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
