================================================================================
  Fraction Calculator / Tester Application
  CS 122 Computer Programming 2 — Project
  Saint Louis University | BSCS
================================================================================

HOW TO OPEN IN INTELLIJ IDEA
------------------------------
1. Open IntelliJ IDEA -> File -> Open -> select the FractionCalculator folder
2. Mark the sources root:
   Right-click src -> Mark Directory As -> Sources Root
3. Set the Working Directory:
   Run -> Edit Configurations -> Working directory = $PROJECT_DIR$
4. Set Main class to: fraction.FractionCalculatorApp
5. Run!

PROJECT STRUCTURE
------------------
FractionCalculator/
 src/
    fraction/
       FractionCalculatorApp.java   <- MAIN ENTRY POINT
       model/
          Fraction.java             <- handles fraction logic (a/b form)
       util/
          ExpressionParser.java     <- parses expressions (e.g. 1/2 + 3/4)
          CalculatorEngine.java     <- performs operations
       ui/
          CalculatorUI.java         <- GUI (buttons, layout, display)

FEATURES
---------
 - Supports fraction input (e.g. 1/2, 3/4, 5/6)
 - Supports operations:
       + Addition
       - Subtraction
       * Multiplication
       / Division
 - Supports parentheses for complex expressions
 - Converts and simplifies results automatically
 - Handles improper fractions and mixed values
 - GUI-based calculator with clickable buttons
 - Clear (C) and Backspace (⌫) functions
 - Responsive layout when resizing the window

USAGE
------
 - Input expressions using buttons or keyboard
 - Example inputs:
       1/2 + 3/4
       (2/3) * (5/6)
       7/2 - 1/3
 - Press "=" to evaluate the expression
 - Result will automatically be simplified

BUTTON FUNCTIONS
-----------------
 [0–9]        -> Number input
 [ / ]        -> Fraction separator
 [ + - * / ]  -> Operations
 [ ( ) ]      -> Parentheses
 [ = ]        -> Evaluate expression
 [ C ]        -> Clear all input
 [ ⌫ ]        -> Delete last character

ENTER KEY FIX
--------------
The calculator processes input only when "=" is pressed.
Pressing Enter (if enabled) triggers evaluation once.
Prevents double-input or duplicate execution bugs.

ASSUMPTIONS
-----------
 - Fractions must follow a/b format
 - Denominator cannot be zero
 - Input must be a valid mathematical expression
 - Results are always simplified to lowest terms
 - Improper fractions are allowed

KNOWN LIMITATIONS
-----------------
 - Does not support decimal inputs (fractions only)
 - Invalid expressions may show error messages
 - No history feature (single calculation at a time)

DEVELOPERS NOTES
----------------
 - Built using Java Swing for GUI
 - Focused on clean UI and accurate fraction computation
 - Designed to mimic real calculator behavior while handling fractions
