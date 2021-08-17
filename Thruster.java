import java.io.Serializable;
import java.util.Random;

// subclass of Part, engine that runs ship
public class Thruster extends Part implements Serializable {
    private final float fuelEfficiency; // Coefficient for amount of fuel it takes to go one light year. Default is 1

    public Thruster (int durability, float fuelEfficiency, Random random) { // new part
        super(durability, random);
        this.fuelEfficiency = fuelEfficiency;
    }

    public Thruster (int durability, Random random) { // for new parts in shop
        super(durability, random);
        this.fuelEfficiency = (float) (((double) getValue() / 150) + (random.nextDouble() / 100)); // the higher the value, the greater the fuel efficiency
    }

    public Thruster (int durability, float fuelEfficiency) { // for default parts
        super(durability);
        this.fuelEfficiency = fuelEfficiency;
    }

    public Thruster (int durability, int fullDurability, float fuelEfficiency) { // used part
        super(durability, fullDurability);
        this.fuelEfficiency = fuelEfficiency;
    }

    public float getFuelEfficiency() { return fuelEfficiency; }

   public String toString(int index, String start) {
        return super.toString(index, " Thruster") + " | " + fuelEfficiency + " light-years/L";
    }
}
