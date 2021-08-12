import java.util.Random;

public class FuelCell extends Part {
    private float capacity;

    public FuelCell (float value) { // for default parts
        super(value);
        this.capacity = value / 2;
    }

    public FuelCell (float value, Random random) { // for parts in the shop
        super(value);
        this.capacity = (float) Math.ceil((((double) value / 3) + (random.nextDouble() / 100))); // the higher the value, the greater the capacity
    }

    public String toString(int index, String start) {
        return super.toString(index, " Fuel Cell") + " | Capacity: " + capacity + "L";
    }

    public float getCapacity() {
        return capacity;
    }
}