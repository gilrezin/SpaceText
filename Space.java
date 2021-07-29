import java.util.Comparator;
import java.util.Random;
import java.lang.Math;

// class used by sectors to create a grid
public class Space implements Comparable<Space>{
   private int locationX;
   private int locationY;
   private int sectorX;
   private int sectorY;
   
   public Space(int sectorX, int sectorY, int locationX, int locationY, Random random) {
      this.locationX = locationX;
      this.locationY = locationY;
      this.sectorX = sectorX;
      this.sectorY = sectorY;
   }
   
   public int getLocationX() {
      return locationX;
   }
   
   public int getLocationY() {
      return locationY;
   }

   public int getSectorX() {
      return sectorX;
   }

   public int getSectorY() {
      return sectorY;
   }

   public int getGalacticLocationX() { return sectorX * 21 + locationX - 10; }

   public int getGalacticLocationY() { return sectorY * -21 + locationY - 10; }
   
   public String toString() {
      return "Empty Space";
   }
   
   public String toString(boolean location) {
      return "You are here";
   }
   
   public Planet getPlanet(int index) {
      return null;
   }

   public int getNumOfPlanets() {return 0; }

   public String getPlanetList() {
      return null;
   }

   public String getPlanetList(int index) {
      return null;
   }
   
   public String getName() {
      return null;
   }

   public int compareTo(Space other) { // compares space by their location in a given scan
      //return Comparator.comparing(Space::getSectorY).reversed().thenComparing(Space::getLocationY).thenComparing(Space::getLocationX).thenComparing(Space::getSectorX).compare(this, other); // deprecated, use only for testing
      return Comparator.comparing(Space::getSectorY).reversed().thenComparing(Space::getLocationY).thenComparing(Space::getSectorX).thenComparing(Space::getLocationX).compare(this, other);
   }

}