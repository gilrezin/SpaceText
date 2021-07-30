// class containing player data
import java.util.ArrayList;
public class PlayerShip {
   private int currentPlanet;
   private int locationX; // location within a chunk
   private int locationY;
   private int currentSectorX;
   private int currentSectorY;
   private int credits;
   private float fuelRange; // fuel level in light years
   private float maxFuelRange; // max fuel range in light years
   private ArrayList<Part> activeParts;
   private ArrayList<Part> spareParts;
   
   public PlayerShip(int credits) {
      this.currentPlanet = 1; // player begins on the second planet
      this.locationX = 10;
      this.locationY = 10;
      this.currentSectorX = 0;
      this.currentSectorY = 0;
      this.credits = credits;
      this.activeParts = new ArrayList<Part>();
      /* active parts consists of 3 parts:
      0. Thruster (Engine)
      1. Blaster (Weapons)
      2. Shield
      */
      this.activeParts.add(new Thruster(300, 1));
      this.activeParts.add(new Blaster(100, 40));
      this.activeParts.add(new Shield(60));
      this.spareParts = new ArrayList<Part>();
      this.fuelRange = 8;
      this.maxFuelRange = 8;
   }
   
   public int getCurrentPlanet() { // get current planet
      return currentPlanet;
   }
   
   public int getLocationX() { // get coordinates
      return locationX;
   }
   
   public int getLocationY() {
      return locationY;
   }
   
   public int getCurrentSectorX() { // get current sector
      return currentSectorX;
   }
   
   public int getCurrentSectorY() {
      return currentSectorY;
   }

   public int getGalacticLocationX() { return currentSectorX * 21 + locationX - 10; } // returns the player's absolute coordinates within the world

   public int getGalacticLocationY() { return currentSectorY * -21 + locationY - 10; }
   
   public int getCredits() { // returns the player's money
      return credits;
   }
   
   public void addCredits(int add) { // adds or subtracts a player's money
      this.credits += add;
   }

   public void repair(int index) {
      spareParts.get(index).repair(); // repairs part
      credits -= spareParts.get(index).getFullDurability() / 3; // subtracts money
   }
   
   public Part getActivePart(int index) { // returns an active part
      return activeParts.get(index);
   }
   
   public Part getSparePart(int index) {
      return spareParts.get(index);
   }

   public String getPartList() {
      String output = "Active Parts:\n";
      for (int i = 0; i < activeParts.size(); i++) { // get active parts
         output += activeParts.get(i).toString(i + 1, "") + "\n";
      }
      if (getNumberOfSpareParts() == 0) { // don't display spare parts if none exist
         output += "\nSpare Parts:\nNone\n\nType \"back\" to go back";
         return output;
      }
      output += "\nSpare Parts:\n";
      for (int i = 0; i < spareParts.size(); i++) {
         output += i + 1 + ". " + spareParts.get(i).toString(i + 1, "") + "\n";
      }

      output += "\nType \"back\" to go back";
      return output;
   }

   public void equipPart(int index) {
      int activeIndex = 0; // index for what part in activeParts should be replaced
      if (spareParts.get(index) instanceof Thruster) { // finds out which part should be replaced
         activeIndex = 0;
      }
      else if (spareParts.get(index) instanceof Blaster) {
         activeIndex = 1;
      }
      else if (spareParts.get(index) instanceof Shield) {
         activeIndex = 2;
      }
      spareParts.add(activeParts.get(activeIndex)); // adds old part to spare parts
      activeParts.remove(activeIndex);
      activeParts.add(activeIndex, spareParts.get(index)); // adds new part to active parts
      spareParts.remove(index);

   }

   public int getNumberOfSpareParts() { return spareParts.size(); }

   public void purchasePart(Part newPart, int price) { // adds part to spare part list
      spareParts.add(newPart);
      credits -= price;
   }

   public int sellPart(int index) { // sells part, returns value sold for
      int output = spareParts.get(index).getValue();
      credits += spareParts.get(index).getValue();
      spareParts.remove(index);
      return output;
   }
   
   public void travel (int newSectorX, int newSectorY, int newX, int newY, int newPlanet) {
      this.fuelRange -= ((1 * getActivePart(0).getFuelEfficiency()) * (Math.sqrt(Math.pow(newX - locationX, 2) + Math.pow(newY - locationY, 2)))); // reduce fuel by amount using distance formula
      getActivePart(0).use((int) (Math.sqrt(Math.pow(newX - locationX, 2) + Math.pow(newY - locationY, 2)))); // reduce durability of thruster
      this.currentSectorX = newSectorX;
      this.currentSectorY = newSectorY;
      this.locationX = newX;
      this.locationY = newY;
      this.currentPlanet = newPlanet;
   }
   
   public String toString() { // NOTE: Change light-year unit of fuel to liters and fuel coefficient to liters per light year
      return "\n\n<Ship Details> \nFuel Level: " + fuelRange + "/" + maxFuelRange + "L\n" + credits + " Credits\nThruster: " + getActivePart(0).getDurability() + "/" + getActivePart(0).getFullDurability() + "\nBlaster: " + getActivePart(1).getDurability() + "/" + getActivePart(1).getFullDurability() + "\nShield: " + getActivePart(2).getDurability() + "/" + getActivePart(2).getFullDurability() + "\n";
   }
   
   public float getFuelLevel() {
      return this.fuelRange;
   }
   
   public float getMaxFuelLevel() {
      return this.maxFuelRange;
   }

   public void setMaxFuelRange(int range) {
      this.maxFuelRange = range;
      this.fuelRange = range;
   }
   
   public void refuel() { // refuels ship
      credits -= (int) (maxFuelRange - fuelRange); // cost to refuel (1 credit per light year)
      fuelRange = maxFuelRange;
   }

}