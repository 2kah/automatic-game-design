package agd.evolution;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 13, 2008
 * Time: 9:46:03 PM
 */
public interface Evolvable {
    public boolean getFitnessKnown();
    public void setFitnessKnown(boolean known);

    public Evolvable copy ();

    public void mutate ();

    public Evolvable newInstance ();

}
