import wox.serial.Easy;

/**
 * This class provides an example of a class with some primitive
 * fields. The main method uses the Easy class to serialize a Course
 * object to XML (save method); and to de-serialize the XML to a
 * Course object back again (load method).
 * http://woxserializer.sourceforge.net/
 *
 * @author Carlos R. Jaimez Gonzalez <br />
 *         Simon M. Lucas
 * @version Course.java - 1.0
 */
public class Course {

    private int code;
    private String name;
    private int term;

    public Course(int code, String name, int term) {
        this.code = code;
        this.name = name;
        this.term = term;
    }

    public String toString(){
        return ("code: " + code + ", name: " + name + ", term: " + term);
    }

    /**
     * This method shows how easy is to serialize and de-serialize java objects
     * to/from XML. The XML representation of the objects is a standard WOX represntation.
     * For more information about the XML representation please visit:
     * http://woxserializer.sourceforge.net/
     */
    public static void main(String[] args) {
        String filename = "C:\\TestCourse.xml";
        Course course1 = new Course(6756, "XML and Related Technologies", 3);
        Course course2 = new Course(9865, "Object Oriented Programming", 2);
        Course course3 = new Course(1134, "E-Commerce Programming", 2);

        //print the object
        System.out.println(course1);
        //object to standard XML
        Easy.save(course1, filename);
        //load the object from the XML file
        Course newCourse = (Course)Easy.load(filename);
        //print the new object - it is the same as before
        System.out.println(newCourse);
    }

}
