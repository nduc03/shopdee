import MainSystem.ShopdeeClient;
import Utils.Utils;

public class Main {
    public static void main(String[] args) {
        while (true) {
            try {
                ShopdeeClient.displayMenu();
            } catch (Exception e) {
                e.printStackTrace();
                if (!Utils.promptInput("Restart program? (y/n): ").equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
    }
}
