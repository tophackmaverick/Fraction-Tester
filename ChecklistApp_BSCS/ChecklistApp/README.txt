================================================================================
  My Checklist Monitoring Application
  CS 122 Computer Programming 2 — Final Group Project
  Saint Louis University | BSCS A.Y. 2018-2019
================================================================================

HOW TO OPEN IN INTELLIJ IDEA
------------------------------
1. Open IntelliJ IDEA -> File -> Open -> select the ChecklistApp folder
2. Mark the sources root:
   Right-click src/main/java -> Mark Directory As -> Sources Root
3. Set the Working Directory for the Run Configuration:
   Run -> Edit Configurations -> Working directory = $PROJECT_DIR$
   (This is required so the app finds data/bscs_curriculum.txt at runtime.)
4. Set Main class to: checklist.ChecklistApp
5. Run!

PROJECT STRUCTURE
------------------
ChecklistApp/
 data/
    bscs_curriculum.txt      <- BSCS curriculum data (required at runtime)
    student_record.txt       <- auto-created after first grade is entered
 src/main/java/checklist/
    ChecklistApp.java        <- MAIN ENTRY POINT (Member 6)
    model/
       Course.java           <- Course data model (Member 1)
    data/
       CurriculumLoader.java     <- reads curriculum file (Member 2)
       StudentRecordManager.java <- saves/loads student record (Member 2)
    features/
       ShowSubjectsByTerm.java      <- Menu <1> (Member 1)
       ShowSubjectsWithGrades.java  <- Menu <2> (Member 1)
       EnterGrades.java             <- Menu <3> (Member 4)
       EditCourse.java              <- Menu <4> (Member 4)
       ShowGPA.java                 <- Menu <5> (Member 5)
       ShowGradeRanking.java        <- Menu <6> (Member 5)
       ShowProgressSummary.java     <- Menu <7> (Member 5)
    util/
       InputUtil.java    <- validated console input (Member 3)
       PrintUtil.java    <- display/formatting helpers (Member 3)
       TermGrouper.java  <- groups courses by term (Member 3)

MENU OPTIONS
-------------
 <1> Show subjects for each school term
 <2> Show subjects with grades for each term
 <3> Enter grades for recently finished subjects
       - Regular curriculum course
       - Elective (resolves placeholder to actual course no. + title)
       - Credited course (from another program/school)
       - Extra course (outside the BSCS curriculum)
 <4> Edit a course (grade, title, units, credited status, elective details)
 <5> Show General Weighted Average (GWA) with Latin honors indicator
 <6> Show courses ranked by grade (descending order)
 <7> Show curriculum progress summary (progress bar, failed alerts, remaining list)
 <8> Quit (auto-saves before exit)

ENTER KEY FIX
--------------
pressEnterToContinue() now strictly requires a bare Enter keypress.
Typing any text before pressing Enter will prompt the user again.

ASSUMPTIONS
-----------
 - Passing grade is 75 and above
 - Grades are whole numbers 0-100
 - Failing grades are saved and flagged [FAILED]; they stay on record
 - Elective slots are resolved when graded for the first time
 - Record is auto-saved after every grade entry or edit operation
