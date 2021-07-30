// main class that handles game logic.
/* Context menu index:
0: Title screen
1: Tutorial
2: Beginning planet text
3: trading stop menu
4: player response to trading stop menu
5: travel menu
6: travel "are you sure" message
7: travelling message
8: uninhabited planet options
9: player response to colony stop menu
10: shop menu
11: refuel menu
12: purchase items message
13: Spare parts menu
14: Ship menu
15: shop sell menu
16: sell items message
17: Nearby Planets menu
 */
import java.util.HashMap;
import java.util.Random;

public class Game {
   private StarChart starChart;
   private PlayerShip ship;
   private int contextMenu; // tells Menu object which text to display
   private int travelToSectorX;
   private int travelToSectorY;
   private int travelToX;
   private int travelToY;
   private float travelDistance;
   private int travelToPlanet;
   private int selectedPart;
   private boolean nearbyPlanets;
   private boolean mostPopulousPlanets;
   private boolean starlist;
   private Database help;
   private final String inhabitedPlanetText;
   private final String uninhabitedPlanetText;
   private final String incorrectInputText;
   private String starlistText;
   private String travelMenuText;
   private Random random; // provides random values for use by other random objects
   //private int seed = ("q").hashCode();
   private int seed;
   
   public Game() {
      this.ship = new PlayerShip(300); // create a player ship with 300 credits
      //this.starChart = new StarChart("" + random.nextDouble(), ship.getCurrentSectorX(), ship.getCurrentSectorY()); // generate world and pass on a seed to StarChart's random variable
      this.contextMenu = 0;
      this.help = new Database(0); // get data for help menus
      this.inhabitedPlanetText = "\n1. Shop  2. Nearby Planets  3. Leave  4. Refuel  5. Ship  6. Starlist";
      this.uninhabitedPlanetText = "\n1. Colony  2. Nearby Planets  3. Leave  4. Starlist";
      this.incorrectInputText = "Type the letter/number corresponding to the option you want to select.";
      this.starlistText = "\n\n+. Add current planet to Starlist  -. Back";
      //this.travelMenuText = starChart.generateSectorMap(ship.getCurrentSectorX(), ship.getCurrentSectorY(), ship.getLocationX(), ship.getLocationY()) + "\n\nWhere to?\nType \"+\" for most populous planets in this sector"; // display travel menu within sector
   }
   
