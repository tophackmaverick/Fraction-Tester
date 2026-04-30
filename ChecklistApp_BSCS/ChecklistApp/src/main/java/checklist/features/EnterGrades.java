package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

/**
 * EnterGrades.java
 *
 * Feature 3 of the BSCS Checklist Monitoring Application.
 *
 * Allows the student to record grades for recently finished courses.
 * Handles four cases:
 *   (1) Regular curriculum course — choose from list, enter numeric grade.
 *   (2) Elective placeholder — resolve actual course no. + title, then grade.
 *   (3) Credited course — mark a curriculum course as credited from
 *       another program/school, or add it as an extra if not in curriculum.
 *   (4) Extra course — a course outside the BSCS curriculum that the
 *       student completed (still recorded on the student's file).
 *
 * Business rules enforced:
 *   • Failing grade (< 75) is accepted; course stays on record with [FAILED].
 *   • A course already graded can only be changed via Edit Course (Feature 4).
 *   • Grades are stored as whole-number strings (e.g. "85").
 *
 * Author  : Member 4
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class EnterGrades {

    private final List<Course> courses;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public EnterGrades(List<Course> courses) {
        this.courses = courses;
    }

    // -------------------------------------------------------------------------
    // Main method
    // -------------------------------------------------------------------------

    public void execute() {
        PrintUtil.appHeader("ENTER GRADES FOR RECENTLY FINISHED SUBJECTS");

        System.out.println("  Select the type of course to record:");
        System.out.println();
        System.out.println("    [1] Regular curriculum course");
        System.out.println("    [2] Credited course (transferred from another program / school)");
        System.out.println("    [3] Extra course (not part of the BSCS curriculum)");
        System.out.println("    [0] Back to main menu");

        int choice = InputUtil.readIntInRange("\n  Choice: ", 0, 3);

        switch (choice) {
            case 1 -> enterCurriculumGrade();
            case 2 -> enterCreditedCourse();
            case 3 -> enterExtraCourse();
            case 0 -> { /* return silently */ }
        }
    }

    // -------------------------------------------------------------------------
    // Case 1: Regular curriculum course
    // -------------------------------------------------------------------------

    private void enterCurriculumGrade() {
        // Collect untaken curriculum courses (excluding Extra group)
        List<Course> untaken = courses.stream()
                .filter(c -> !c.isTaken() && !c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        if (untaken.isEmpty()) {
            System.out.println("\n  All curriculum courses already have grades entered.");
            System.out.println("  Use Feature 4 (Edit a Course) to update an existing grade.");
            InputUtil.pressEnterToContinue();
            return;
        }

        // Display the numbered list
        System.out.println();
        System.out.println("  Untaken curriculum courses:");
        System.out.println("  " + "-".repeat(70));
        for (int i = 0; i < untaken.size(); i++) {
            Course c = untaken.get(i);
            System.out.printf("  [%2d] %-13s  %s  (%.1f units)%n",
                    i + 1,
                    c.getDisplayCourseNo(),
                    PrintUtil.trunc(c.getDisplayTitle(), 42),
                    c.getUnits());
        }

        int index = InputUtil.readIntInRange(
                "\n  Select course number (0 to cancel): ", 0, untaken.size());
        if (index == 0) return;

        Course selected = untaken.get(index - 1);

        // Resolve elective placeholder before grading
        if (selected.isElective() && selected.getActualCourseNo().isBlank()) {
            resolveElective(selected);
        }

        String grade = InputUtil.readGrade(selected.getDisplayCourseNo());
        selected.setGrade(grade);

        double g = Double.parseDouble(grade);
        if (g < 75) {
            System.out.printf("%n  [WARNING] Grade %s is a FAILING grade for %s.%n",
                    grade, selected.getDisplayCourseNo());
            System.out.println("  The course will remain on your record and must be retaken.");
        } else {
            System.out.printf("%n  Grade %s successfully saved for %s.%n",
                    grade, selected.getDisplayCourseNo());
        }

        InputUtil.pressEnterToContinue();
    }

    /**
     * Prompts the student to supply the actual course number and title
     * for an elective slot before the grade is entered.
     */
    private void resolveElective(Course c) {
        System.out.println();
        System.out.printf("  \"%s\" is an elective slot. " +
                "Please provide the actual course details.%n", c.getCourseNo());
        String actualNo    = InputUtil.readNonBlank("  Actual Course No.   : ");
        String actualTitle = InputUtil.readNonBlank("  Actual Course Title : ");
        c.setActualCourseNo(actualNo);
        c.setActualTitle(actualTitle);
        System.out.println("  Elective resolved.");
    }

    // -------------------------------------------------------------------------
    // Case 2: Credited course
    // -------------------------------------------------------------------------

    private void enterCreditedCourse() {
        System.out.println();
        System.out.println("  A credited course is one completed at another program or school");
        System.out.println("  that has been officially credited toward your BSCS curriculum.");
        System.out.println();

        String courseNo = InputUtil.readNonBlank("  Curriculum Course No. it fulfills: ");
        Course match    = findByCourseNo(courseNo);

        if (match != null) {
            // Found in curriculum — mark it credited
            System.out.printf("  Found in curriculum: %s — %s%n",
                    match.getCourseNo(), match.getDescriptiveTitle());
            String from  = InputUtil.readNonBlank("  Credited from (program / school): ");
            String grade = InputUtil.readGrade(match.getCourseNo());

            match.setGrade(grade);
            match.setCredited(true);
            match.setCreditedFrom(from);
            System.out.printf("%n  Course %s marked as credited from \"%s\".%n",
                    match.getCourseNo(), from);
        } else {
            // Not found — add as extra credited course
            System.out.println("  Course not found in the BSCS curriculum.");
            System.out.println("  It will be added as an extra credited course on your record.");

            String title   = InputUtil.readNonBlank("  Descriptive Title              : ");
            String unitsIn = InputUtil.readNonBlank("  Units                          : ");
            String from    = InputUtil.readNonBlank("  Credited from (program/school) : ");
            String grade   = InputUtil.readGrade(courseNo);

            double units = parseUnits(unitsIn);
            Course extra = new Course(courseNo, title, units, "", 0, "Extra");
            extra.setGrade(grade);
            extra.setCredited(true);
            extra.setCreditedFrom(from);
            courses.add(extra);
            System.out.printf("%n  Credited course %s added to your record.%n", courseNo);
        }

        InputUtil.pressEnterToContinue();
    }

    // -------------------------------------------------------------------------
    // Case 3: Extra (non-curriculum) course
    // -------------------------------------------------------------------------

    private void enterExtraCourse() {
        System.out.println();
        System.out.println("  An extra course is any course you finished that is NOT");
        System.out.println("  required in the BSCS curriculum but is part of your record.");
        System.out.println();

        String courseNo = InputUtil.readNonBlank("  Course No.    : ");
        String title    = InputUtil.readNonBlank("  Course Title  : ");
        String unitsIn  = InputUtil.readNonBlank("  Units         : ");
        String grade    = InputUtil.readGrade(courseNo);

        double units = parseUnits(unitsIn);
        Course extra = new Course(courseNo, title, units, "", 0, "Extra");
        extra.setGrade(grade);
        courses.add(extra);

        System.out.printf("%n  Extra course %s added to your record.%n", courseNo);
        InputUtil.pressEnterToContinue();
    }

    // -------------------------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------------------------

    /** Searches for a course by course number (case-insensitive). */
    private Course findByCourseNo(String no) {
        for (Course c : courses) {
            if (c.getCourseNo().equalsIgnoreCase(no)) return c;
        }
        return null;
    }

    /** Parses a units string; defaults to 3.0 if the input is not a valid number. */
    private double parseUnits(String raw) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            System.out.println("  Invalid units value. Defaulting to 3.0.");
            return 3.0;
        }
    }
}
