package agd.test;

import agd.gridgame.Controller;
import agd.gridgame.KeyboardController;
import agd.gridgame.Parameters;
import agd.gridgame.Play;
import wox.serial.Easy;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: 2kah
 * Date: 13/03/13
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class TestHuman {

    public static void main(String[] args) throws Exception
    {
        Parameters parameters = null;
        String paramName = "";
        paramName = args[0];
        try {
            parameters = (Parameters) Class.forName (paramName).newInstance ();
        }
        catch (ClassNotFoundException e) {
            System.out.println (paramName + " is not a class name; trying to load a wox definition with that name.");
            parameters = (Parameters) Easy.load(paramName);
        }
        System.out.println(parameters);
        Controller controller = new KeyboardController();
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
        // Play the game and output results
        Play.play (parameters, controller, args[0]);
    }
}
