import java.io.Serializable;
import java.util.Random;

// subclass of Part, defends ship from weapons
public class Shield extends Part implements Serializable {

    public Shield (int durability, Random random) {
        super(durability, random);
    }

    public Shield (int durability) {
        super(durability);
    }

    public Shield (int durability, int fullDurability) { // used part
        super(durability, fullDurability);
    }

    public String toString(int index, String start) {
        return super.toString(index, " Shield Generator") + " |";
    }
}
