// class that represents a planet in solar system
import java.util.Arrays;
import java.util.Random;
public class Planet implements Comparable<Planet> {
   private String name;
   int planetIndex;
   private float size;
   private float temperature;
   private boolean inhabited;
   private float population;
   private Part[] partsToPurchase;
   private Resource resource;
   private int galacticLocationX;
   private int galacticLocationY;

   
   public Planet(String name, int index, Random random, int galacticLocationX, int galacticLocationY) {
      this.galacticLocationX = galacticLocationX;
      this.galacticLocationY = galacticLocationY;
      this.planetIndex = index;
      this.name = name;
      this.size = (float) random.nextInt(2000) + 20; // surface area in millions of square kilometers
      this.temperature = (float) (random.nextInt(900) - 273.15);
      this.inhabited = random.nextDouble() < 0.5; // returns random true or false
      if (inhabited) {
         this.population = (float) (random.nextDouble() * (size * 30) / (Math.abs(temperature) / 30) + 1); // population in millions of people (set up so that the larger the planet and the closer to 0C, the higher the population)
         resource = null;
      }
      else {
         resource = new Resource(5, random); // create new resource
      }
      
      if (population > 1000) { // if population is more than 1 billion, sell new parts. Otherwise, sell used parts
         partsToPurchase = new Part[(int) (random.nextDouble() * population / 1000 * (random.nextDouble() * 5 + 1))]; // number of parts available is influenced by population
         for (int i = 0; i < partsToPurchase.length; i++) {
            int randomValue = random.nextInt(100);
            int randomDurability = (int) (random.nextDouble() * random.nextInt(950) + 50);
            if (randomValue < 33) { // add Thruster
               partsToPurchase[i] = new Thruster(randomDurability, random); // part durability randomized between 50-500
            } // add Shield
            else if (randomValue < 66) {
               partsToPurchase[i] = new Shield(randomDurability, random);
            } // add Blaster
            else {
               partsToPurchase[i] = new Blaster(randomDurability, random);
            }
         }
      }
      
      else { // sell used parts
         partsToPurchase = new Part[(int) (random.nextDouble() * population / 100 * (random.nextDouble() * 10 + 1))]; // number of parts available is influenced by population
         for (int i = 0; i < partsToPurchase.length; i++) {
            int durability = (int) (random.nextDouble()* 450 + 50); // max part durability between 50-500

            partsToPurchase[i] = new Part((int) (random.nextDouble() * durability), durability);
            int randomValue = random.nextInt(100);
            if (randomValue < 33) { // add Thruster
               partsToPurchase[i] = new Thruster((int) (random.nextDouble() * durability), durability, (float) (1 - (random.nextDouble() / 10))); // part durability randomized between 50-500
            } // add Shield
            else if (randomValue < 66) {
               partsToPurchase[i] = new Shield((int) (random.nextDouble() * durability), durability);
            } // add Blaster
            else {
               partsToPurchase[i] = new Blaster((int) (random.nextDouble() * durability), durability, (int) (random.nextDouble() * 150 + 50));
            }
         }
      }
      Arrays.sort(partsToPurchase); // sorts parts by price
   }

   public int getPlanetIndex() { return planetIndex; }
   
   public String getName() {
      return this.name;
   }
   
   public double getSize() {
      return this.size;
   }
   
   public double getTemperature() {
      return this.temperature;
   }

   public double getPopulation() { return this.population; }
   
   public boolean isInhabited() {
      return inhabited;
   }

   public int getGalacticLocationX() {
      return galacticLocationX;
   }

   public int getGalacticLocationY() {
      return galacticLocationY;
   }
   
   public String toString() {
      String inhabitedString;
      if (inhabited) { // if inhabited, display population
         inhabitedString = "Population: " + population + " million";
         if (population > 1000000) { // display population in trillions of people
            inhabitedString = "Population: " + (population / 1000000) + " trillion";
         }
         else if (population > 1000) { // display population in billions of people
            inhabitedString = "Population: " + (population / 1000) + " billion";
         }

      }
      else {
         inhabitedString = "Uninhabited\nContains high concentrations of " + resource.getElementName();
      }
      
      String sizeString = size + " million"; // displays the correct units
      if (size > 1000) {
         sizeString = size / 1000 + " billion";
      }

      
      return "<Planet " + name + ">\nSize: " + sizeString +" km^2\nAverage Temperature: " + temperature + " C\n" + inhabitedString;
   }
   
   public Part getPart(int index) {
      return partsToPurchase[index];
   }
   
   public String getPartList() {
      if (partsToPurchase.length == 0) {
         return "No Parts Available\n\nType \"back\" to go back";
      }
      String output = "";
      int index;
      int maxIndex;
      for (int i = 0; i < partsToPurchase.length; i++) {
         output += (i + 1) + ". ";
         index = i;
         maxIndex = (partsToPurchase.length + "").length(); // gets digit length of largest value
         int spaces = maxIndex - (((index + 1) + "").length()); // gets digit length of index value and subtracts from maxIndex
         while (spaces >= 1) { // adds spacing if index < 10 (for formatting purposes)
            output += " ";
            index /= 10;
            spaces--; // reduces spacing once added
         }
         output += getPart(i).toString(i, "") + "\n"; // return part number and info
      }
      output += "\nType \"back\" to go back";
      return output;
   }

   public int compareTo(Planet other) { // compares Planets by population
      if (this.population > other.population) { return 1;}
      if (this.population < other.population) { return -1;}

      else {return 0;}
   }
   
}