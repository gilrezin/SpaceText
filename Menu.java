// unused, delete later
public class Menu {
   private String[] menuList;
   
   public Menu() {
      this.menuList = new String[]{"\\\\SpaceText//\nA game by Gil Rezin", "this is a test message", ""};
   }
   
   public String getMenu(int index) { // returns the given text
      return menuList[index];
   }
}