package checklist.model;

/**
 * Course.java
 *
 * Represents a single course entry in the BSCS curriculum or
 * the student's personal record. Stores all data fields needed
 * by the application: curriculum info, grade, credit status,
 * and elective resolution.
 *
 * Author  : Member 1
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class Course {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private String courseNo;          // e.g. "CS 122"
    private String descriptiveTitle;  // e.g. "Computer Programming 2"
    private double units;             // e.g. 2.0
    private String prereq;            // e.g. "CS 112"
    private int    year;              // 1–4  (0 = extra/outside curriculum)
    private String term;              // "1st Semester", "2nd Semester", "Short Term", "Extra"

    private String  grade;            // numeric string or null = not yet taken
    private boolean credited;         // true if transferred from another program/school
    private String  creditedFrom;     // e.g. "BSIT - SLU" or "University of X"
    private String  actualCourseNo;   // electives: real course no. once taken
    private String  actualTitle;      // electives: real title once taken

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public Course(String courseNo, String descriptiveTitle, double units,
                  String prereq, int year, String term) {
        this.courseNo         = courseNo;
        this.descriptiveTitle = descriptiveTitle;
        this.units            = units;
        this.prereq           = prereq;
        this.year             = year;
        this.term             = term;
        this.grade            = null;
        this.credited         = false;
        this.creditedFrom     = "";
        this.actualCourseNo   = "";
        this.actualTitle      = "";
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String  getCourseNo()         { return courseNo; }
    public String  getDescriptiveTitle() { return descriptiveTitle; }
    public double  getUnits()            { return units; }
    public String  getPrereq()           { return prereq; }
    public int     getYear()             { return year; }
    public String  getTerm()             { return term; }
    public String  getGrade()            { return grade; }
    public boolean isCredited()          { return credited; }
    public String  getCreditedFrom()     { return creditedFrom; }
    public String  getActualCourseNo()   { return actualCourseNo; }
    public String  getActualTitle()      { return actualTitle; }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setCourseNo(String courseNo)             { this.courseNo = courseNo; }
    public void setDescriptiveTitle(String title)        { this.descriptiveTitle = title; }
    public void setUnits(double units)                   { this.units = units; }
    public void setPrereq(String prereq)                 { this.prereq = prereq; }
    public void setYear(int year)                        { this.year = year; }
    public void setTerm(String term)                     { this.term = term; }
    public void setGrade(String grade)                   { this.grade = grade; }
    public void setCredited(boolean credited)            { this.credited = credited; }
    public void setCreditedFrom(String from)             { this.creditedFrom = from; }
    public void setActualCourseNo(String actualCourseNo) { this.actualCourseNo = actualCourseNo; }
    public void setActualTitle(String actualTitle)       { this.actualTitle = actualTitle; }

    // -------------------------------------------------------------------------
    // Derived / helper methods
    // -------------------------------------------------------------------------

    /** Returns true if a grade has been entered for this course. */
    public boolean isTaken() {
        return grade != null && !grade.isBlank();
    }

    /** Returns true if the course has been taken and the grade is >= 75. */
    public boolean isPassed() {
        if (!isTaken()) return false;
        try {
            return Double.parseDouble(grade) >= 75.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Returns true if this course is an elective placeholder. */
    public boolean isElective() {
        String no = courseNo.toUpperCase();
        return no.startsWith("CSE") || no.contains("ELEC");
    }

    /**
     * Returns the course number to display:
     * the actual course no. if resolved (elective), otherwise the placeholder.
     */
    public String getDisplayCourseNo() {
        return actualCourseNo.isBlank() ? courseNo : actualCourseNo;
    }

    /**
     * Returns the title to display:
     * the actual title if resolved (elective), otherwise the descriptive title.
     */
    public String getDisplayTitle() {
        return actualTitle.isBlank() ? descriptiveTitle : actualTitle;
    }

    /**
     * Serializes this course to a pipe-delimited string for file storage.
     * Format: COURSE_NO|TITLE|UNITS|PREREQ|YEAR|TERM|GRADE|CREDITED|CREDITED_FROM|ACTUAL_NO|ACTUAL_TITLE
     */
    @Override
    public String toString() {
        return String.join("|",
            courseNo,
            descriptiveTitle,
            String.valueOf(units),
            prereq,
            String.valueOf(year),
            term,
            grade == null ? "" : grade,
            String.valueOf(credited),
            creditedFrom,
            actualCourseNo,
            actualTitle
        );
    }
}
