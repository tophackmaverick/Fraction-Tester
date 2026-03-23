package prog2.edu.slu;

import javax.swing.SwingUtilities;

/**
 * Entry point of the application
 */
public class MainApp {

    public static void main(String[] args) {

        // Ensures GUI runs on correct thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FractionTester();
            }
        });

    }
}