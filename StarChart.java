// Chart keeping track of every visited sector
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class StarChart {
   private HashMap<Entry<Integer, Integer>, Sector> starChart;
   private Random random;
   private String seed;
   private ArrayList<SolarSystem> systems;
   private TreeMap<Planet, Space> planets; // map containing the most populous solar systems in a given scan
   private ArrayList<Planet> Starlist;
   public StarChart(String seed, int startingLocationX, int startingLocationY) {
      this.random = new Random(); // creates random variable to be used by world generation
      this.random.setSeed(seed.hashCode());
      this.seed = seed;
      planets = new TreeMap<>();
      starChart = new HashMap<>();
      starChart.put(new AbstractMap.SimpleEntry(startingLocationX, startingLocationY), new Sector(true, seed, startingLocationX, startingLocationY));
      systems = new ArrayList<>(); // ArrayList tracking viewable solar systems in every new sector map
      Starlist = new ArrayList<>();
   }

   public Sector getSector(int x, int y) { // returns the designated Sector
      return starChart.get(new AbstractMap.SimpleEntry(x, y));
   }
   
   public String sectorCount() {
      int sectorCount = starChart.size();
      if (sectorCount == 1) {
         return "You have visited 1 sector.";
      }
      else {
         return "You have visited " + sectorCount + "sectors.";
      }
   }
   /* method for generating sector map:
   1. Find all necessary sectors to be generated. If some sectors do not exist, create them.
   2. Patch all sectors together using toString into a String called output.
   3. Replace all default labels with their proper values. Add * in the center to represent the player.
    */
   public String generateSectorMap(int sectorX, int sectorY, int x, int y) { // generates a map 10 points out in all directions from the player and a list of systems
      StringBuilder output = new StringBuilder("");
      systems.clear();
      ArrayList<String> stringsToCombine = new ArrayList<String>();
      boolean[] generateAdditionalSectors = new boolean[9]; // order: up, right, down, left, top-right, bottom-right, bottom-left, top-left
      // finds all necessary sectors to be generated, generates new sectors as necessary, then finds the necessary combinations

      if (y + 10 >= 21) { // down
         generateAdditionalSectors[1] = true;
         if (getSector(sectorX, sectorY - 1) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX, sectorY - 1), new Sector(false, seed, sectorX, sectorY - 1));
         }
         stringsToCombine.add(getSector(sectorX, sectorY).toString(0, y - 10, 21, 21));
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, 0, y - 10, 21, 21); // adds to solar system list used in creating the key
         stringsToCombine.add(getSector(sectorX, sectorY - 1).toString(0, 0, 21, y - 10));
         getSector(sectorX, sectorY - 1).addSolarSystemsToList(systems, 0, 0, 21, y - 10);
      }
      if (y - 10 < 0) { // up
         generateAdditionalSectors[3] = true;
         if (getSector(sectorX, sectorY + 1) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX, sectorY + 1), new Sector(false, seed, sectorX, sectorY + 1));
         }
         stringsToCombine.add(getSector(sectorX, sectorY + 1).toString(0, 21 + (y - 10), 21, 21));
         getSector(sectorX, sectorY + 1).addSolarSystemsToList(systems, 0, 21 + (y - 10), 21, 21);
         stringsToCombine.add(getSector(sectorX, sectorY).toString(0, 0, 21, 21 + (y - 10)));
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, 0, 0, 21, 21 + (y - 10));
      }
      if (x - 10 < 0) { // left
         generateAdditionalSectors[2] = true;
         if (getSector(sectorX - 1, sectorY) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX - 1, sectorY), new Sector(false, seed, sectorX - 1, sectorY));
         }
         stringsToCombine.add(getSector(sectorX - 1, sectorY).toString(21 + (x - 10), 0, 21, 21));
         getSector(sectorX - 1, sectorY).addSolarSystemsToList(systems, 21 + (x - 10), 0, 21, 21);
         stringsToCombine.add(getSector(sectorX, sectorY).toString(0, 0, 21 + (x - 10), 21));
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, 0, 0, 21 + (x - 10), 21);
      }
      if (x + 10 >= 21) { // right
         generateAdditionalSectors[4] = true;
         if (getSector(sectorX + 1, sectorY) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX + 1, sectorY), new Sector(false, seed, sectorX + 1, sectorY));
         }
         stringsToCombine.add(getSector(sectorX, sectorY).toString(x - 10, 0, 21, 21));
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, x - 10, 0, 21, 21);
         stringsToCombine.add(getSector(sectorX + 1, sectorY).toString(0, 0, x - 10, 21));
         getSector(sectorX + 1, sectorY).addSolarSystemsToList(systems, 0, 0, x - 10, 21);

      }

      if ((generateAdditionalSectors[1] && generateAdditionalSectors[2]) || (generateAdditionalSectors[2] && generateAdditionalSectors[3]) || (generateAdditionalSectors[3] && generateAdditionalSectors[4]) || (generateAdditionalSectors[4] && generateAdditionalSectors[1])) {
         stringsToCombine.clear(); // clears sector and system list if a corner sector is to be generated.
         systems.clear();
         //System.out.println("lines cleared");
         //System.out.println("New Ship X: " + x + "\nNew Ship Y: " + y);
      }

      if (generateAdditionalSectors[3] && generateAdditionalSectors[4]) { // top right
         generateAdditionalSectors[5] = true;
         if (getSector(sectorX + 1, sectorY + 1) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX + 1, sectorY + 1), new Sector(false, seed, sectorX + 1, sectorY + 1));
         }
         stringsToCombine.add(getSector(sectorX, sectorY + 1).toString(x - 10, 21 + (y - 10), 21, 21)); // top left quadrant
         getSector(sectorX, sectorY + 1).addSolarSystemsToList(systems, x - 10, 21 + (y - 10), 21, 21);
         stringsToCombine.add(getSector(sectorX + 1, sectorY + 1).toString(0, 21 + (y - 10), x - 10, 21)); // top right quadrant
         getSector(sectorX + 1, sectorY + 1).addSolarSystemsToList(systems, 0, 21 + (y - 10), x - 10, 21);
         stringsToCombine.add(getSector(sectorX, sectorY).toString(x - 10, 0, 21, 21 + (y - 10))); // bottom left quadrant
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, x - 10, 0, 21, 21 + (y - 10));
         stringsToCombine.add(getSector(sectorX + 1, sectorY).toString(0, 0, x - 10, 21 + (y - 10))); // bottom right quadrant
         getSector(sectorX + 1, sectorY).addSolarSystemsToList(systems, 0, 0, x - 10, 21 + (y - 10));
      } else if (generateAdditionalSectors[1] && generateAdditionalSectors[4]) { // bottom right
         generateAdditionalSectors[6] = true;
         if (getSector(sectorX + 1, sectorY - 1) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX + 1, sectorY - 1), new Sector(false, seed, sectorX + 1, sectorY - 1));
         }
         stringsToCombine.add(getSector(sectorX, sectorY).toString(x - 10, y - 10, 21, 21)); // top left quadrant
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, x - 10, y - 10, 21, 21);
         stringsToCombine.add(getSector(sectorX + 1, sectorY).toString(0, y - 10, x - 10, 21)); // top right quadrant
         getSector(sectorX + 1, sectorY).addSolarSystemsToList(systems, 0, y - 10, x - 10, 21);
         stringsToCombine.add(getSector(sectorX, sectorY - 1).toString(x - 10, 0, 21, y - 10)); // bottom left quadrant
         getSector(sectorX, sectorY - 1).addSolarSystemsToList(systems, x - 10, 0, 21, y - 10);
         stringsToCombine.add(getSector(sectorX + 1, sectorY - 1).toString(0, 0, x - 10, y - 10)); // bottom right quadrant
         getSector(sectorX + 1, sectorY - 1).addSolarSystemsToList(systems, 0, 0, x - 10, y - 10);
      } else if (generateAdditionalSectors[1] && generateAdditionalSectors[2]) { // bottom left
         generateAdditionalSectors[7] = true;
         if (getSector(sectorX - 1, sectorY - 1) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX - 1, sectorY - 1), new Sector(false, seed, sectorX - 1, sectorY - 1));
         }
         stringsToCombine.add(getSector(sectorX - 1, sectorY).toString(21 + (x - 10), y - 10, 21, 21)); // top left quadrant
         getSector(sectorX - 1, sectorY).addSolarSystemsToList(systems, 21 + (x - 10), y - 10, 21, 21);
         stringsToCombine.add(getSector(sectorX, sectorY).toString(0, y - 10, 21 + (x - 10), 21)); // top right quadrant
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, 0, y - 10, 21 + (x - 10), 21);
         stringsToCombine.add(getSector(sectorX - 1, sectorY - 1).toString(21 + (x - 10), 0, 21, y - 10)); // bottom left quadrant
         getSector(sectorX - 1, sectorY - 1).addSolarSystemsToList(systems, 21 + (x - 10), 0, 21, y - 10);
         stringsToCombine.add(getSector(sectorX, sectorY - 1).toString(0, 0, 21 + (x - 10), y - 10)); // bottom right quadrant
         getSector(sectorX, sectorY - 1).addSolarSystemsToList(systems, 0, 0, 21 + (x - 10), y - 10);
      } else if (generateAdditionalSectors[3] && generateAdditionalSectors[2]) { // top left
         generateAdditionalSectors[8] = true;
         if (getSector(sectorX - 1, sectorY + 1) == null) { // generates new sector if it does not exist
            starChart.put(new AbstractMap.SimpleEntry(sectorX - 1, sectorY + 1), new Sector(false, seed, sectorX - 1, sectorY + 1));
         }
         stringsToCombine.add(getSector(sectorX - 1, sectorY + 1).toString(21 + (x - 10), 21 + (y - 10), 21, 21)); // top left quadrant
         getSector(sectorX - 1, sectorY + 1).addSolarSystemsToList(systems, 21 + (x - 10), 21 + (y - 10), 21, 21);
         stringsToCombine.add(getSector(sectorX, sectorY + 1).toString(0, 21 + (y - 10), 21 + (x - 10), 21)); // top right quadrant
         getSector(sectorX, sectorY + 1).addSolarSystemsToList(systems, 0, 21 + (y - 10), 21 + (x - 10), 21);
         stringsToCombine.add(getSector(sectorX - 1, sectorY).toString(21 + (x - 10), 0, 21, 21 + (y - 10))); // bottom left quadrant
         getSector(sectorX - 1, sectorY).addSolarSystemsToList(systems, 21 + (x - 10), 0, 21, 21 + (y - 10));
         stringsToCombine.add(getSector(sectorX, sectorY).toString(0, 0, 21 + (x - 10), 21 + (y - 10))); // bottom right quadrant
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, 0, 0, 21 + (x - 10), 21 + (y - 10));
      } else if (!generateAdditionalSectors[1] && !generateAdditionalSectors[2] && !generateAdditionalSectors[3] && !generateAdditionalSectors[4]) { // if in the center of the map
         stringsToCombine.add(getSector(sectorX, sectorY).toString(0, 0, 21, 21));
         getSector(sectorX, sectorY).addSolarSystemsToList(systems, 0, 0, 21, 21);
      }


      // patches all Strings together to form cohesive map
      ArrayList<String[]> lines = new ArrayList<>(); // array of strings[] to combine
      for (String s : stringsToCombine) {
         lines.add(s.split("\n")); // splits String by line
      }

      String[] combine = new String[21];

      // combine corner parts together by stitching 4 quadrants together
      //System.out.println("Number of quadrants loaded: " + lines.size()); // sector debugging tool
      if (generateAdditionalSectors[5] || generateAdditionalSectors[6] || generateAdditionalSectors[7] || generateAdditionalSectors[8]) {
         //System.out.println(lines.size());
         int index = 0;
         for (; index < lines.get(0).length; index++) {
            combine[index] = lines.get(0)[index] + lines.get(1)[index]; // combines top row
         }
         for (int i = 0; index < 21; index++) {
            combine[index] = lines.get(2)[i] + lines.get(3)[i]; // combines bottom row
            i++;
         }
      }

      // combine each horizontal row together (ex: combine[0][0] with combine[1][0])
      else if (generateAdditionalSectors[2] || generateAdditionalSectors[4]) { // horizontal connections
         for (int i = 0; i < 21; i++) {
            combine[i] = lines.get(0)[i] + lines.get(1)[i]; // combines both lines (for horizontal connections)
         }
      } else if (generateAdditionalSectors[1] || generateAdditionalSectors[3]) {
         int index = 0;
         for (int i = 0; i < lines.get(0).length; i++) { // vertical connections
            combine[index] = lines.get(0)[index];
            index++;
         }
         for (int i = 0; i < lines.get(1).length; i++) {
            combine[index] = lines.get(1)[index - lines.get(0).length];
            index++;
         }

      } else {
         for (int i = 0; i < 21; i++) {
            combine[i] = lines.get(0)[i];
         }
      }

      //NOTE: Add in cases for corner parts

      // combine rows into single String
      for (String s : combine) {
         output.append(s + "\n");
      }

      // add labels for planets
      // finds instances of placeholder "0", replacing with label, appending label after every use
      char label = 'a';
      for (int i = 0; i < output.length(); i++) {
         if (output.charAt(i) == '0') {
            output.setCharAt(i, label);
            label++;
         }
      }
      output.setCharAt(output.length() / 2 - 1, '*'); // places player in center (index 671)
      output.append("\n\n"); // spacing in between map and list

      //add list of planets
      label = 'a';
      char columnCount = 'a';
      int count = 1;
      boolean location = false;
      output.append(systems.size() + " Nearby Solar Systems:\n");

      Collections.sort(systems);
      try {
         for (int i = 0; i < systems.size(); i++) {
            if (systems.get(label - 97).getLocationX() == x && systems.get(label - 97).getLocationY() == y) { // adds a marker for location
               location = true;
            }
            if (count % 3 == 0) { // add to the list and move to the next line
               output.append(label + ". " + systems.get(label - 97).toString(location) + "\n");
               columnCount++;
               label = columnCount; // reset label
            } else { // add to list and add tabs for formatting
               output.append(label + ". " + systems.get(label - 97).toString(location) + "\t\t");
               if (systems.size() % 3 == 2 && systems.get(count - 1) != null) { // if divisible by 3 remainder 2
                  label += Math.ceil(systems.size() / 3) + 1;
               } else if (systems.size() % 3 == 0 && systems.get(count - 1) != null) { // if divisible by 3
                  label += Math.ceil(systems.size() / 3);
               } else if (systems.size() % 3 == 1 && systems.get(count - 1) != null) { // if divisible by 3 remainder 1
                  label += Math.ceil(systems.size() / 3) + 1;
               }
            }
               location = false;
               count++; // append count
            }
         return "" + output;
      } catch (Exception IndexOutOfBoundsException) {
         output.append("\n"); // adds last line for cases where % 3 == 1
         columnCount++;
         label = columnCount; // reset label
         for (int j = 0; j < 2; j++) {
            if (systems.get(label - 97).getLocationX() == x && systems.get(label - 97).getLocationY() == y) { // adds a marker for location
               location = true;
            }
            output.append(label + ". " + systems.get(label - 97).toString(location) + "\t\t");
            label += Math.ceil(systems.size() / 3) + 1;
            location = false;
            count++; // append count
         }
         return "" + output;
      }
   }

   public Space getSolarSystemAtIndex(String input) { // returns a solar system at a given index
      int equivalentNumber = input.charAt(0) - 97; // gets numeric value from letter
      return systems.get(equivalentNumber);
   }

   public String getMostPopulousPlanets(int locationX, int locationY) {
      // assigns planet with solar system
      planets.clear();
      String output = "";
      for (Space solarSystem : systems) { // adds all planets in sector to list
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
         output += "\n- " + distance + " light years away -\n" + (11 - i) + ": " + entryArray[i].getKey() + "\n";
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

   public String getStarlist(int x, int y) {
      String output = "";
      int index = 1;
      if (Starlist.size() == 0) {
         output += "-------------------------\nNothing added to Starlist\n-------------------------";
      }
      for (Planet p : Starlist) {
         float distance = ((float) Math.sqrt(Math.pow(p.getGalacticLocationX() - x, 2) + Math.pow(p.getGalacticLocationY() - y, 2))); // distance formula (x2 is the new galactic location, x1 is the old one)
         output += "\n- " + distance + " light years away -\n" + index + ": " + p.toString() + "\n";
         index++;
      }
      return output;
   }

   public Planet getStarlistAtIndex(int input) {
      return Starlist.get(input - 1);
   }

   public void addToStarlist(int x, int y, int planet) {
      for (SolarSystem s : systems) {
         if (s.getLocationX() == x) { // finds the SolarSystem that aligns with the current coordinates, then saves those coordinates to Starlist
            Starlist.add(s.getPlanet(planet));
            break;
         }
      }
   }

   
   public String toString() { // may add sector map later, WIP
      return null;
   }
}