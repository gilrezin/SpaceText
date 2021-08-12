// class containing player data
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
public class PlayerShip {
   private int currentPlanet;
   private int locationX; // location within a chunk
   private int locationY;
   private int currentSectorX;
   private int currentSectorY;
   private int credits;
   private float fuelRange; // fuel level in light years
   private float maxFuelRange; // max fuel range in light years
   private float amountGatheredThisSession; // amount gathered in one mining session
   private TreeMap<Resource, Float> resourcesGathered = new TreeMap<>(); // TreeMap of all resources mined and their amounts
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
      /* active parts consists of 4 parts:
      0. Thruster (Engine)
      1. Blaster (Weapons)
      2. Shield
      3. Fuel Cell
      */
      this.activeParts.add(new Thruster(300, 1));
      this.activeParts.add(new Blaster(100, 40));
      this.activeParts.add(new Shield(60));
      this.activeParts.add(new FuelCell(32));
      this.spareParts = new ArrayList<Part>();
      this.fuelRange = activeParts.get(3).getCapacity();
      this.maxFuelRange = this.fuelRange;
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
      else if (spareParts.get(index) instanceof FuelCell) {
         activeIndex = 3;
      }
      spareParts.add(activeParts.get(activeIndex)); // adds old part to spare parts
      activeParts.remove(activeIndex);
      activeParts.add(activeIndex, spareParts.get(index)); // adds new part to active parts
      spareParts.remove(index);
      if (activeIndex == 3) {
         this.maxFuelRange = activeParts.get(3).getCapacity();
      }

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

   public void mine(int duration, TreeMap<Resource, Float> resourcesThisSession) {
      this.fuelRange -= 0.25 * duration;
      this.amountGatheredThisSession = 0;
      boolean sameResource = false;
      Set<Map.Entry<Resource, Float>> entrySet = resourcesThisSession.entrySet(); // uses entryset to allow the TreeMap to be used in a for each loop
      for (Map.Entry<Resource, Float> currentResource : entrySet) {
         amountGatheredThisSession += currentResource.getValue(); // amends the amount of resource gathered in the mining session
         Set<Map.Entry<Resource, Float>> entrySet2 = resourcesGathered.entrySet(); // compares every resource just collected to ones already stored
         for (Map.Entry<Resource, Float> storedResource : entrySet2) {
            if (storedResource.getKey() == currentResource.getKey()) {
               resourcesGathered.put(storedResource.getKey(), storedResource.getValue() + currentResource.getValue()); // if duplicate resource, add their amounts together
               sameResource = true; // if the resource is the same, do not add it as a new resource in the TreeMap
            }
         }
         if (!sameResource) { // if mined a new resource, add it to the list
            resourcesGathered.put(currentResource.getKey(), currentResource.getValue());
         }
         sameResource = false;
      }
      if (resourcesGathered.size() == 0) { // if no resources have been gathered yet
         this.resourcesGathered = resourcesThisSession;
      }
   }

   public float getChangeInResourcesGathered() {
      return amountGatheredThisSession;
   }


   public String getAmountOfResourcesGathered() { // body of text equivalent to getTotalAmountOfResourcesGathered()
      String output = "Current Resources Gathered:\n";
      Set<Map.Entry<Resource, Float> > entrySet = resourcesGathered.entrySet(); // uses entryset to allow the TreeMap to be used in a for each loop
      for (Map.Entry<Resource, Float> currentResource : entrySet) {
         output += currentResource.getKey().getElementName() + ": " + currentResource.getValue() + "kg\n";
      }
      if (resourcesGathered.size() == 0) {
         output += "Nothing yet";
      }
      return output;
   }

   public float getValueOfResourcesGathered() {
      float output = 0;
      Set<Map.Entry<Resource, Float> > entrySet = resourcesGathered.entrySet(); // uses entryset to allow the TreeMap to be used in a for each loop
      for (Map.Entry<Resource, Float> currentResource : entrySet) {
         output += currentResource.getKey().getElementValue() * currentResource.getValue();
      }
      return output;
   }

}