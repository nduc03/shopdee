import MainSystem.Menu;
import Utils.Utils;

public class Main {
    public static void main(String[] args) {
        while (true) {
            try {
                Menu.displayMenu();
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                e.printStackTrace();
                if (!Utils.promptInput("Restart program? (y/n): ").equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
    }
}