   public String processCommand(String input) {
      if (input.equalsIgnoreCase("seed")) {
         return "" + seed;
      }
      if (input.equalsIgnoreCase("help")) { // back button
            return help.get(contextMenu);
         }
      if (input.equalsIgnoreCase("debug")) {
         ship.setMaxFuelRange(99999);
         ship.addCredits(99999);
         return "debug mode enabled";
      }
      if (input.equalsIgnoreCase("location")) {
         return "(" + (ship.getCurrentSectorX() * 21 + ship.getLocationX() - 10) + ", " + (ship.getCurrentSectorY() * -21 + ship.getLocationY() - 10) + ")";
      }
      if (input.equalsIgnoreCase("sector")) {
         return "(" + ship.getCurrentSectorX() + ", " + ship.getCurrentSectorY() + ")";
      }
      if (input.equalsIgnoreCase("system")) {
         return getCurrentSolarSystem().toString();
      }
      if (input.equalsIgnoreCase("back")) { // back button
         switch(contextMenu) {
            case 5:
            case 17:
               if (getCurrentPlanet().isInhabited()) {
                  contextMenu = 4;
                  return getCurrentPlanet() + ship.toString() + inhabitedPlanetText;
               }
               else {
                  contextMenu = 9;
                  return getCurrentPlanet() + ship.toString() + uninhabitedPlanetText;
               }
            case 6:
               contextMenu--; // go back to solar system list
               return travelMenuText;
            case 7:
               if (nearbyPlanets) { // if came from nearby planets menu
                  contextMenu = 17;
                  return getCurrentSolarSystem().getPlanetList(ship.getCurrentPlanet());
               }
               if (mostPopulousPlanets) { // if came from most populous planets menu
                  contextMenu = 6;
                  return starChart.getMostPopulousPlanets(ship.getLocationX(), ship.getLocationY());
               }
               if (starlist) {
                  contextMenu = 19;
                  return starChart.getStarlist(ship.getGalacticLocationX(), ship.getGalacticLocationY()) + starlistText;
               }
               contextMenu--; // go back to planet list
               return getSolarSystem(travelToSectorX, travelToSectorY, travelToX, travelToY).toString() + "\n\n" + getSolarSystem(travelToSectorX, travelToSectorY, travelToX, travelToY).getPlanetList();
            case 10:
               contextMenu = 4; // go back to menu
               return getCurrentPlanet() + ship.toString() + inhabitedPlanetText;
            case 12:
            case 15:
               contextMenu = 10; // go back to menu
               return "1. Buy  2. Sell  3. Exit";
            case 13: // back out of spare parts menu
            case 19: // back out of starlist menu
               if (starlist) {
                  starlist = false;
               }
               if (getCurrentPlanet().isInhabited()) {
                  contextMenu = 4;
                  return getCurrentPlanet() + ship.toString() + inhabitedPlanetText;
               }
               else {
                  contextMenu = 9;
                  return uninhabitedPlanetText;
               }
            case 14:
               contextMenu--;
               return ship.getPartList();
            case 16:
               contextMenu--;
               return "1. Parts  2. Resources  3. Exit";

         }
      }

      switch (contextMenu) {
         case 0: // Title screen
            contextMenu = 18; // send to seed menu
            return "\\\\SpaceText//\nA game by Gil Rezin\nPress Enter to begin.";

         case 1: // Tutorial and initializing world
            this.seed = input.hashCode();
            this.random = new Random(seed);
            this.starChart = new StarChart("" + random.nextDouble(), ship.getCurrentSectorX(), ship.getCurrentSectorY()); // generate world and pass on a seed to StarChart's random variable
            this.travelMenuText = starChart.generateSectorMap(ship.getCurrentSectorX(), ship.getCurrentSectorY(), ship.getLocationX(), ship.getLocationY()) + "\n\nWhere to?\nType \"+\" for most populous planets in this sector"; // display travel menu within sector
            contextMenu++;
            return "Welcome to SpaceText! You are a small interplanetary mining contractor. Your work consists of bringing rare materials from uninhabited worlds to habited ones for a profit.\nYour goal should be to make as much money as possible.\nIf you get stuck, type \"help\" for assistance.";

         case 2: // Beginning planet text
            String inhabitedString = "inhabited";
            if (getCurrentPlanet().isInhabited()) {
               contextMenu = 3;
            } else {
               inhabitedString = "uninhabited";
               contextMenu = 8;
            }
            return "You begin your journey on the " + inhabitedString + " planet " + getCurrentPlanet().getName() + " in the Solar System " + getCurrentSolarSystem().getName() + ", near the center of the galaxy."; // location info

         case 3: // trading stop menu
            contextMenu++;
            return getCurrentPlanet() + ship.toString() + inhabitedPlanetText; // info text

         case 4: // player response to trading stop menu
            switch (input) {
               case "1":
                  contextMenu = 10; // shop menu
                  return "1. Buy  2. Sell  3. Exit";
               case "2":
                  contextMenu = 17; // nearby planets menu
                  return getCurrentSolarSystem().getPlanetList(ship.getCurrentPlanet()); // solar system info & display previous info again
               case "3":
                  contextMenu = 5; // travel menu
                  return travelMenuText;
               case "4":
                  if (ship.getFuelLevel() / ship.getMaxFuelLevel() == 1.0) {
                     return "Ship is already fueled!";
                  } else {
                     contextMenu = 11; // go to refuel menu
                     if ((int) (ship.getMaxFuelLevel() - ship.getFuelLevel()) == 1) {
                        return "Price: 1 Credit/light year\nYou will be charged 1 Credit.\nOk? (y/n)"; // refuel text if it only costs one credit
                     } else {
                        return "Price: 1 Credit/light year\nYou will be charged " + ((int) (ship.getMaxFuelLevel() - ship.getFuelLevel())) + " Credits.\nOk? (y/n)"; // refuel text
                     }
                  }
               case "5":
                  contextMenu = 13;
                  return ship.getPartList();
               case "6":
                  contextMenu = 19;
                  return starChart.getStarlist(ship.getGalacticLocationX(), ship.getGalacticLocationY()) + starlistText;

               default:
                  return incorrectInputText;
            }
         case 5: // travel menu
            mostPopulousPlanets = false;
            if (input.equals("+")) {
               contextMenu++;
               mostPopulousPlanets = true;
               return starChart.getMostPopulousPlanets(ship.getLocationX(), ship.getLocationY());
            }
            try {
               if (input.length() > 1) { // allow only single letters to be entered
                  return incorrectInputText;
               }
               contextMenu++;
               this.travelToX = starChart.getSolarSystemAtIndex(input).getLocationX(); // set coordinates for where to travel to
               this.travelToY = starChart.getSolarSystemAtIndex(input).getLocationY();
               if (starChart.getSolarSystemAtIndex(input).getSectorX() != ship.getCurrentSectorX() || starChart.getSolarSystemAtIndex(input).getSectorY() != ship.getCurrentSectorY()) { // if the new location is located in a different sector, switch to that sector
                  this.travelToSectorX = starChart.getSolarSystemAtIndex(input).getSectorX();
                  this.travelToSectorY = starChart.getSolarSystemAtIndex(input).getSectorY();
               }
               System.out.println("System coords: (" + travelToX + ", " + travelToY + ")");
               return starChart.getSolarSystemAtIndex(input).toString() + "\n\n" + starChart.getSolarSystemAtIndex(input).getPlanetList(); // return planet list
            } catch (Exception IndexOutOfBoundsException) {
               contextMenu--;
               return incorrectInputText;
            }
         case 6: // travel message & most populous planets travel menu
            try {
               if (mostPopulousPlanets) { // most populous planets travel menu
                  int[] coordinates = starChart.getCoordinatesOfPopulousPlanet(Integer.parseInt(input));
                  travelToX = coordinates[0];
                  travelToY = coordinates[1];
                  travelToPlanet = coordinates[2];
               }
               else {
                  this.travelToPlanet = Integer.parseInt(input); // sets planet to travel to
               }
               contextMenu++;
               this.travelDistance = ((float) Math.sqrt(Math.pow((travelToSectorX * 21 + travelToX - 10) - ship.getGalacticLocationX(), 2) + Math.pow((travelToSectorY * -21 + travelToY - 10) - ship.getGalacticLocationY(), 2))); // distance formula (x2 is the new galactic location, x1 is the old one)
               //System.out.println((travelToSectorY * 21 + travelToY - 10) - ship.getGalacticLocationY()); // debug
               String distanceUnit = " light years ";
               if (travelDistance == 1.0) {
                  distanceUnit = " light year ";

               }
               return "Travel to:\nSolar System " + getSolarSystem(travelToSectorX, travelToSectorY, travelToX, travelToY).getName() + "\nPlanet " + getPlanet(travelToSectorX, travelToSectorY, travelToX, travelToY, travelToPlanet).getName() + "\n" + travelDistance + distanceUnit + "away" + "\nYou will use " + ((1 * ship.getActivePart(0).getFuelEfficiency()) * travelDistance + "L of fuel\n(y/n)"); //returns solar system name, planet name, and distance using distance formula
            } catch (Exception IllegalArgumentException) {
               // add case for if random string is entered
               if (getSolarSystem(travelToSectorX, travelToSectorY, travelToX, travelToY).getNumOfPlanets() < travelToPlanet) { // if planet number entered is greater than the number of planets
                  contextMenu--;
               }
               return incorrectInputText;
            }
         case 7: // travel
            if (input.equalsIgnoreCase("y")) {
               mostPopulousPlanets = false;
               if (travelDistance * ship.getActivePart(0).getFuelEfficiency() > ship.getFuelLevel()) { // if distance is greater than amount of fuel
                  return "You don't have enough fuel! You need " + String.format("%.3g", travelDistance * ship.getActivePart(0).getFuelEfficiency()) + "L. Fuel level is " + ship.getFuelLevel() + "L"; // display not enough fuel message
               } else if (travelDistance > (ship.getActivePart(0).getDurability())) { // if distance is greater than engine durability
                  return "Your engine's thruster is too weak and needs repair!";
               }
               contextMenu++;
               ship.travel(travelToSectorX, travelToSectorY, travelToX, travelToY, travelToPlanet); // set player location to new place
               //System.out.println(ship.getCurrentSectorX() + " " + ship.getCurrentSectorY()); // enable for sector debugging
               if (getPlanet(travelToSectorX, travelToSectorY, travelToX, travelToY, travelToPlanet).isInhabited()) {
                  contextMenu = 3;
               } else {
                  contextMenu = 8;
               }
               starlist = false;
               this.travelMenuText = starChart.generateSectorMap(ship.getCurrentSectorX(), ship.getCurrentSectorY(), ship.getLocationX(), ship.getLocationY()) + "\n\nWhere to?\nType \"+\" for most populous planets in this sector"; // update travel menu to display new position
               return "You head for the planet " + getPlanet(travelToX, travelToY, travelToPlanet).getName() + " in the solar system " + getSolarSystem(travelToX, travelToY).getName() + ".\n\n";
            } else if (input.equalsIgnoreCase("n")) {
               contextMenu--; // go back to planet list
               if (mostPopulousPlanets) {
                  return starChart.getMostPopulousPlanets(ship.getLocationX(), ship.getLocationY());
               }
               if (starlist) {
                  contextMenu = 19;
                  return starChart.getStarlist(ship.getGalacticLocationX(), ship.getGalacticLocationY()) + starlistText;

               }
               return getSolarSystem(travelToSectorX, travelToSectorY, travelToX, travelToY).toString() + "\n\n" + getSolarSystem(travelToSectorX, travelToSectorY, travelToX, travelToY).getPlanetList();
            } else {
               return incorrectInputText;
            }
         case 8: // uninhabited planet options
            contextMenu++;
            return getCurrentPlanet() + ship.toString() + uninhabitedPlanetText; // info text
         case 9: // player response to colony stop menu
            switch (input) {
               case "1":
                  // colony menu, add contextMenu pointer value later
                  return "No options yet";
               case "2":
                  contextMenu = 17; // nearby planets menu
                  return getCurrentSolarSystem().getPlanetList(ship.getCurrentPlanet()); // solar system info & display previous info again
               case "3":
                  contextMenu = 5; // travel menu
                  return travelMenuText;
               case "4":
                  contextMenu = 19;
                  return starChart.getStarlist(ship.getGalacticLocationX(), ship.getGalacticLocationY()) + starlistText;
               default:
                  return incorrectInputText;

            }
         case 10: // shop menu
            switch (input) {
               case "1":
                  contextMenu = 12; // go to purchase items menu
                  return getCurrentPlanet().getPartList() + "\nYou have " + ship.getCredits() + " Credits";
               case "2":
                  contextMenu = 15;
                  return "1. Parts  2. Resources  3. Exit";
               case "3":
                  contextMenu = 4;
                  return getCurrentPlanet() + ship.toString() + inhabitedPlanetText;
               default:
                  return incorrectInputText;
         }

         case 11: // refuel menu
            if (ship.getCredits() < ((int) (ship.getMaxFuelLevel() - ship.getFuelLevel())) && input.equalsIgnoreCase("y")) { // if insufficient funds and answered yes
               return "Insufficient credits";
            } else if (input.equalsIgnoreCase("y")) {
               ship.refuel();
            }
            contextMenu = 4;
            return getCurrentPlanet() + ship.toString() + inhabitedPlanetText;

         case 12: // purchase items message
            try {
               Part partToPurchase = getCurrentPlanet().getPart(Integer.parseInt(input) - 1); // gets part to purchase from list
               if (ship.getCredits() >= partToPurchase.getValue()) { // only purchase if player has sufficient funds
                  ship.purchasePart(partToPurchase, partToPurchase.getValue());
                  return getCurrentPlanet().getPartList() + "\n" + "Purchased!\n-" + partToPurchase.getValue() + " Credits\nYou have " + ship.getCredits() + " Credits";
               } else {
                  return "Insufficient Funds";
               }
            } catch (Exception IllegalArgumentException) {
               return incorrectInputText;
            }
         case 13: // Spare parts menu
            try {
            selectedPart = Integer.parseInt(input) - 1;
            contextMenu = 14;
            return "1. Equip  2. Repair (" + (ship.getSparePart(selectedPart).getFullDurability() / 3) + " Credits)  3. Back";
            }
            catch (Exception IllegalArgumentException) { // if entered incorrectly
               contextMenu--;
               return incorrectInputText;
            }
         case 14: // ship menu
            switch (input) {
               case "1": // equip selected part
                  ship.equipPart(selectedPart);
                  contextMenu = 13;
                  return ship.getPartList();
               case "2": // repair selected part
                  if (ship.getCredits() <= (ship.getSparePart(selectedPart).getFullDurability() / 3)) {// if insufficient funds
                     contextMenu = 13;
                     return "Insufficient Funds\n1. Equip  2. Repair (" + (ship.getSparePart(selectedPart).getFullDurability() / 3) + " Credits)  3. Back";
                  }
                  else if (ship.getSparePart(selectedPart).getDurability() == ship.getSparePart(selectedPart).getFullDurability()) {
                     return "Part is already repaired!";
                  }
                  else {
                     ship.repair(selectedPart);
                     contextMenu = 13;
                     return "Repaired!\n-" + (ship.getSparePart(selectedPart).getFullDurability() / 3) + " Credits\n" + ship.getPartList();
                  }
               case "3": // back
                  contextMenu = 13;
                  return ship.getPartList();

               default:
                  return incorrectInputText;
            }
         case 15: // shop sell menu
            switch (input) {
               case "1": // sell parts
                  contextMenu = 16;
                  return ship.getPartList().substring(ship.getPartList().indexOf("Spare Parts")); // return ship part list without active parts
               case "2": // sell materials
                  return "Nothing here yet, work in progress";
               case "3":
                  contextMenu = 10; // go back to menu
                  return "1. Buy  2. Sell  3. Exit";
               default:
                  return incorrectInputText;
            }
         case 16: // sell items message
            try {
               return "Sold! +" + ship.sellPart(Integer.parseInt(input) - 1) + " Credits\n" + ship.getPartList().substring(ship.getPartList().indexOf("Spare Parts"));

            }
            catch(Exception IllegalArgumentException) {
               return incorrectInputText;
            }
         case 17: // Nearby Planets menu
            try {
               this.travelToX = ship.getLocationX();
               this.travelToY = ship.getLocationY();
               contextMenu = 7;
               String distanceUnit = " light years ";
               nearbyPlanets = true;
               this.travelToPlanet = Integer.parseInt(input);
               travelDistance = ((float) Math.sqrt(Math.pow(travelToX - ship.getLocationX(), 2) + Math.pow(travelToY - ship.getLocationY(), 2)));
               return "Travel to:\nSolar System " + getSolarSystem(travelToX, travelToY).getName() + "\nPlanet " + getPlanet(travelToX, travelToY, travelToPlanet).getName() + "\n" + travelDistance + distanceUnit + "away" + "\nYou will use " + ((1 * ship.getActivePart(0).getFuelEfficiency()) * travelDistance + "L of fuel\n(y/n)"); //returns solar system name, planet name, and distance using distance formula
            } catch (Exception IllegalArgumentException) {
               contextMenu = 17;
               return incorrectInputText;
            }
         case 18: // ask player for seed before beginning
            contextMenu = 1;
            return "Please enter a seed for world generation.";
         case 19: // Starlist menu (bookmarks for easy travel)
            starlist = true;
            switch(input) {
               case "+": // add to Starlist
                  starChart.addToStarlist(ship.getLocationX(), ship.getLocationY(), ship.getCurrentPlanet());
                  return starChart.getStarlist(ship.getGalacticLocationX(), ship.getGalacticLocationY()) + starlistText;
               case "-": // back
                  contextMenu = 4;
                  starlist = false;
                  return getCurrentPlanet() + ship.toString() + inhabitedPlanetText;
               default:
                  try {
                     this.travelToSectorX = starChart.getStarlistAtIndex(Integer.parseInt(input)).getGalacticLocationX() / 21;
                     this.travelToSectorY = starChart.getStarlistAtIndex(Integer.parseInt(input)).getGalacticLocationY() / -21;
                     this.travelToX = starChart.getStarlistAtIndex(Integer.parseInt(input)).getGalacticLocationX() - (travelToSectorX * 21) + 10;
                     this.travelToY = starChart.getStarlistAtIndex(Integer.parseInt(input)).getGalacticLocationY() - (travelToSectorY * 21) + 10;
                     this.travelToPlanet = starChart.getStarlistAtIndex(Integer.parseInt(input)).getPlanetIndex() + 1;
                     travelDistance = ((float) Math.sqrt(Math.pow(starChart.getStarlistAtIndex(Integer.parseInt(input)).getGalacticLocationX() - ship.getGalacticLocationX(), 2) + Math.pow(starChart.getStarlistAtIndex(Integer.parseInt(input)).getGalacticLocationY() - ship.getGalacticLocationY(), 2)));
                     String distanceUnit = " light years ";
                     if (travelDistance == 1.0) {
                        distanceUnit = " light year ";
                     }
                     contextMenu = 7;
                     return "Travel to:\nSolar System " + getSolarSystem(travelToSectorX, travelToSectorY, travelToX, travelToY).getName() + "\nPlanet " + getPlanet(travelToSectorX, travelToSectorY, travelToX, travelToY, travelToPlanet).getName() + "\n" + travelDistance + distanceUnit + "away" + "\nYou will use " + ((1 * ship.getActivePart(0).getFuelEfficiency()) * travelDistance + "L of fuel\n(y/n)"); //returns solar system name, planet name, and distance using distance formula
                  }
                  catch (Exception IllegalArgumentException) {
                     contextMenu = 19;
                     return incorrectInputText;
                  }
            }
         default:
            return null;
      }
   }
   

   
   
   
   
