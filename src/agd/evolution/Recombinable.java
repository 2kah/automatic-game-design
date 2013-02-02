package agd.evolution;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Oct 14, 2008
 * Time: 3:34:31 PM
 */
public interface Recombinable extends Evolvable {

    public Recombinable recombine (Recombinable other);

}
