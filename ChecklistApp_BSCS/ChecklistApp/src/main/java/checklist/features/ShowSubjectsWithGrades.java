package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;
import checklist.util.TermGrouper;

import java.util.List;
import java.util.Map;

/**
 * ShowSubjectsWithGrades.java
 *
 * Feature 2 of the BSCS Checklist Monitoring Application.
 *
 * Displays all courses grouped by school year and term, each with
 * its corresponding grade. Courses not yet taken show "Not yet taken".
 * Courses with a failing grade (below 75) are flagged with [FAILED].
 * Credited courses are annotated with their source institution.
 *
 * Author  : Member 1
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class ShowSubjectsWithGrades {

    private final List<Course> courses;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ShowSubjectsWithGrades(List<Course> courses) {
        this.courses = courses;
    }

    // -------------------------------------------------------------------------
    // Main method
    // -------------------------------------------------------------------------

    /**
     * Iterates over each (year, term) group and prints a formatted table
     * of courses with their grades. Also prints per-term totals.
     * Extra courses (outside the curriculum) are shown in a separate section.
     */
    public void execute() {
        PrintUtil.appHeader("SHOW SUBJECTS WITH GRADES FOR EACH TERM");

        // ── Curriculum courses ────────────────────────────────────────────────
        List<Course> curriculumOnly = courses.stream()
                .filter(c -> !c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        var grouped = TermGrouper.group(curriculumOnly);

        for (Map.Entry<String, List<Course>> entry : grouped.entrySet()) {
            String[] keyParts = entry.getKey().split("\\|", 2);
            int    year = Integer.parseInt(keyParts[0]);
            String term = keyParts[1];

            PrintUtil.termHeader(year, term);
            PrintUtil.printColumnHeaderWithGrade();

            double termTotal  = 0;
            double earnedThisTerm = 0;

            for (Course c : entry.getValue()) {
                String gradeDisplay;
                String annotation = "";

                if (!c.isTaken()) {
                    gradeDisplay = "Not yet taken";
                } else if (!c.isPassed()) {
                    gradeDisplay = c.getGrade();
                    annotation   = "  [FAILED]";
                } else {
                    gradeDisplay = c.getGrade();
                    if (c.isCredited()) {
                        annotation = "  [CREDITED from " + c.getCreditedFrom() + "]";
                    }
                }

                System.out.printf("  %-14s  %-44s  %6.1f  %-13s%s%n",
                        c.getDisplayCourseNo(),
                        PrintUtil.trunc(c.getDisplayTitle(), 44),
                        c.getUnits(),
                        gradeDisplay,
                        annotation);

                termTotal += c.getUnits();
                if (c.isPassed()) earnedThisTerm += c.getUnits();
            }

            System.out.printf("%n  %-62s  %6.1f%n", "TOTAL UNITS THIS TERM:", termTotal);
            System.out.printf("  %-62s  %6.1f%n",   "UNITS PASSED:",          earnedThisTerm);
            InputUtil.pressEnterToContinue();
        }

        // ── Extra / non-curriculum courses ────────────────────────────────────
        List<Course> extras = courses.stream()
                .filter(c -> c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        if (!extras.isEmpty()) {
            PrintUtil.appHeader("EXTRA / NON-CURRICULUM COURSES");
            PrintUtil.printColumnHeaderWithGrade();

            for (Course c : extras) {
                String note = c.isCredited()
                        ? "  [CREDITED from " + c.getCreditedFrom() + "]"
                        : "";
                System.out.printf("  %-14s  %-44s  %6.1f  %-13s%s%n",
                        c.getDisplayCourseNo(),
                        PrintUtil.trunc(c.getDisplayTitle(), 44),
                        c.getUnits(),
                        c.isTaken() ? c.getGrade() : "Not yet taken",
                        note);
            }
            InputUtil.pressEnterToContinue();
        }
    }
}
