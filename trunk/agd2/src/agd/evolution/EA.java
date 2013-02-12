package agd.evolution;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Nov 24, 2008
 * Time: 6:32:45 PM
 */
public interface EA {

    public double getBestFitness ();
    
    public Evolvable getBest ();

    public void oneMoreGeneration ();
}
