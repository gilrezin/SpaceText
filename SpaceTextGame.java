import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class SpaceTextGame {
   public static void main(String[] args) {
      Scanner scan = new Scanner(System.in);
      String output = "";
      Game game = loadGame(); // loads game progress
      if (game == null) { // start a new save if none exists
         game = new Game();
      }
      System.out.println("type \"quit\" to exit at any time.\nUse fullscreen for optimal experience.\nPress enter to continue.\n");
      while (!output.equalsIgnoreCase("quit")) {
         output = scan.nextLine();
         if (!output.equalsIgnoreCase("quit")) {
            if (output.equalsIgnoreCase("save")) {
               saveGame(game);
            }
            else {
               System.out.println(game.processCommand(output));
            }
         }
      }
   }

   public static void saveGame(Game game) { // saves game
      try {
         FileOutputStream f = new FileOutputStream(new File("savefile"));
         ObjectOutputStream o = new ObjectOutputStream(f);
         o.writeObject(game);
         o.close();
         f.close();
         System.out.println("Game saved");
      }
      catch (IOException e) {
         System.out.println("There was a problem saving. Please try again later.");
      }
   }

   public static Game loadGame() { // loads game progress
      try {
         FileInputStream fi = new FileInputStream(new File("savefile"));
         ObjectInputStream oi = new ObjectInputStream(fi);
         // Read objects
         Game game = (Game) oi.readObject();
         oi.close();
         fi.close();
         game.setContextMenu(0);
         return game;
      }
      catch (FileNotFoundException e) {
         ;
      }
      catch (IOException e) {
         System.out.println("There was an error loading your progress.");
      }
      catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
      return null;
   }

}
