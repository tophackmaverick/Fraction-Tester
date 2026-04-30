package checklist.util;

import java.util.Scanner;

/**
 * InputUtil.java
 *
 * Utility class providing validated console-input helpers used throughout
 * the application. All input is read from a single shared Scanner so that
 * the input stream is never closed accidentally.
 *
 * Author  : Member 3
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class InputUtil {

    // Single Scanner instance shared across the entire application
    private static final Scanner SCANNER = new Scanner(System.in);

    // Prevent instantiation — this is a utility class
    private InputUtil() {}

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------

    /**
     * Repeatedly prompts the user until a valid integer within [min, max]
     * is entered. Handles both non-integer input and out-of-range values
     * with clear error messages matching the sample run.
     *
     * @param prompt the message to display before each attempt
     * @param min    minimum acceptable value (inclusive)
     * @param max    maximum acceptable value (inclusive)
     * @return a validated integer in [min, max]
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = SCANNER.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value < min || value > max) {
                    System.out.printf("  The number must be from %d to %d.%n", min, max);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  You entered an invalid integer. Please enter an integer.");
            }
        }
    }

    /**
     * Prompts the user until a non-blank string is entered.
     *
     * @param prompt the message to display
     * @return a trimmed, non-blank string
     */
    public static String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("  Input cannot be blank. Please try again.");
        }
    }

    /**
     * Prompts the user for a numeric grade between 0 and 100, inclusive.
     * Loops until a valid grade is entered.
     *
     * @param courseNo course identifier shown in the prompt
     * @return the grade as a whole-number string (e.g. "85")
     */
    public static String readGrade(String courseNo) {
        while (true) {
            System.out.printf("  Enter grade for %-14s (0 - 100): ", courseNo);
            String raw = SCANNER.nextLine().trim();
            try {
                double g = Double.parseDouble(raw);
                if (g < 0 || g > 100) {
                    System.out.println("  Grade must be between 0 and 100.");
                } else {
                    return String.valueOf((int) g);
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a number (e.g. 85).");
            }
        }
    }

    /**
     * Reads any line of input (may be blank, e.g. for Enter-to-continue prompts).
     *
     * @param prompt the message to display
     * @return the raw trimmed input (possibly empty)
     */
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    /**
     * Pauses execution until the user presses Enter with NO other input.
     * If the user types anything before pressing Enter, they are warned
     * and prompted again until a bare Enter is received.
     */
    public static void pressEnterToContinue() {
        while (true) {
            System.out.print("\n  Press ENTER to continue...");
            String input = SCANNER.nextLine();
            if (input.isEmpty()) {
                return; // bare Enter — correct
            }
            System.out.println("  Please press ENTER only (do not type anything).");
        }
    }
}
