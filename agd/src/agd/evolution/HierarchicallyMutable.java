package agd.evolution;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Nov 24, 2008
 * Time: 11:31:11 AM
 */
public interface HierarchicallyMutable extends Evolvable {

    public void mutate (int level);

    public HierarchicallyMutable copy ();

    public HierarchicallyMutable getNewInstance ();

    public int countNumberOfActiveConnections ();

}
