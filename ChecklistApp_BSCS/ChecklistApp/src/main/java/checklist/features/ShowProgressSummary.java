package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

/**
 * ShowProgressSummary.java
 *
 * Feature 7 of the BSCS Checklist Monitoring Application.
 *
 * Shows the student's overall progress through the BSCS curriculum:
 *   • Total curriculum units vs. units earned (passed)
 *   • Visual ASCII progress bar
 *   • Count of passed, failed, and untaken courses
 *   • A list of courses with failing grades that must be retaken
 *   • A list of remaining (untaken) courses
 *
 * Extra and credited courses are counted separately and do not
 * affect the curriculum progress percentage.
 *
 * Author  : Member 5
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class ShowProgressSummary {

    private final List<Course> courses;

    // Progress bar width in characters
    private static final int BAR_WIDTH = 40;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ShowProgressSummary(List<Course> courses) {
        this.courses = courses;
    }

    // -------------------------------------------------------------------------
    // Main method
    // -------------------------------------------------------------------------

    public void execute() {
        PrintUtil.appHeader("CURRICULUM PROGRESS SUMMARY");

        double totalUnits   = 0;
        double passedUnits  = 0;
        double failedUnits  = 0;
        int    passedCount  = 0;
        int    failedCount  = 0;
        int    untakenCount = 0;

        // ── Tally curriculum courses ──────────────────────────────────────────
        for (Course c : courses) {
            if (c.getTerm().equalsIgnoreCase("Extra")) continue;

            totalUnits += c.getUnits();

            if (!c.isTaken()) {
                untakenCount++;
            } else if (c.isPassed()) {
                passedUnits += c.getUnits();
                passedCount++;
            } else {
                failedUnits += c.getUnits();
                failedCount++;
            }
        }

        double remainingUnits = totalUnits - passedUnits;
        double percent        = (totalUnits > 0) ? (passedUnits / totalUnits) * 100.0 : 0;

        // ── Statistics ────────────────────────────────────────────────────────
        System.out.println();
        System.out.printf("  %-36s  %8.1f units%n", "Total curriculum units:",    totalUnits);
        System.out.printf("  %-36s  %8.1f units%n", "Units earned (passed):",     passedUnits);
        System.out.printf("  %-36s  %8.1f units%n", "Units failed:",              failedUnits);
        System.out.printf("  %-36s  %8.1f units%n", "Units remaining:",           remainingUnits);
        System.out.println();
        System.out.printf("  %-36s  %8d%n", "Courses passed:",  passedCount);
        System.out.printf("  %-36s  %8d%n", "Courses failed:",  failedCount);
        System.out.printf("  %-36s  %8d%n", "Courses not yet taken:", untakenCount);

        // ── Progress bar ──────────────────────────────────────────────────────
        int filled = (int) Math.round((percent / 100.0) * BAR_WIDTH);
        String bar = "#".repeat(filled) + "-".repeat(BAR_WIDTH - filled);
        System.out.printf("%n  Progress: [%s] %.1f%%%n", bar, percent);

        // ── Failed courses alert ──────────────────────────────────────────────
        List<Course> failed = courses.stream()
                .filter(c -> c.isTaken() && !c.isPassed()
                          && !c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        if (!failed.isEmpty()) {
            System.out.println();
            System.out.println("  [!] COURSES WITH FAILING GRADES (must be retaken):");
            System.out.println("  " + "-".repeat(68));
            for (Course c : failed) {
                System.out.printf("      %-14s  %-42s  Grade: %s%n",
                        c.getDisplayCourseNo(),
                        PrintUtil.trunc(c.getDisplayTitle(), 42),
                        c.getGrade());
            }
        }

        // ── Remaining courses ─────────────────────────────────────────────────
        List<Course> remaining = courses.stream()
                .filter(c -> !c.isTaken() && !c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        if (!remaining.isEmpty()) {
            System.out.println();
            System.out.printf("  Remaining courses (%d):%n", remaining.size());
            System.out.println("  " + "-".repeat(68));
            for (Course c : remaining) {
                System.out.printf("      %-14s  %s  (%.1f units)%n",
                        c.getCourseNo(),
                        PrintUtil.trunc(c.getDescriptiveTitle(), 42),
                        c.getUnits());
            }
        }

        InputUtil.pressEnterToContinue();
    }
}
