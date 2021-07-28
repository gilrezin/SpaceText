// class used to read csv files from disk
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

public class Database {
    private HashMap<Integer, String> map;

    public Database(int index) {
        try {
            Scanner scan;
            File file = new File("Help.csv"); // case 0, for help text
            map = new HashMap<Integer, String>();
            String line = null;
            switch (index) {

                case 1: // for element & name database
                case 2:
                    file = new File("Resources.csv");
            }
            scan = new Scanner(file);
            while ((line = scan.nextLine()) != null) {
                int key;
                String value = "";
                key = Integer.parseInt(line.substring(line.indexOf(",") - 2, line.indexOf(","))); // get Integer part
                switch (index) {
                    case 0:
                        value = line.substring(line.indexOf(",") + 1); // get String part
                        break;
                    case 1:
                        value = line.substring(line.indexOf(",") + 1, line.lastIndexOf(",")); // get String part
                        break;
                    case 2:
                        value = line.substring(line.lastIndexOf(",") + 1); // get String part
                        break;
                }
                map.put(key, value);
            }
        } catch (Exception IOException) {
        }
    }

    public String get(int index) { // returns data from HashMap
        try {
            return map.get(index).replace("&n", "\n") + "\n"; // replaces custom escape sequence &n with \n.
        }
        catch (Exception NullPointerException) { // if no help menu exists at location
            return "No help menu exists here.";
        }
    }
}
