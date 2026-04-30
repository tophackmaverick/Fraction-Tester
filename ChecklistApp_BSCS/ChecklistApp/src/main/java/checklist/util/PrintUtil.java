package checklist.util;

/**
 * PrintUtil.java
 *
 * Shared console-formatting utilities used by all feature classes.
 * Centralizing display logic here keeps feature classes focused on
 * their own business logic and ensures consistent output styling.
 *
 * Author  : Member 3
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class PrintUtil {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    public static final String THICK_LINE =
        "================================================================================";

    public static final String THIN_LINE =
        "--------------------------------------------------------------------------------";

    // Prevent instantiation
    private PrintUtil() {}

    // -------------------------------------------------------------------------
    // Header methods
    // -------------------------------------------------------------------------

    /** Prints a thick-bordered application-level header. */
    public static void appHeader(String title) {
        System.out.println();
        System.out.println(THICK_LINE);
        System.out.printf("  %s%n", title);
        System.out.println(THICK_LINE);
    }

    /** Prints a thin-bordered section header for a specific year and term. */
    public static void termHeader(int year, String term) {
        System.out.println();
        System.out.println(THIN_LINE);
        System.out.printf("  Year = %-15s   Term = %s%n", yearLabel(year), term);
        System.out.println(THIN_LINE);
    }

    // -------------------------------------------------------------------------
    // Column header methods
    // -------------------------------------------------------------------------

    /** Prints the table header for subject listings without grades. */
    public static void printColumnHeaderNoGrade() {
        System.out.printf("  %-14s  %-48s  %s%n",
                "Course No.", "Descriptive Title", "Units");
        System.out.println("  " + "-".repeat(70));
    }

    /** Prints the table header for subject listings with grades. */
    public static void printColumnHeaderWithGrade() {
        System.out.printf("  %-14s  %-44s  %6s  %s%n",
                "Course No.", "Descriptive Title", "Units", "Grade");
        System.out.println("  " + "-".repeat(76));
    }

    // -------------------------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------------------------

    /** Converts a year number to its ordinal label string. */
    public static String yearLabel(int year) {
        return switch (year) {
            case 1 -> "First Year";
            case 2 -> "Second Year";
            case 3 -> "Third Year";
            case 4 -> "Fourth Year";
            default -> year + "th Year";
        };
    }

    /**
     * Truncates a string to at most maxLen characters.
     * Appends "..." if the string was cut.
     */
    public static String trunc(String s, int maxLen) {
        if (s == null || s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 3) + "...";
    }

    /** Prints a blank line. */
    public static void blank() {
        System.out.println();
    }
}
