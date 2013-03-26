package agd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: 2kah
 * Date: 20/03/13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    /**
     * writes the array of doubles to a csv file with the given filename
     *
     * @param filename
     * @param values
     * @return true for success, false for failure
     */
    public boolean writeToCsv(String filename, double[] values)
    {
        try
        {
            FileWriter writer = new FileWriter(filename);

            for(int i = 0; i < values.length; i++)
            {
                writer.append(Double.toString(values[i]) + '\n');
            }
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * appends the arraylist to a csv file with the given filename
     *
     * @param filename
     * @param values
     * @return true for success, false for failure
     */
    public boolean writeToCsv(String filename, ArrayList values)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));

            for(int i = 0; i < values.size(); i++)
            {
                writer.write(values.get(i).toString() + '\n');
            }
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
