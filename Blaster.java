import java.util.Random;

// subclass of Part, weapon systems on ship
public class Blaster extends Part {
    private final int firepower; // Amount of damage ship can do to others

    public Blaster (int durability, int firepower, Random random) {
        super(durability, random);
        this.firepower = firepower;
    }

    public Blaster (int durability, Random random) { // for new parts in shop
        super(durability, random);
        this.firepower = (int) (((double) getValue() / 4) + (random.nextDouble() / 100)); // the higher the value, the greater the firepower
    }

    public Blaster (int durability, int firepower) { // for default parts
        super(durability);
        this.firepower = firepower;
    }

    public Blaster (int durability, int fullDurability, int firepower) { // used part
        super(durability, fullDurability);
        this.firepower = firepower;
    }

    public String toString(int index, String start) {
        return super.toString(index, " Blaster") + " | Strength: " + firepower;
    }
}
