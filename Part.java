import java.io.Serializable;
import java.util.Random;

// superclass for parts that can be replaced on the ship.
public class Part implements Comparable<Part>, Serializable {
   private final int fullDurability; // set max durability
   private int durability; // set current durability
   private int value; // value of part
   private int usedValue; // value of part when used
   private int newValue; // value of part when new
   
   public Part(int durability, Random random) { // new parts
      this.durability = durability;
      this.fullDurability = this.durability;
      this.value = (int) ((random.nextDouble() * 0.5 + 0.5) * this.fullDurability); // new part value formula
      this.newValue = value; // sets used and new values for parts
      this.usedValue = newValue - (fullDurability / 3);
   }

   public Part(int durability) { // new parts (default parts)
      this.durability = durability;
      this.fullDurability = this.durability;
      this.value = 70;
      this.newValue = value; // sets used and new values for parts
      this.usedValue = 40;
   }

   public Part(int durability, int fullDurability) { // used parts
      this.durability = durability;
      this.fullDurability = fullDurability;
      this.value = (int) (70.0 * this.durability / this.fullDurability) + 1; // used parts value formula
      this.usedValue = value; // sets used and new values for parts
      this.newValue = usedValue + (fullDurability / 3);
   }

   public Part(float value) { // for indestructable parts
      this.value = (int) value;
      this.durability = -1;
      this.fullDurability = -1;
      this.usedValue = this.value;
      this.newValue = this.value;
   }

   
   public void use(int value) { // decrease  durability by a set amount
      if (this.fullDurability > -1) { // if part is destructable
         this.durability -= value;
         this.value = this.usedValue; // reduce value in part if used
      }
   }

   public void repair() { // sets durability to initial value;
      this.durability = this.fullDurability;
      this.value = newValue;
   }

   public int getValue() { return this.value; }

   public int compareTo(Part other) { // compares Parts by price
      return Integer.compare(this.value, other.value);
   }

   public int getDurability() { return durability; }

   public int getFullDurability() { return fullDurability; }

   public String toString(int index, String start) {
      String output = "";

      if (durability == fullDurability) {
         output += "New";
      }
      
      else {
         output += "Used";
      }

      output += start;

      while (output.length() < 25) { // spacing for formatting
         output += " ";
      }
      if (fullDurability > -1) {
         output += "Durability: " + this.durability + "/" + this.fullDurability;
      }
      while (output.length() < 70) { // spacing for formatting
         output += " ";
      }
      output += "Value: " + this.value + " Credits";
      if (this.value == 1) {
         output = output.substring(0, output.length() - 1); // credits -> credit if value == 1
      }
      while (output.length() < 90) { // spacing for formatting
         output += " ";
      }
      return output;
      }

      public float getFuelEfficiency() { return 0; }

      public float getCapacity() { return 0; }
}