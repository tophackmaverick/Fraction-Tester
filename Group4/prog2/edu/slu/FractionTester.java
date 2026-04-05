package prog2.edu.slu;

import pregroup01.Fraction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FractionTester extends JFrame {

    private JTextField input = new JTextField();
    private FractionLabel result = new FractionLabel(0, 1);
    private DefaultListModel<String> historyModel = new DefaultListModel<>();

    public FractionTester() {
        setTitle("Fraction Calculator");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // INPUT FIELD
        input.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        input.setHorizontalAlignment(JTextField.RIGHT);

        add(input, BorderLayout.NORTH);
        add(result, BorderLayout.SOUTH);

        // BUTTON PANEL
        JPanel buttons = new JPanel(new GridLayout(5, 4, 5, 5));

        String[] keys = {
                "7","8","9","⌫",
                "4","5","6","÷",
                "1","2","3","×",
                "0",".","a/b","-",
                "(",")","C","="
        };

        for (String k : keys) {
            JButton btn = new JButton(k);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.addActionListener(e -> handleInput(k));
            buttons.add(btn);
        }

        add(buttons, BorderLayout.CENTER);

        // HISTORY PANEL
        JList<String> historyList = new JList<>(historyModel);
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setPreferredSize(new Dimension(150, 0));
        add(scrollPane, BorderLayout.EAST);

        // LIVE PREVIEW (updates while typing)
        input.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updatePreview();
            }
        });

        // KEYBOARD SUPPORT
        setupKeyboard();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        input.requestFocusInWindow();
    }

    private void handleInput(String key) {

        if (key.equals("C")) {
            input.setText("");
            result.setFraction(0, 1);
        } else if (key.equals("=")) {
            try {
                String expr = input.getText()
                        .replace("×", "*")
                        .replace("÷", "/");

                Fraction res = ExpressionEvaluator.evaluate(expr);

                result.setFraction(res.getNumerator(), res.getDenominator());

                // SAVE TO HISTORY
                historyModel.addElement(input.getText() + " = " + res);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid Expression");
            }
        } else if (key.equals("a/b")) {

            String text = input.getText();
            int pos = input.getCaretPosition();

            // Insert template " / "
            String insert = "/";

            input.setText(text.substring(0, pos) + insert + text.substring(pos));

            // Move cursor AFTER the slash (so user types denominator next)
            input.setCaretPosition(pos + 1);

        }

        else if (key.equals("⌫")) {

            String text = input.getText();
            int pos = input.getCaretPosition();

            if (!text.isEmpty() && pos > 0) {
                input.setText(text.substring(0, pos - 1) + text.substring(pos));
                input.setCaretPosition(pos - 1);
            }
        }

        else {
            input.setText(input.getText() + key);
        }

        updatePreview();
    }

    private void updatePreview() {
        try {
            String expr = input.getText()
                    .replace("×", "*")
                    .replace("÷", "/");

            if (!expr.isEmpty()) {
                Fraction res = ExpressionEvaluator.evaluate(expr);
                result.setFraction(res.getNumerator(), res.getDenominator());
            }
        } catch (Exception e) {
            // ignore while typing
        }
    }

    // 🔥 KEYBOARD SUPPORT
    private void setupKeyboard() {

        input.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();

                if (key == KeyEvent.VK_ENTER) {
                    handleInput("=");
                } else if (key == KeyEvent.VK_ESCAPE) {
                    handleInput("C");
                } else if (key == KeyEvent.VK_BACK_SPACE) {
                    String text = input.getText();
                    if (!text.isEmpty()) {
                        input.setText(text.substring(0, text.length() - 1));
                    }
                    e.consume();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

                char c = e.getKeyChar();

                if ("0123456789+-*/^().".indexOf(c) != -1) {

                    if (c == '*') {
                        input.setText(input.getText() + "×");
                    } else if (c == '/') {
                        input.setText(input.getText() + "÷");
                    } else {
                        input.setText(input.getText() + c);
                    }

                    e.consume();
                } else {
                    e.consume();
                }
            }
        });
    }
}