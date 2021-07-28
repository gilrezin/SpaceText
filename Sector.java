//class representing one sector in starChart. Is made up of type Space and solarSystem.
import java.util.*;


public class Sector {
   private Space[][] grid; // sector grid
   private ArrayList<Space> solarSystems; // list of solar systems in the region
   private TreeMap<Planet, Space> planets; // map containing the most populous solar systems in a given sector
   private Random random;
   
   public Sector(boolean firstSector, String randomSeed, int locationX, int locationY) {
      random = new Random((locationX + randomSeed + locationY).hashCode()); // creates a seed unique to each sector and each galaxy
      int randomNumber;
      this.solarSystems = new ArrayList<Space>(); // initializes list of solar systems
      grid = new Space[21][21]; // creates 21x21 grid & fills with empty space
      
      for (int y = 0; y < grid.length; y++) {
         for (int x = 0; x < grid[y].length; x++) {
            randomNumber = random.nextInt(1000);
            if (randomNumber > (960 + 0.25 * Math.abs(locationX) + 0.25 * Math.abs(locationY)) && solarSystems.size() < 26) { // creates a sparse number of solar systems that thin out from the center of the galaxy
               grid[x][y] = new SolarSystem(locationX, locationY, x, y, random);
               solarSystems.add(grid[x][y]);
            }
            // NOTE: Consider adding a tutorial sector or tutorial solar system that is not procedurally generated
            if (x == 10 & y == 10 & firstSector) { // creates a starting solar system in the first sector
               grid[x][y] = new SolarSystem(locationX, locationY, x, y, random);
               solarSystems.add(grid[x][y]);
            }
         }
      }
      this.planets = new TreeMap<Planet, Space>();

   }
   