   public Sector getSector(int x, int y) {
      return starChart.getSector(x, y);
   }
   
   public Sector getCurrentSector() {
      return getSector(ship.getCurrentSectorX(), ship.getCurrentSectorY());
   }
   
   public Space getSolarSystem(int x, int y) {
      return getSector(ship.getCurrentSectorX(), ship.getCurrentSectorY()).getSpace(x, y);
   }

   public Space getSolarSystem(int sectorX, int sectorY, int x, int y) {
      return getSector(sectorX, sectorY).getSpace(x, y);
   }
   
   public Planet getPlanet(int x, int y, int planet) {
      return getSector(ship.getCurrentSectorX(), ship.getCurrentSectorY()).getSpace(x, y).getPlanet(planet);
   }

   public Planet getPlanet(int sectorX, int sectorY, int x, int y, int planet) {
      return getSector(sectorX, sectorY).getSpace(x, y).getPlanet(planet);
   }
   
   public Space getCurrentSolarSystem() {
      return getSector(ship.getCurrentSectorX(), ship.getCurrentSectorY()).getSpace(ship.getLocationX(), ship.getLocationY());
   }
   
   public Planet getCurrentPlanet() {
      return getSector(ship.getCurrentSectorX(), ship.getCurrentSectorY()).getSpace(ship.getLocationX(), ship.getLocationY()).getPlanet(ship.getCurrentPlanet());
   }

}