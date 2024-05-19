package Utils;

import org.jetbrains.annotations.NotNull;

import java.io.Console;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public final class Utils {
    private static final Scanner sc = new Scanner(System.in);
    private static final Console console = System.console();

    private Utils() {
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static String promptInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static String promptPassword(String prompt) {
        if (console != null)
            return new String(console.readPassword(prompt));
        return promptInput(prompt);
    }

    public static Optional<Integer> promptIntInput(String prompt) {
        System.out.print(prompt);
        try {
            int res = sc.nextInt();
            sc.nextLine();
            return Optional.of(res);
        } catch (InputMismatchException e) {
            sc.nextLine();
            return Optional.empty();
        }
    }

    public static Optional<Double> promptDoubleInput(String prompt) {
        System.out.print(prompt);
        try {
            double res = sc.nextInt();
            sc.nextLine();
            return Optional.of(res);
        } catch (InputMismatchException e) {
            sc.nextLine();
            return Optional.empty();
        }
    }

    public static @NotNull String dateToString(Date date) {
        if (date == null) return "null date";
        DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return fmt.format(date);
    }
}