   public String toString() {
      String output = "";
      char solarSystemLabel = 'a'; // counts the number of solar systems in the sector
      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[i].length; j++) {
            if (grid[j][i] instanceof SolarSystem) { // check if grid is a SolarSystem
               output += " " + solarSystemLabel + " "; // marks each solar system with a number
               solarSystemLabel++;
            }
            else {
               output += " - "; // add empty space
            }
         }
         output += "\n";
      }
      return output;
   }

   public String toString(int startX, int startY, int endX, int endY) { // toString overload to only generate up to certain values (starting values exclusive, ending values inclusive)
      String output = "";
      for (int i = startY; i < endY; i++) { // (j is x, i is y)
         for (int j = startX; j < endX; j++) {
            if (grid[j][i] instanceof SolarSystem) { // check if grid is a SolarSystem
               output += " 0 "; // marks each solar system with a default value to be changed later
            }
            else {
               output += " - "; // add empty space
            }
         }
         output += "\n";
      }
      return output;
   }
   
   public String toString(int locationX, int locationY) { // toString overload including location awareness
      String output = "";
      int count = 1;
      char solarSystemLabel = 'a'; // counts the number of solar systems in the sector
      for (int i = 0; i < grid.length; i++) { // (j is x, i is y)
         for (int j = 0; j < grid[i].length; j++) {
            if (count == 27) { // switch from capital letters to lowercase letters
               solarSystemLabel = 'a';
            }
            
            if (grid[j][i] instanceof SolarSystem) { // check if grid is a SolarSystem
               if (locationX == j && locationY == i) { // marks ship current location with *
                  output += " * ";
               }
               else {
                  output += " " + solarSystemLabel + " "; // marks each solar system with a number
               }
               solarSystemLabel++;
               count++;
            }
            else {
               output += " - "; // add empty space
            }
         }
         output += "\n";
      }
      return output;
   }
   
   public Space getSpace(int x, int y) {
      return grid[x][y];
   }
   
   public String getSolarSystemList(int x, int y) { // gets a full list of all the solar systems in a sector
      String output = "";
      char label = 'a';
      char columnCount = 'a';
      int count = 1;
      boolean location = false;
      output += solarSystems.size() + " Solar Systems in this sector:\n";
      try {
         for (int i = 0; i < solarSystems.size(); i++) {
            if (solarSystems.get(label - 97).getLocationX() == x && solarSystems.get(label - 97).getLocationY() == y) { // adds a marker for location
               location = true;
            }
            if (count % 3 == 0) { // add to the list and move to the next line
               output += label + ". " + solarSystems.get(label - 97).toString(location) + "\n";
               columnCount++;
               label = columnCount; // reset label
            } else { // add to list and add tabs for formatting
               output += label + ". " + solarSystems.get(label - 97).toString(location) + "\t\t";

               if (solarSystems.size() % 3 == 2 && solarSystems.get(count - 1) != null) { // if divisible by 3 remainder 2
                  label += Math.ceil(solarSystems.size() / 3) + 1;
               } else if (solarSystems.size() % 3 == 0 && solarSystems.get(count - 1) != null) { // if divisible by 3
                  label += Math.ceil(solarSystems.size() / 3);
               } else if (solarSystems.size() % 3 == 1 && solarSystems.get(count - 1) != null) { // if divisible by 3 remainder 1
                  label += Math.ceil(solarSystems.size() / 3) + 1;
               }
            }

            location = false;
            count++; // append count
         }
         return output;
      } catch (Exception IndexOutOfBoundsException) {
         output += "\n"; // adds last line for cases where % 3 == 1
         columnCount++;
         label = columnCount; // reset label
         for (int j = 0; j < 2; j++) {
            if (solarSystems.get(label - 97).getLocationX() == x && solarSystems.get(label - 97).getLocationY() == y) { // adds a marker for location
               location = true;
            }
            output += label + ". " + solarSystems.get(label - 97).toString(location) + "\t\t";
            label += Math.ceil(solarSystems.size() / 3) + 1;
            location = false;
            count++; // append count
         }
         return output;
      }
   }

   public void addSolarSystemsToList(ArrayList<SolarSystem> systems, int startX, int startY, int endX, int endY) { // adds solar systems in the given part of the sector for later use
      for (int y = startY; y < endY; y++) {
         for (int x = startX; x < endX; x++) {
            if (grid[x][y] instanceof SolarSystem) {
               systems.add((SolarSystem) grid[x][y]);
            }
         }
      }
   }
   
   public Space getSolarSystemAtIndex(String input) { // returns a solar system at a given index
      int equivalentNumber = input.charAt(0) - 97; // gets numeric value from letter
      return solarSystems.get(equivalentNumber);
   }

   public String getMostPopulousPlanets(int locationX, int locationY) {
      // assigns planet with solar system
      String output = "";
      for (Space solarSystem : solarSystems) { // adds all planets in sector to list
         for (int j = 1; j <= solarSystem.getNumOfPlanets(); j++) {
            planets.put(solarSystem.getPlanet(j), solarSystem);
         }
      }
      while (planets.size() > 11) { // cuts size of planet list to 10
         planets.remove(planets.firstKey()); // removes the first value
      }
      Set<Map.Entry<Planet, Space>> entries = planets.entrySet(); // creates readable map from TreeMap
      Map.Entry<Planet, Space>[] entryArray = entries.toArray(new Map.Entry[entries.size()]);

      for (int i = 1; i < planets.size(); i++) { // adds to list
         float distance = ((float) Math.sqrt(Math.pow(entryArray[i].getValue().getLocationX() - locationX, 2) + Math.pow(entryArray[i].getValue().getLocationY() - locationY, 2)));
         output += "\n- " + distance + " light years away -" + " \n" + (11 - i) + ": " + entryArray[i].getKey() + "\n";
      }
      return output;
   }

   public int[] getCoordinatesOfPopulousPlanet(int index) {
      int[] output = new int[3];
      Set<Map.Entry<Planet, Space>> entries = planets.entrySet(); // creates readable map from TreeMap
      Map.Entry<Planet, Space>[] entryArray = entries.toArray(new Map.Entry[entries.size()]);
      output[0] = entryArray[11 - index].getValue().getLocationX(); // 0 is X coordinate of solar system
      output[1] = entryArray[11 - index].getValue().getLocationY(); // 1 is Y coordinate of solar system
      output[2] = entryArray[11 - index].getKey().getPlanetIndex() + 1; // 2 is planet index in solar system

      return output;
   }


}