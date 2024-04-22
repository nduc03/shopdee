package Utils;

import java.util.Scanner;

public final class Utils {
    private static final Scanner sc = new Scanner(System.in);
    private Utils() {
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static String promptInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }
}
