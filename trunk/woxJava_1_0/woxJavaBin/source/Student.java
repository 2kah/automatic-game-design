import wox.serial.Easy;

/**
 * This class provides an example of a class with some primitive
 * fields, and an array of Course objects. The main method uses the
 * Easy class to serialize a Student object to XML (save method);
 * and to de-serialize the XML to a Student object back again (load method).
 * http://woxserializer.sourceforge.net/
 *
 * @author Carlos R. Jaimez Gonzalez <br />
 *         Simon M. Lucas
 * @version Student.java - 1.0
 */
public class Student {

    private String name;
    private int registrationNumber;
    private Course[] courses;

    public Student(String name, int registrationNumber, Course[] courses) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.courses = courses;
    }

    public String toString(){
        return ("name: " + name + ", registrationNumber, " + registrationNumber +
                ", courses: \n" + printArray(courses) );
    }

    public String printArray(Object[] ob){
        String coursesStr = "";
        for (int i=0; i<ob.length; i++){
            coursesStr = coursesStr + ob[i] + "\n";
        }
        return coursesStr;
    }

    /**
     * This method shows how easy is to serialize and de-serialize java objects
     * to/from XML. The XML representation of the objects is a standard WOX represntation.
     * For more information about the XML representation please visit:
     * http://woxserializer.sourceforge.net/
     */
    public static void main(String[] args) {
        String filename = "C:\\TestStudent.xml";
        Course[] courses = {new Course(6756, "XML and Related Technologies", 2),
                            new Course(9865, "Object Oriented Programming", 2),
                            new Course(1134, "E-Commerce Programming", 3)};
        Student student = new Student ("Carlos Jaimez", 76453, courses);

        //print the Student object
        System.out.println(student);
        //object to standard XML
        Easy.save(student, filename);
        //get the object back from the XML file
        Student newStudent = (Student)Easy.load(filename);
        //print the new object - it is the same as before
        System.out.println(newStudent);                
    }

}
