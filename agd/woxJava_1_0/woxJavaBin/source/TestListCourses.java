import wox.serial.Easy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;

/**
 * This class provides an example of an ArrayList of Course objects.
 * The main method uses the Easy class to serialize the ArrayList to XML (save method);
 * and to de-serialize the XML to an ArrayList object back again (load method).
 * http://woxserializer.sourceforge.net/
 *
 * @author Carlos R. Jaimez Gonzalez <br />
 *         Simon M. Lucas
 * @version TestListCourses.java - 1.0
 */
public class TestListCourses {

    public static void printArray(ArrayList list){
        Iterator it = list.iterator();
        while (it.hasNext()){
            System.out.println("" + it.next());
        }
    }

    /**
     * This method shows how easy is to serialize and de-serialize java objects
     * to/from XML. The XML representation of the objects is a standard WOX represntation.
     * For more information about the XML representation please visit:
     * http://woxserializer.sourceforge.net/
     */
    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        list.add(new Course(6756, "XML and Related Technologies", 3));
        list.add(new Course(9865, "Object Oriented Programming", 2));
        list.add(new Course(1134, "E-Commerce Programming", 2));
        list.add(new Course(4598, "Enterprise Component Architecture", 3));

        String filename = "C:\\TestListCourses.xml";
        //print the object
        printArray(list);
        //object to standard XML
        Easy.save(list, filename);
        //load the object from the XML file
        ArrayList newList = (ArrayList)Easy.load(filename);
        //print the new object - it is the same as before
        printArray(newList);
    }

}

