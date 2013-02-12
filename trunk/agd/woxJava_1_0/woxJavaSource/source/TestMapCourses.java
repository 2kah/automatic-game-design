import wox.serial.Easy;
import java.util.*;

/**
 * This class provides an example of a HashMap of Course objects.
 * The main method uses the Easy class to serialize the HashMap to XML (save method);
 * and to de-serialize the XML to a HashMap object back again (load method).
 * http://woxserializer.sourceforge.net/
 *
 * @author Carlos R. Jaimez Gonzalez <br />
 *         Simon M. Lucas
 * @version TestMapCourses.java - 1.0
 */
public class TestMapCourses {

    public static void printMap(HashMap map){
        Set keys = map.entrySet();
        Iterator it = keys.iterator();
        while (it.hasNext()){
            Map.Entry entryMap= (Map.Entry)it.next();
            Integer key = (Integer)entryMap.getKey();
            Course value = (Course)entryMap.getValue();
            System.out.println("*KEY* " + key + ", *OBJECT* " + value);
        }
    }

    /**
     * This method shows how easy is to serialize and de-serialize java objects
     * to/from XML. The XML representation of the objects is a standard WOX represntation.
     * For more information about the XML representation please visit:
     * http://woxserializer.sourceforge.net/
     */
    public static void main(String[] args) {
        HashMap map = new HashMap();
        map.put(6756, new Course(6756, "XML and Related Technologies", 3));
        map.put(9865, new Course(9865, "Object Oriented Programming", 2));
        map.put(1134, new Course(1134, "E-Commerce Programming", 2));
        map.put(4598, new Course(4598, "Enterprise Component Architecture", 3));

        String filename = "C:\\TestMapCourses.xml";
        //print the object
        printMap(map);
        //object to XML
        Easy.save(map, filename);
        //load the object from the XML file
        HashMap newMap = (HashMap)Easy.load(filename);
        //print the object just loaded
        printMap(newMap);
    }

}
