import wox.serial.Easy;

/**
 * This class provides an example of a class with some primitive arrays as fields.
 * The main method uses the Easy class to serialize TestArray object to XML (save method);
 * and to de-serialize the XML to a TestArray object back again (load method).
 * http://woxserializer.sourceforge.net/
 *
 * @author Carlos R. Jaimez Gonzalez <br />
 *         Simon M. Lucas
 * @version TestArray.java - 1.0
 */
public class TestArray {
    private char[] codes;
    private int[] values;
    private boolean[] answers;

    public TestArray(char[] codes, int[] values, boolean[] answers) {
        this.codes = codes;
        this.values = values;
        this.answers = answers;
    }

    public static void printTestArray(TestArray testArray){
        String display = "";
        for (int i=0; i<testArray.codes.length; i++){
            display += testArray.codes[i] + " ";
        }
        display += "\n";
        for (int i=0; i<testArray.values.length; i++){
            display += testArray.values[i] + " ";
        }
        display += "\n";
        for (int i=0; i<testArray.codes.length; i++){
            display += testArray.answers[i] + " ";
        }
        display += "\n";
        System.out.println(display);
    }

    /**
     * This method shows how easy is to serialize and de-serialize java objects
     * to/from XML. The XML representation of the objects is a standard WOX represntation.
     * For more information about the XML representation please visit:
     * http://woxserializer.sourceforge.net/
     */
    public static void main(String[] args) {
        TestArray testArray = new TestArray(new char[]{'e', 't', 'r', 'g', 'w'},
                                            new int[]{23, 56, 78, 33, 69},
                                            new boolean[]{true, false, true, false, false});
        String filename = "C:\\TestPrimitiveArrays.xml";
        //print the object
        printTestArray(testArray);
        //object to standard XML
        Easy.save(testArray, filename);
        //load the object from the XML file
        TestArray newTestArray = (TestArray)Easy.load(filename);
        //print the new object - it is the same as before
        printTestArray(newTestArray);
    }

}
