public abstract class GeneratorTools {
   public static String generateName() {
      String characters = "abcdefghijklmnopqrstuvwxyz-'";
      String output = "";
      char x = characters.charAt((char) (Math.random() * 27) + 1); // generates first random character
      for (int i = 0; i < Math.random() * 10 + 3; i++) {
         output += x; // adds character to end
         x = characters.charAt((char) (Math.random() * 27) + 1); // generates random character
      }
      return output;
   }
}