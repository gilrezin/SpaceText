// class that represents a planet in solar system
import java.io.Serializable;
import java.util.*;

public class Planet implements Comparable<Planet>, Serializable {
   private String name;
   int planetIndex;
   private float size;
   private float temperature;
   private boolean inhabited;
   private float population;
   private Part[] partsToPurchase;
   private TreeMap<Float, Resource>  resources; // resources on a planet and their given percent of composition in the planet's ground
   private int galacticLocationX;
   private int galacticLocationY;
   private boolean isColony;

   
   public Planet(String name, int index, Random random, int galacticLocationX, int galacticLocationY) {
      this.galacticLocationX = galacticLocationX;
      this.galacticLocationY = galacticLocationY;
      this.planetIndex = index;
      this.name = name;
      this.size = (float) random.nextInt(2000) + 20; // surface area in millions of square kilometers
      this.temperature = (float) (random.nextInt(900) - 273.15);
      this.inhabited = random.nextDouble() < 0.5; // returns random true or false
      this.isColony = false;
      if (inhabited) {
         this.population = (float) (random.nextDouble() * (size * 30) / (Math.abs(temperature) / 30) + 1); // population in millions of people (set up so that the larger the planet and the closer to 0C, the higher the population)
         resources = null;
      }
      else {
         resources = new TreeMap<>(Comparator.reverseOrder()); // creating a resource map of the resources and the percent that can be mined from the planet
         float percentages = 1;
         float nextFloat = random.nextFloat();
         ArrayList<String> resourcesInPlanet = new ArrayList<>(); // duplicate detection list
         boolean removedResource = false; // checker for if a resource has passed the duplication check or not
         while (percentages > 0.005) {
            nextFloat = random.nextFloat();
            while (nextFloat > percentages) { // add more elements to the planet until less than 5% is remaining
               nextFloat = random.nextFloat();
            }

            percentages -= nextFloat; // subtracts percentages of elements until none is left
            double distanceFromStart = galacticLocationX + galacticLocationY;
            resources.put(nextFloat, new Resource(distanceFromStart, random)); // add elements into the table
            if (resourcesInPlanet.size() != 0) {
               for (String s : resourcesInPlanet) {
                  if (resources.get(nextFloat).getElementName().equals(s)) { // detects duplicate resources in a planet and removes them
                     resources.remove(nextFloat);
                     removedResource = true;
                     break;
                  }
               }
            }
            if (!removedResource) {
               resourcesInPlanet.add(resources.get(nextFloat).getElementName()); // adds to the list after passing the duplicate check
            }
            removedResource = false;
         }
      }
      if (population > 1000) { // if population is more than 1 billion, sell new parts. Otherwise, sell used parts
         partsToPurchase = new Part[(int) (random.nextDouble() * population / 1000 * (random.nextDouble() * 5 + 1))]; // number of parts available is influenced by population
         for (int i = 0; i < partsToPurchase.length; i++) {
            int randomValue = random.nextInt(100);
            int randomDurability = (int) (random.nextDouble() * random.nextInt(950) + 50);
            if (randomValue < 25) { // add Thruster
               partsToPurchase[i] = new Thruster(randomDurability, random); // part durability randomized between 50-500
            } // add Shield
            else if (randomValue < 50) {
               partsToPurchase[i] = new Shield(randomDurability, random);
            } // add Blaster
            else if (randomValue < 75) {
               partsToPurchase[i] = new Blaster(randomDurability, random);
            }
            else {
               partsToPurchase[i] = new FuelCell ((float) randomDurability, random);
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

   public boolean isColony() {
      return isColony;
   }

   public void createColony() {
      isColony = true;
   }

   public void removeColony() {
      isColony = false;
   }

   public float getAmountMined(int duration) { // mines planet for resources, then returns how much was mined
      float output = 0;
      Set<Map.Entry<Float, Resource> > entrySet = resources.entrySet(); // uses entryset to allow the TreeMap to be used in a for each loop
      for (Map.Entry<Float, Resource> currentResource : entrySet) {
         output +=  50 * duration * (currentResource.getKey() * currentResource.getValue().getElementValue()); // formula for the amount of a resource mined in kg (10 kg mined in a day)
      }
      return output;
   }

   public TreeMap<Resource, Float> mine(int duration) {
      TreeMap<Resource, Float> output = new TreeMap<>();
      Set<Map.Entry<Float, Resource> > entrySet = resources.entrySet(); // uses entryset to allow the TreeMap to be used in a for each loop
      for (Map.Entry<Float, Resource> currentResource : entrySet) {
         output.put(currentResource.getValue(), 50 * duration * (currentResource.getKey() * currentResource.getValue().getElementValue())); // formula for the amount of a resource mined in kg (10 kg mined in a day)
      }
      return output;
   }

   public TreeMap<Float, String> sortByValue() { // takes resources map and sorts the elements within by value
      TreeMap<Float, String> output = new TreeMap<>(Comparator.reverseOrder());
      Set<Map.Entry<Float, Resource> > entrySet = resources.entrySet(); // uses entryset to allow the TreeMap to be used in a for each loop
      for (Map.Entry<Float, Resource> currentEntry : entrySet) {
         output.put(currentEntry.getValue().getElementValue(), currentEntry.getValue().getElementName());
      }
      return output;
   }

   public String getResourceDetails() {
      String output = "";
      String percentOfResources;
      Set<Map.Entry<Float, Resource> > entrySet = resources.entrySet(); // uses entryset to allow the TreeMap to be used in a for each loop
      for (Map.Entry<Float, Resource> currentEntry : entrySet) {
         percentOfResources = String.format("%.4g", currentEntry.getKey() * 100);
         output += percentOfResources + "% " + currentEntry.getValue() + "\n"; // gets percent and resource and turns it into a readable format
      }
      return output;
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
         TreeMap<Float, String> values = sortByValue();
         String valueOfFirstEntry = "" + values.firstEntry(); // displays the most valuable resource from the planet
         inhabitedString = "Uninhabited\nContains concentrations of " + valueOfFirstEntry.substring(valueOfFirstEntry.indexOf("=") + 1);
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