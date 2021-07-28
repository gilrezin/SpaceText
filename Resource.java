// represents the various resources found within a planet
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Random;

public class Resource {
    private Database elementValues; // represents element in periodic table and its value
    private Database elementNames; // represents element in periodic table and name of element
    String elementName;
    int elementValue;

    public Resource(double distanceFromStart, Random random) { // gets distance in sectors from origin
        elementNames = new Database(1); // get database of element names
        elementValues = new Database(2); // get database of element values

        // gets random coordinates. If coordinates are beneath a formula of 1/x * (2 * distanceFromStart + 1), then become the resource at coordinate
        elementValue = 0;
        while (elementValue == 0) {
            int xCoordinate = random.nextInt(90) + 1; // gets a set of random coordinates to check from
            int yCoordinate = random.nextInt(50) + 1;
            double formula = ((5.0 / xCoordinate) * (2 * distanceFromStart + 1));
            //System.out.println(xCoordinate + " " + yCoordinate + " " + formula);
            if (yCoordinate < formula) {
                elementName = elementNames.get(yCoordinate);
                String getElementValue = elementValues.get(yCoordinate);
                getElementValue = getElementValue.replaceAll("\\D+","");
                elementValue = Integer.parseInt(getElementValue); // get element value without quotes
                //System.out.println("done");
            }
        }
    }

    public int getElementValue() { return elementValue; }

    public String getElementName() { return elementName; }
}
