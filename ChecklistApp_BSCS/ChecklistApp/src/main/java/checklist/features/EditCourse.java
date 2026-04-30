package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

/**
 * EditCourse.java
 *
 * Feature 4 of the BSCS Checklist Monitoring Application.
 *
 * Allows the student to modify any field of an existing course record.
 * Supports editing:
 *   [1] Grade              — re-enter or clear a numeric grade
 *   [2] Descriptive title  — correct the title stored on record
 *   [3] Units              — update the unit value
 *   [4] Credited status    — toggle and set/clear the credited-from field
 *   [5] Actual course no.  — update the resolved elective course number
 *   [6] Actual course title — update the resolved elective title
 *
 * The course is looked up by its course number (case-insensitive).
 * Actual course no. is also accepted as a lookup key for electives.
 *
 * Author  : Member 4
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class EditCourse {

    private final List<Course> courses;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public EditCourse(List<Course> courses) {
        this.courses = courses;
    }

    // -------------------------------------------------------------------------
    // Main method
    // -------------------------------------------------------------------------

    public void execute() {
        PrintUtil.appHeader("EDIT A COURSE");

        String courseNo = InputUtil.readNonBlank(
                "  Enter Course No. to edit (e.g. CS 122, or 0 to cancel): ");

        if (courseNo.equals("0")) return;

        Course c = findByCourseNo(courseNo);

        if (c == null) {
            System.out.printf("%n  Course \"%s\" was not found in your record.%n", courseNo);
            System.out.println("  Tip: Course numbers are case-insensitive (e.g. cs 122 = CS 122).");
            InputUtil.pressEnterToContinue();
            return;
        }

        printCourseDetails(c);

        System.out.println();
        System.out.println("  What would you like to edit?");
        System.out.println();
        System.out.println("    [1] Grade");
        System.out.println("    [2] Descriptive title");
        System.out.println("    [3] Units");
        System.out.println("    [4] Credited status");
        System.out.println("    [5] Actual course no.  (for electives)");
        System.out.println("    [6] Actual course title (for electives)");
        System.out.println("    [0] Cancel");

        int field = InputUtil.readIntInRange("\n  Choice: ", 0, 6);

        switch (field) {
            case 1 -> editGrade(c);
            case 2 -> editTitle(c);
            case 3 -> editUnits(c);
            case 4 -> editCreditedStatus(c);
            case 5 -> editActualCourseNo(c);
            case 6 -> editActualTitle(c);
            case 0 -> System.out.println("\n  Edit cancelled. No changes were made.");
        }

        InputUtil.pressEnterToContinue();
    }

    // -------------------------------------------------------------------------
    // Field-edit methods
    // -------------------------------------------------------------------------

    private void editGrade(Course c) {
        System.out.printf("%n  Current grade : %s%n",
                c.getGrade() == null ? "Not yet taken" : c.getGrade());
        System.out.println("  (Enter a number 0–100, or type \"clear\" to remove the grade.)");

        String raw = InputUtil.readNonBlank("  New grade: ");

        if (raw.equalsIgnoreCase("clear")) {
            c.setGrade(null);
            System.out.println("  Grade cleared. Course is now marked as Not yet taken.");
            return;
        }

        try {
            double g = Double.parseDouble(raw);
            if (g < 0 || g > 100) {
                System.out.println("  Grade must be between 0 and 100. No changes made.");
                return;
            }
            c.setGrade(String.valueOf((int) g));
            if (g < 75) {
                System.out.printf("  [WARNING] Grade %s is a failing grade. Saved anyway.%n", (int) g);
            } else {
                System.out.printf("  Grade updated to %s.%n", (int) g);
            }
        } catch (NumberFormatException e) {
            System.out.println("  Invalid input. No changes were made.");
        }
    }

    private void editTitle(Course c) {
        System.out.printf("%n  Current title : %s%n", c.getDescriptiveTitle());
        String newTitle = InputUtil.readNonBlank("  New title     : ");
        c.setDescriptiveTitle(newTitle);
        System.out.println("  Title updated.");
    }

    private void editUnits(Course c) {
        System.out.printf("%n  Current units : %.1f%n", c.getUnits());
        String raw = InputUtil.readNonBlank("  New units     : ");
        try {
            double u = Double.parseDouble(raw);
            c.setUnits(u);
            System.out.printf("  Units updated to %.1f.%n", u);
        } catch (NumberFormatException e) {
            System.out.println("  Invalid number. No changes were made.");
        }
    }

    private void editCreditedStatus(Course c) {
        System.out.println();
        if (c.isCredited()) {
            System.out.printf("  Currently credited from: \"%s\"%n", c.getCreditedFrom());
            System.out.println("  Options: [1] Change credited-from  [2] Remove credited flag  [0] Cancel");
            int sub = InputUtil.readIntInRange("  Choice: ", 0, 2);
            switch (sub) {
                case 1 -> {
                    String from = InputUtil.readNonBlank("  New credited-from (program/school): ");
                    c.setCreditedFrom(from);
                    System.out.printf("  Updated. Now credited from \"%s\".%n", from);
                }
                case 2 -> {
                    c.setCredited(false);
                    c.setCreditedFrom("");
                    System.out.println("  Credited flag removed.");
                }
                case 0 -> System.out.println("  Cancelled.");
            }
        } else {
            System.out.println("  Course is not currently marked as credited.");
            String from = InputUtil.readNonBlank(
                    "  Mark as credited from (program/school, or 0 to cancel): ");
            if (!from.equals("0")) {
                c.setCredited(true);
                c.setCreditedFrom(from);
                System.out.printf("  Marked as credited from \"%s\".%n", from);
            }
        }
    }

    private void editActualCourseNo(Course c) {
        System.out.printf("%n  Current actual course no. : %s%n",
                c.getActualCourseNo().isBlank() ? "(none)" : c.getActualCourseNo());
        System.out.println("  (Type \"clear\" to remove it.)");
        String val = InputUtil.readNonBlank("  New actual course no.     : ");
        if (val.equalsIgnoreCase("clear")) {
            c.setActualCourseNo("");
            System.out.println("  Actual course no. cleared.");
        } else {
            c.setActualCourseNo(val);
            System.out.println("  Actual course no. updated.");
        }
    }

    private void editActualTitle(Course c) {
        System.out.printf("%n  Current actual title : %s%n",
                c.getActualTitle().isBlank() ? "(none)" : c.getActualTitle());
        System.out.println("  (Type \"clear\" to remove it.)");
        String val = InputUtil.readNonBlank("  New actual title     : ");
        if (val.equalsIgnoreCase("clear")) {
            c.setActualTitle("");
            System.out.println("  Actual title cleared.");
        } else {
            c.setActualTitle(val);
            System.out.println("  Actual title updated.");
        }
    }

    // -------------------------------------------------------------------------
    // Display helper
    // -------------------------------------------------------------------------

    private void printCourseDetails(Course c) {
        System.out.println();
        System.out.println("  " + PrintUtil.THIN_LINE);
        System.out.printf("  Course No.     : %s%n", c.getCourseNo());
        System.out.printf("  Title          : %s%n", c.getDescriptiveTitle());
        System.out.printf("  Units          : %.1f%n", c.getUnits());
        System.out.printf("  Year / Term    : %s / %s%n",
                PrintUtil.yearLabel(c.getYear()), c.getTerm());
        System.out.printf("  Grade          : %s%n",
                c.getGrade() == null ? "Not yet taken" : c.getGrade());
        System.out.printf("  Credited       : %s%s%n",
                c.isCredited() ? "Yes" : "No",
                c.isCredited() ? " — from \"" + c.getCreditedFrom() + "\"" : "");
        if (!c.getActualCourseNo().isBlank())
            System.out.printf("  Actual No.     : %s%n", c.getActualCourseNo());
        if (!c.getActualTitle().isBlank())
            System.out.printf("  Actual Title   : %s%n", c.getActualTitle());
        System.out.println("  " + PrintUtil.THIN_LINE);
    }

    // -------------------------------------------------------------------------
    // Search helper
    // -------------------------------------------------------------------------

    /** Finds a course by its course number or its actual (resolved) course number. */
    private Course findByCourseNo(String no) {
        for (Course c : courses) {
            if (c.getCourseNo().equalsIgnoreCase(no)) return c;
            if (!c.getActualCourseNo().isBlank() &&
                c.getActualCourseNo().equalsIgnoreCase(no)) return c;
        }
        return null;
    }
}
