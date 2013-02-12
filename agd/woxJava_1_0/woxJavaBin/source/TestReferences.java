import wox.serial.Easy;

/**
 * This class provides an example of how WOX handles object references.
 * The main method uses the Easy class to serialize an array of Product objects
 * to XML (save method); and to de-serialize the XML to an array of Product objects
 * back again (load method). The array contains duplicate objects.
 * http://woxserializer.sourceforge.net/
 *
 * @author Carlos R. Jaimez Gonzalez <br />
 *         Simon M. Lucas
 * @version TestReferences.java - 1.0
 */
public class TestReferences {

    public static void printProducts(Product[] products){
        for(int i=0; i<products.length;i++){
            System.out.println("" + products[i]);
        }
    }

    /**
     * This method shows how easy is to serialize and de-serialize java objects
     * to/from XML. The XML representation of the objects is a standard WOX represntation.
     * For more information about the XML representation please visit:
     * http://woxserializer.sourceforge.net/
     */
    public static void main(String[] args) {
        Product p1 = new Product("Baked beans", 1.75, 250, true, 'B');
        Product p2 = new Product("Basmati Rice", 3.89, 750, true, 'R');
        Product p3 = new Product("White bread", 1.06, 300, false, 'H');
        Product[] products = new Product[]{p1, p2, p1, p3, p3, p1};

        String filename = "C:\\TestReferences.xml";
        //print the array of objects
        printProducts(products);
        //object to standard XML
        Easy.save(products, filename);
        //load the object (array) from the XML file
        Product[] newProducts = (Product[])Easy.load(filename);
        //print the new object - it is the same as before
        printProducts(newProducts);
    }
}
