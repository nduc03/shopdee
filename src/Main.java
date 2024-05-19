import MainSystem.Menu;
import Utils.Utils;

public class Main {
    public static void main(String[] args) {
        boolean isDebug = args.length >= 1 && args[0] != null && args[0].trim().equalsIgnoreCase("debug");
        if (isDebug) System.out.println("Running in debug mode");
        while (true) {
            try {
                Menu.displayMenu();
            } catch (Exception e) {
                if (isDebug)
                    e.printStackTrace();
                else
                    System.out.println(e.getMessage());

                if (!Utils.promptInput("Program Error. Restart program? (y/n): ").equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
    }
}
