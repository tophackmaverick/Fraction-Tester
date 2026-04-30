package checklist.data;

import checklist.model.Course;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CurriculumLoader.java
 *
 * Reads the BSCS curriculum from a plain-text data file and produces
 * a list of Course objects with no grades (clean template).
 *
 * Expected file format (pipe-delimited):
 *   YEAR|TERM|COURSE_NO|DESCRIPTIVE_TITLE|UNITS|PREREQ
 * Lines starting with '#' and blank lines are ignored.
 *
 * Author  : Member 2
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class CurriculumLoader {

    private final String filePath;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public CurriculumLoader(String filePath) {
        this.filePath = filePath;
    }

    // -------------------------------------------------------------------------
    // Public method
    // -------------------------------------------------------------------------

    /**
     * Parses the curriculum file and returns a fresh list of Course objects.
     *
     * @return list of courses in curriculum order
     * @throws IOException if the file cannot be read
     */
    public List<Course> load() throws IOException {
        List<Course> courses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Skip comments and blank lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\|", 6);
                if (parts.length < 5) {
                    System.out.printf("[WARNING] Skipping malformed line %d: %s%n",
                            lineNumber, line);
                    continue;
                }

                try {
                    int    year   = Integer.parseInt(parts[0].trim());
                    String term   = parts[1].trim();
                    String no     = parts[2].trim();
                    String title  = parts[3].trim();
                    double units  = Double.parseDouble(parts[4].trim());
                    String prereq = (parts.length == 6) ? parts[5].trim() : "";

                    courses.add(new Course(no, title, units, prereq, year, term));

                } catch (NumberFormatException e) {
                    System.out.printf("[WARNING] Skipping line %d due to bad number format: %s%n",
                            lineNumber, line);
                }
            }
        }

        return courses;
    }
}
