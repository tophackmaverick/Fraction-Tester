package checklist;

import checklist.data.CurriculumLoader;
import checklist.data.StudentRecordManager;
import checklist.features.*;
import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * ChecklistApp.java
 *
 * My Checklist Monitoring Application
 * CS 122 Computer Programming 2 — Final Group Project
 * Saint Louis University | BSCS A.Y. 2018-2019
 *
 * This is the application entry point. It initializes data, drives the
 * main menu loop, and delegates each menu option to its own dedicated
 * feature class. Auto-saves the student record after every mutation.
 *
 * Group Members:
 *   Member 1 — [Name]  (ShowSubjectsByTerm, ShowSubjectsWithGrades)
 *   Member 2 — [Name]  (CurriculumLoader, StudentRecordManager)
 *   Member 3 — [Name]  (InputUtil, PrintUtil, TermGrouper)
 *   Member 4 — [Name]  (EnterGrades, EditCourse)
 *   Member 5 — [Name]  (ShowGPA, ShowGradeRanking, ShowProgressSummary)
 *   Member 6 — [Name]  (ChecklistApp, integration and testing)
 *
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class ChecklistApp {

    // -------------------------------------------------------------------------
    // File paths — resolved dynamically so the app works regardless of
    // which folder IntelliJ uses as the working directory.
    // -------------------------------------------------------------------------

    private static final String DATA_DIR        = resolveDataDir();
    private static final String CURRICULUM_FILE = DATA_DIR + "bscs_curriculum.txt";
    private static final String RECORD_FILE     = DATA_DIR + "student_record.txt";

    /**
     * Locates the data/ folder by trying several candidate paths:
     *
     *  1. "data/"  relative to the JVM working directory.
     *     Works when IntelliJ Run Config has Working directory = $PROJECT_DIR$.
     *
     *  2. Walks up from the compiled .class root (e.g. out/production/…)
     *     up to four levels until it finds a sibling "data/" folder.
     *     Works with the default IntelliJ output layout even when the
     *     working directory is NOT set to the project root.
     *
     * Falls back to "data/" so any subsequent IOException still produces
     * a readable error message.
     */
    private static String resolveDataDir() {
        // ── Candidate 1: relative to current JVM working directory ───────────
        File cwd = new File("data");
        if (cwd.isDirectory()) {
            return "data" + File.separator;
        }

        // ── Candidate 2: relative to the .class / .jar location ──────────────
        try {
            File classRoot = new File(
                ChecklistApp.class.getProtectionDomain()
                                  .getCodeSource()
                                  .getLocation()
                                  .toURI()
            );
            // classRoot may be a .jar file or the classes output folder.
            // Walk upward (max 5 levels) looking for a "data" sibling folder.
            File current = classRoot.isDirectory() ? classRoot : classRoot.getParentFile();
            for (int level = 0; level <= 5 && current != null; level++) {
                File candidate = new File(current, "data");
                if (candidate.isDirectory()) {
                    return candidate.getAbsolutePath() + File.separator;
                }
                current = current.getParentFile();
            }
        } catch (URISyntaxException | SecurityException ignored) {
            // Fall through to default
        }

        // ── Fallback ──────────────────────────────────────────────────────────
        return "data" + File.separator;
    }

    // -------------------------------------------------------------------------
    // Application state
    // -------------------------------------------------------------------------

    private List<Course>               courses;
    private final StudentRecordManager recordManager;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ChecklistApp() {
        this.recordManager = new StudentRecordManager(RECORD_FILE);
    }

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    /**
     * Loads data on startup.
     * If a saved student record exists, it is loaded from disk.
     * Otherwise, the fresh curriculum template is loaded and no record is created
     * until the first grade is entered (lazy initialization).
     */
    private void init() throws IOException {
        if (recordManager.recordExists()) {
            courses = recordManager.load();
            System.out.println("  [INFO] Student record loaded from: " + RECORD_FILE);
        } else {
            CurriculumLoader loader = new CurriculumLoader(CURRICULUM_FILE);
            courses = loader.load();
            System.out.println("  [INFO] Curriculum loaded from: " + CURRICULUM_FILE);
            System.out.println("  [INFO] No saved record found. Starting fresh.");
        }
        System.out.printf("  [INFO] %d courses ready.%n", courses.size());
    }

    // -------------------------------------------------------------------------
    // Main menu loop
    // -------------------------------------------------------------------------

    private void run() {
        boolean running = true;

        while (running) {
            printMenu();

            int choice = InputUtil.readIntInRange(
                    "  Enter a number corresponding to your choice: ", 1, 8);

            switch (choice) {
                case 1 -> new ShowSubjectsByTerm(courses).execute();

                case 2 -> new ShowSubjectsWithGrades(courses).execute();

                case 3 -> {
                    new EnterGrades(courses).execute();
                    autosave();
                }
                case 4 -> {
                    new EditCourse(courses).execute();
                    autosave();
                }
                case 5 -> new ShowGPA(courses).execute();

                case 6 -> new ShowGradeRanking(courses).execute();

                case 7 -> new ShowProgressSummary(courses).execute();

                case 8 -> {
                    autosave();
                    System.out.println("\n  ..Thank you. Goodbye!\n");
                    running = false;
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Menu display
    // -------------------------------------------------------------------------

    private void printMenu() {
        System.out.println();
        System.out.println(PrintUtil.THICK_LINE);
        System.out.println("  My Checklist Monitoring Application");
        System.out.println("  BSCS — Saint Louis University  A.Y. 2018-2019");
        System.out.println(PrintUtil.THICK_LINE);
        System.out.println("  <1>  Show subjects for each school term");
        System.out.println("  <2>  Show subjects with grades for each term");
        System.out.println("  <3>  Enter grades for subjects recently finished");
        System.out.println("  <4>  Edit a course");
        System.out.println("  <5>  Show General Weighted Average (GWA)");
        System.out.println("  <6>  Show courses ranked by grade (descending)");
        System.out.println("  <7>  Show curriculum progress summary");
        System.out.println("  <8>  Quit");
        System.out.println(PrintUtil.THICK_LINE);
    }

    // -------------------------------------------------------------------------
    // Auto-save
    // -------------------------------------------------------------------------

    /**
     * Saves the current course list to disk after any operation that
     * modifies data (enter grades, edit course, quit).
     */
    private void autosave() {
        try {
            recordManager.save(courses);
            System.out.println("  [INFO] Record saved to: " + RECORD_FILE);
        } catch (IOException e) {
            System.out.println("  [WARNING] Could not save record: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Program entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println();
        System.out.println(PrintUtil.THICK_LINE);
        System.out.println("  My Checklist Monitoring Application");
        System.out.println("  Initializing...");
        System.out.println(PrintUtil.THICK_LINE);

        ChecklistApp app = new ChecklistApp();

        try {
            app.init();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load data: " + e.getMessage());
            System.err.println("  Looked for: " + CURRICULUM_FILE);
            System.err.println("  Please ensure bscs_curriculum.txt is inside a 'data' folder");
            System.err.println("  in the same directory as the project, then re-run.");
            System.exit(1);
        }

        printAssumptionsAndLimitations();
        app.run();
    }

    // -------------------------------------------------------------------------
    // Startup notice
    // -------------------------------------------------------------------------

    private static void printAssumptionsAndLimitations() {
        System.out.println();
        System.out.println("  " + PrintUtil.THIN_LINE);
        System.out.println("  ASSUMPTIONS AND LIMITATIONS");
        System.out.println("  " + PrintUtil.THIN_LINE);
        System.out.println("  1. The passing grade is 75 and above.");
        System.out.println("  2. Grades are entered as whole numbers (0-100).");
        System.out.println("  3. A failing grade is accepted and flagged; the course");
        System.out.println("     remains on record and must be retaken.");
        System.out.println("  4. Elective slots (CSE ELEC1-4) are resolved to actual");
        System.out.println("     course numbers and titles when a grade is entered.");
        System.out.println("  5. Courses transferred from another program or school");
        System.out.println("     can be credited and traced to their source.");
        System.out.println("  6. Extra courses outside the curriculum can be recorded.");
        System.out.println("  7. The record is auto-saved after every entry or edit.");
        System.out.println("  " + PrintUtil.THIN_LINE);
    }
}
