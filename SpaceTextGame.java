import java.util.Scanner;
public class SpaceTextGame {
   public static void main(String[] args) {
      Scanner scan = new Scanner(System.in);
      String output = "";
      Game game = new Game();
      System.out.println("type \"quit\" to exit at any time.\nUse fullscreen for optimal experience.\nPress enter to continue.\n");
      while (!output.equalsIgnoreCase("quit")) {
         output = scan.nextLine();
         if (!output.equalsIgnoreCase("quit")) {
            System.out.println(game.processCommand(output));
         }
      }
   }
}
