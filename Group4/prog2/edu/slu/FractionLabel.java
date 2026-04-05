package prog2.edu.slu;

import javax.swing.*;
import java.awt.*;

public class FractionLabel extends JLabel {

    private int numerator;
    private int denominator;

    public FractionLabel(int n, int d) {
        this.numerator = n;
        this.denominator = d;

        setPreferredSize(new Dimension(80, 100)); // more flexible
    }

    public void setFraction(int n, int d) {
        this.numerator = n;
        this.denominator = d;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Dynamic font size (resizes with window)
        int fontSize = Math.min(width, height) / 4 + 10;
        Font font = new Font("Segoe UI", Font.BOLD, fontSize);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();

        // Handle invalid fraction
        if (denominator == 0) {
            g2.setColor(Color.RED);
            drawCentered(g2, "Error", width, height);
            return;
        }

        // Handle whole numbers
        if (numerator == 0) {
            g2.setColor(Color.BLACK);
            drawCentered(g2, String.valueOf(numerator / denominator), width, height);
            return;
        }

        String numStr = String.valueOf(numerator);
        String denStr = String.valueOf(denominator);

        int numWidth = fm.stringWidth(numStr);
        int denWidth = fm.stringWidth(denStr);

        int centerX = width / 2;
        int centerY = height / 2;

        int numX = centerX - numWidth / 2;
        int denX = centerX - denWidth / 2;

        int numY = centerY - 10;
        int denY = centerY + fm.getAscent() + 5;

        // 🎨 Numerator
        g2.setColor(new Color(0, 170, 200));
        g2.drawString(numStr, numX, numY);

        // 🎨 Fraction line (thicker + centered)
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        int lineWidth = Math.max(numWidth, denWidth) + 10;
        int lineX1 = centerX - lineWidth / 2;
        int lineX2 = centerX + lineWidth / 2;

        g2.drawLine(lineX1, centerY, lineX2, centerY);

        // 🎨 Denominator
        g2.setColor(new Color(180, 0, 150));
        g2.drawString(denStr, denX, denY);
    }

    // Helper method for centered text
    private void drawCentered(Graphics2D g2, String text, int width, int height) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height + fm.getAscent()) / 2;
        g2.drawString(text, x, y);
    }
}