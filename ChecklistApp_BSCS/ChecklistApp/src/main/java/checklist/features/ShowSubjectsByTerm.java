package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;
import checklist.util.TermGrouper;

import java.util.List;
import java.util.Map;

/**
 * ShowSubjectsByTerm.java
 *
 * Feature 1 of the BSCS Checklist Monitoring Application.
 *
 * Displays all courses in the curriculum organized by school year and
 * term (1st Semester, 2nd Semester, Short Term). No grades are shown.
 * The user presses Enter to advance from one term to the next.
 *
 * Author  : Member 1
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class ShowSubjectsByTerm {

    private final List<Course> courses;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ShowSubjectsByTerm(List<Course> courses) {
        this.courses = courses;
    }

    // -------------------------------------------------------------------------
    // Main method
    // -------------------------------------------------------------------------

    /**
     * Iterates over each (year, term) group and prints a formatted table
     * of courses. Pauses between each term for readability.
     */
    public void execute() {
        PrintUtil.appHeader("SHOW SUBJECTS FOR EACH SCHOOL TERM");

        // Filter out extra (non-curriculum) courses before grouping
        List<Course> curriculumOnly = courses.stream()
                .filter(c -> !c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        var grouped = TermGrouper.group(curriculumOnly);

        for (Map.Entry<String, List<Course>> entry : grouped.entrySet()) {
            String[] keyParts = entry.getKey().split("\\|", 2);
            int    year = Integer.parseInt(keyParts[0]);
            String term = keyParts[1];

            PrintUtil.termHeader(year, term);
            PrintUtil.printColumnHeaderNoGrade();

            double termTotal = 0;

            for (Course c : entry.getValue()) {
                String creditNote = c.isCredited()
                        ? "  [CREDITED from " + c.getCreditedFrom() + "]"
                        : "";

                System.out.printf("  %-14s  %-48s  %.1f%s%n",
                        c.getDisplayCourseNo(),
                        PrintUtil.trunc(c.getDisplayTitle(), 48),
                        c.getUnits(),
                        creditNote);

                termTotal += c.getUnits();
            }

            System.out.printf("%n  %-64s  %.1f%n", "TOTAL UNITS", termTotal);
            InputUtil.pressEnterToContinue();
        }
    }
}
