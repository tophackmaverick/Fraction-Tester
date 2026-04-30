package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.Comparator;
import java.util.List;

/**
 * ShowGradeRanking.java
 *
 * Feature 6 of the BSCS Checklist Monitoring Application.
 *
 * Displays all courses that have a numeric grade, sorted in descending
 * order (highest grade first). Courses with the same grade receive the
 * same rank number (standard competition ranking).
 *
 * Courses marked as [FAILED] (grade < 75) are included in the list
 * and clearly flagged, since they are still part of the student's record.
 *
 * Author  : Member 5
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class ShowGradeRanking {

    private final List<Course> courses;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ShowGradeRanking(List<Course> courses) {
        this.courses = courses;
    }

    // -------------------------------------------------------------------------
    // Main method
    // -------------------------------------------------------------------------

    public void execute() {
        PrintUtil.appHeader("COURSES RANKED BY GRADE (DESCENDING)");

        // Collect all courses with a valid numeric grade
        List<Course> graded = courses.stream()
                .filter(c -> {
                    if (!c.isTaken()) return false;
                    try { Double.parseDouble(c.getGrade()); return true; }
                    catch (NumberFormatException e) { return false; }
                })
                .sorted(Comparator
                        .comparingDouble((Course c) -> Double.parseDouble(c.getGrade()))
                        .reversed())
                .toList();

        if (graded.isEmpty()) {
            System.out.println("\n  No graded courses to display yet.");
            InputUtil.pressEnterToContinue();
            return;
        }

        System.out.println();
        System.out.printf("  %-4s  %-14s  %-42s  %6s  %7s%n",
                "Rank", "Course No.", "Title", "Units", "Grade");
        System.out.println("  " + "-".repeat(80));

        int rank       = 1;
        int displayRank = 1;
        double prevGrade = -1;

        for (Course c : graded) {
            double g = Double.parseDouble(c.getGrade());

            // Reset to current position if grade changed (standard competition ranking)
            if (g != prevGrade) {
                displayRank = rank;
            }

            System.out.printf("  %-4d  %-14s  %-42s  %6.1f  %7.0f%s%n",
                    displayRank,
                    c.getDisplayCourseNo(),
                    PrintUtil.trunc(c.getDisplayTitle(), 42),
                    c.getUnits(),
                    g,
                    g < 75 ? "  [FAILED]" : "");

            prevGrade = g;
            rank++;
        }

        System.out.println("  " + "-".repeat(80));
        System.out.printf("  Total graded courses listed: %d%n", graded.size());

        InputUtil.pressEnterToContinue();
    }
}
