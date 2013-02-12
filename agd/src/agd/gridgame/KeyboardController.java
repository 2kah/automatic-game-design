package agd.gridgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 9, 2008
 * Time: 5:38:16 PM
 */
public class KeyboardController extends KeyAdapter implements Controller, Constants {

    private direction dir = direction.up;
    boolean newActionChosen = false;

    public Action control(GameState.Description state) {
        while (!newActionChosen) {
            try {
                Thread.sleep (1);
            }
            catch (Exception e) {
                e.printStackTrace ();
            }
        }
        newActionChosen = false;
        Action action = new Action ();
        action.movement = dir;
        return action;
    }
    
    public void keyPressed (KeyEvent e) {
        int key = e.getKeyCode ();
        //System.out.println("key pressed: " + key);
        switch (key) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                dir = direction.down;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                dir = direction.up;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                dir = direction.left;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                dir = direction.right;
                break;
        }
        newActionChosen = true;
    }

    public void reset () {}

}
