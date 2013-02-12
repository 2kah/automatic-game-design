package agd.evolution;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jul 13, 2008
 * Time: 9:46:03 PM
 */
public interface Evolvable {

    public Evolvable copy ();

    public void mutate ();

    public Evolvable newInstance ();

}
