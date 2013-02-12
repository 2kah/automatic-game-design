import wox.serial.Easy;

/**
 * This class provides an example of how WOX handles circular object references.
 * The main method uses the Easy class to serialize a Computer object
 * to XML (save method); and to de-serialize the XML to a Computer object
 * back again (load method). The Computer object has three fields, and one of them
 * is an instance of a computer, which will be used to have a circular reference.
 * http://woxserializer.sourceforge.net/
 *
 * @author Carlos R. Jaimez Gonzalez <br />
 *         Simon M. Lucas
 * @version Computer.java - 1.0
 */
public class Computer {
    private int year;
    private String model;
    private Computer computer;

    public Computer(int year, String model) {
        this.year = year;
        this.model = model;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public String toString(){
        return ("year: " + year + ", model: " + model);
    }

    /**
     * This method shows how easy is to serialize and de-serialize java objects
     * to/from XML. The XML representation of the objects is a standard WOX represntation.
     * For more information about the XML representation please visit:
     * http://woxserializer.sourceforge.net/
     */
    public static void main(String[] args) {
        Computer computer = new Computer(2008, "HP Pavillion");
        //a reference to itself (circular reference)
        computer.setComputer(computer);

        String filename = "C:\\TestComputer.xml";
        //print the Computer object
        System.out.println(computer);
        //object to standard XML
        Easy.save(computer, filename);
        //get the object back from the XML file
        Computer newComputer = (Computer)Easy.load(filename);
        //print the new object - it is the same as before
        System.out.println(newComputer);
        //get the computer field
        Computer computerField = newComputer.getComputer();
        System.out.println(computerField);
    }

}
