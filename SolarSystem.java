import java.util.ArrayList;
import java.util.Random;
public class SolarSystem extends Space {
   private int numOfPlanets;
   private String solarSystemName;
   private ArrayList<Planet> planets;
   
   public SolarSystem(int sectorX, int sectorY, int locationX, int locationY, Random random) {
      super(sectorX, sectorY, locationX, locationY, random);
      this.solarSystemName = generateName(random);
      this.numOfPlanets = random.nextInt(6) + 2; // 2-7 planets
      this.planets = new ArrayList<Planet>();
      for (int i = 0; i < numOfPlanets; i++) {
         planets.add(new Planet(generateName(random), i, random)); // create planets
      }
   }
   
   public String getName() {
      return solarSystemName;
   }
   
   public int getNumOfPlanets() { // returns number of planets in solar system
      return this.numOfPlanets;
   }
      
   public Planet getPlanet(int index) { // returns planet in solar system
      return planets.get(index - 1); 
   }
   
   public String toString() { // creates shortened readable description
      String output = "System " + solarSystemName + ":";
      while (output.length() < 30) { // formatting
         output += " ";
      }
      output += numOfPlanets + " Planets";
      return output;
   }
      
   public String toString(boolean location) { // creates shortened readable description and is location aware
      double population = 0;
      char scale = 'M';
      String extraSpace = ""; // for formatting
      for (int i = 0; i < planets.size(); i++) { // totals up the solar system's population
         population += planets.get(i).getPopulation();
      }
      if (population > 1000000) {
         population /= 1000000;
         scale = 'T';
      }
      else if (population > 1000) {
         population /= 1000;
         scale = 'B';
      }
      if (population > 100) {
         extraSpace = " ";
      }
      String output = "System " + solarSystemName;
      if (location) {
         output += " (*)";
      }
      output += ":";
      while (output.length() < 25) { // formatting
         output += " ";
      }
      
      //output += numOfPlanets + " Planets, Population: " + String.format("%.3g", population) + scale + extraSpace + " |\t"; // planet info text
      output += numOfPlanets + " Planets " + getGalacticLocationX() + ", " + getGalacticLocationY() + " |\t"; // enable for location debugging
      return output;
   }

   public String getPlanetList() { // creates a readable list of planets
      String output = "";
      for (int i = 0; i < planets.size(); i++) {
         output += (i + 1) + ": " + planets.get(i) + "\n\n";
      }
      return output;
   }
   
   public String getPlanetList(int index) { // creates a readable list of planets with location awareness
      index--;
      String output = "";
      for (int i = 0; i < planets.size(); i++) {
         if (index == i) {
            output += "(You are here)\n";
         }
         output += (i + 1) + ": " + planets.get(i) + "\n\n";
      }
      return output;
   }
   
   public static String generateName(Random random) { // generates random names for places
      /*String characters = "abcdefghijklmnopqrstuvwxyz-'";
      String output = "";
      char x = characters.charAt((char) random.nextInt(27) + 1); // generates first random character
      output = "";
      for (int i = 0; i < random.nextInt(10) + 3; i++) {
         output += x; // adds character to end
         x = characters.charAt((char) random.nextInt(27) + 1); // generates random character
      }*/
      StringBuilder output = new StringBuilder();
      String[] consonantClusters = {"b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z", "cs", "sh", "ch", "sc", "ph", "th", "gh", "kh", "-", "'", "rs"};
      String[] vowelClusters = {"a", "e", "i", "o", "u", "aa", "ea", "ee", "ie", "io", "oo", "ou", "au", "ai", "ei", "ia", "oa", "oe", "ua", "ue"};
      int nameLength = 1;
      while (nameLength <= 2)
         nameLength = random.nextInt(9);
      if (random.nextInt(100) < 10) {
         nameLength = random.nextInt(12); // reduces chance of very long name
      }
      if (random.nextInt(100) == 1) { // reduces chance of very short name
         nameLength = random.nextInt(2);
      }

      boolean beginsWithVowel = random.nextBoolean();
      boolean endsWithVowel = random.nextBoolean();
      if (beginsWithVowel) // begins with vowel
         output.append(vowelClusters[random.nextInt(vowelClusters.length)]);
      else // begins with consonant
         output.append(consonantClusters[random.nextInt(consonantClusters.length)]);
      while (nameLength > output.length()) {// generates name based on consonant and vowel clusters
         if (beginsWithVowel) {
            output.append(consonantClusters[random.nextInt(consonantClusters.length)]);
            output.append(vowelClusters[random.nextInt(vowelClusters.length)]);
         }
         else {
            output.append(vowelClusters[random.nextInt(vowelClusters.length)]);
            output.append(consonantClusters[random.nextInt(consonantClusters.length)]);
         }
         if (random.nextInt(100) < 10) { // 10% chance of using another consonant
            output.append(consonantClusters[random.nextInt(consonantClusters.length)]);
         }
      }
      if (random.nextInt(100) == 1 && nameLength <= 9) // low chance of adding " Prime" to planet name (ie Rakata Prime)
         output.append(" Prime");
      else if (nameLength == 0) {
         if (beginsWithVowel) {
            output.append(consonantClusters[random.nextInt(consonantClusters.length)]);
         }
         else {
            output.append(vowelClusters[random.nextInt(vowelClusters.length)]);
         }
      }
      try {
         output.setCharAt(0, Character.toUpperCase(output.charAt(0))); // set first letter to uppercase
      }
      catch (Exception StringIndexOutOfBoundsException) {
      }
      return "" + output;
   }

}
