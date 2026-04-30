package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

/**
 * ShowGPA.java
 *
 * Feature 5 of the BSCS Checklist Monitoring Application.
 *
 * Computes and displays the student's General Weighted Average (GWA).
 * All courses with a numeric grade are included in the computation,
 * whether passed or failed, so the GWA reflects actual academic standing.
 *
 * Formula:
 *   GWA = Σ (grade × units)  /  Σ units  (for all graded courses)
 *
 * A Latin honors indicator is shown if the GWA meets SLU thresholds:
 *   Summa Cum Laude  : GWA >= 95
 *   Magna Cum Laude  : GWA >= 90
 *   Cum Laude        : GWA >= 85
 *
 * Author  : Member 5
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class ShowGPA {

    private final List<Course> courses;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ShowGPA(List<Course> courses) {
        this.courses = courses;
    }

    // -------------------------------------------------------------------------
    // Main method
    // -------------------------------------------------------------------------

    public void execute() {
        PrintUtil.appHeader("GENERAL WEIGHTED AVERAGE (GWA)");

        double sumWeightedGrades = 0;
        double sumGradedUnits    = 0;
        int    countPassed       = 0;
        int    countFailed       = 0;
        int    countUntaken      = 0;

        System.out.println();
        System.out.printf("  %-14s  %-42s  %6s  %7s%n",
                "Course No.", "Title", "Units", "Grade");
        System.out.println("  " + "-".repeat(76));

        for (Course c : courses) {
            if (c.getTerm().equalsIgnoreCase("Extra")) continue; // extras don't count

            if (!c.isTaken()) {
                countUntaken++;
                continue;
            }

            try {
                double g = Double.parseDouble(c.getGrade());

                System.out.printf("  %-14s  %-42s  %6.1f  %7.0f%s%n",
                        c.getDisplayCourseNo(),
                        PrintUtil.trunc(c.getDisplayTitle(), 42),
                        c.getUnits(),
                        g,
                        g < 75 ? "  [FAILED]" : "");

                sumWeightedGrades += g * c.getUnits();
                sumGradedUnits    += c.getUnits();

                if (g >= 75) countPassed++; else countFailed++;

            } catch (NumberFormatException e) {
                countUntaken++; // non-numeric — treat as untaken
            }
        }

        System.out.println("  " + "-".repeat(76));

        if (sumGradedUnits == 0) {
            System.out.println("\n  No graded courses yet. GWA cannot be computed.");
            InputUtil.pressEnterToContinue();
            return;
        }

        double gwa = sumWeightedGrades / sumGradedUnits;

        System.out.printf("%n  Courses passed       : %d%n", countPassed);
        System.out.printf("  Courses failed       : %d%n", countFailed);
        System.out.printf("  Not yet taken        : %d%n", countUntaken);
        System.out.printf("  Total units graded   : %.1f%n", sumGradedUnits);

        System.out.println();
        System.out.println("  +--------------------------------+");
        System.out.printf("  |  GWA  =  %-6.2f              |%n", gwa);
        System.out.println("  +--------------------------------+");

        String distinction = getDistinction(gwa);
        if (!distinction.isEmpty()) {
            System.out.printf("  Distinction: %s%n", distinction);
        }

        InputUtil.pressEnterToContinue();
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private String getDistinction(double gwa) {
        if (gwa >= 95) return "SUMMA CUM LAUDE candidate (GWA >= 95)";
        if (gwa >= 90) return "MAGNA CUM LAUDE candidate (GWA >= 90)";
        if (gwa >= 85) return "CUM LAUDE candidate (GWA >= 85)";
        return "";
    }
}
