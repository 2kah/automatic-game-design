package agd.test;

import agd.gridgame.Controller;
import agd.gridgame.KeyboardController;
import agd.gridgame.Parameters;
import agd.gridgame.Play;
import wox.serial.Easy;

import java.io.File;

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
        //System.out.println(parameters);
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
        String[] gamePath = args[0].split("\\.");
        String gameName = gamePath[gamePath.length-1] + "HumanData";

        // make the folder if it doesn't already exist
        File f = new File(gameName);
        try{
            if(f.mkdir()) {
                //System.out.println("Directory Created");
            } else {
                //System.out.println("Directory is not created");
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        Play.play (parameters, controller, gameName);
    }
}
