// represents the various resources found within a planet
import java.io.Serializable;
import java.util.Random;

public class Resource implements Comparable<Resource>, Serializable {
    private Database elementValues; // represents element in periodic table and its value
    private Database elementNames; // represents element in periodic table and name of element
    String elementName;
    float elementValue;

    public Resource(double distanceFromStart, Random random) { // gets distance in sectors from origin
        elementNames = new Database(1); // get database of element names
        elementValues = new Database(2); // get database of element values

        // gets random coordinates. If coordinates are beneath a formula of 1/x * (2 * distanceFromStart + 1), then become the resource at coordinate
        elementValue = 0;
        while (elementValue == 0) {
            int xCoordinate = random.nextInt(90) + 1; // gets a set of random coordinates to check from
            int yCoordinate = random.nextInt(50) + 1;
            double formula = ((5.0 / xCoordinate) * (2 * Math.abs(distanceFromStart) + 1));
            //System.out.println(xCoordinate + " " + yCoordinate + " " + formula);
            if (yCoordinate < formula) {
                elementName = elementNames.get(yCoordinate);
                elementName = elementName.substring(0, elementName.length() - 1); // eliminates the \n
                elementValue = Float.parseFloat(elementValues.get(yCoordinate));
                //String getElementValue = elementValues.get(yCoordinate);
                //getElementValue = getElementValue.replaceAll("\\D+","");
                //elementValue = Integer.parseInt(getElementValue); // get element value without quotes
                //System.out.println("done");
            }
        }
    }

    public String toString() {
        return getElementName() + " (" + getElementValue() + " Credits/kg)";
    }

    public float getElementValue() { return elementValue; }

    public String getElementName() { return elementName; }

    public int compareTo(Resource other) { // compares resources by value
        return Float.compare(this.elementValue, other.elementValue);
    }
}
