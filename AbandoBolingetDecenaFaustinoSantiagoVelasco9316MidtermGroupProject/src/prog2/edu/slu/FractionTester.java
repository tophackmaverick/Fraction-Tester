package prog2.edu.slu;

import prog2.edu.slu.pregroup01.Fraction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class FractionTester extends JFrame {

    private final JTextField input = new JTextField();
    private final JLabel result = new JLabel("Result will appear here");

    private static final Color BG = new Color(18, 18, 18);
    private static final Color BTN = new Color(255, 140, 0);
    private static final Color BLUE = new Color(30, 144, 255);
    private static final Color FIELD_BG = new Color(40, 40, 40);
    private static final Color RESULT_BG = new Color(30, 30, 30);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 40);
    private static final Font FIELD_FONT = new Font("Consolas", Font.PLAIN, 26);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font RESULT_FONT = new Font("Segoe UI", Font.BOLD, 32);

    private static final String[] KEYS = {
            "7","8","9","<-",
            "4","5","6","÷",
            "1","2","3","×",
            "0",".","a/b","-",
            "(",")","C","+","="
    };

    public FractionTester() {
        setTitle("Fraction Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 700));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridwidth = 4;

        panel.add(makeTitle(), at(c, 0));
        styleInput();
        panel.add(input, at(c, 1));
        panel.add(makeButtonPanel(), at(c, 2));
        styleResult();
        panel.add(result, at(c, 3));

        add(panel);
        getContentPane().setBackground(BG);
        setVisible(true);
    }

    private GridBagConstraints at(GridBagConstraints c, int y) {
        c.gridy = y;
        return c;
    }

    private JLabel makeTitle() {
        JLabel title = new JLabel("Fraction Calculator", SwingConstants.CENTER);
        title.setForeground(Color.ORANGE);
        title.setFont(TITLE_FONT);
        return title;
    }

    private void styleInput() {
        input.setBackground(FIELD_BG);
        input.setForeground(Color.WHITE);
        input.setCaretColor(Color.ORANGE);
        input.setBorder(BorderFactory.createLineBorder(BLUE, 2));
        input.setFont(FIELD_FONT);
        input.setHorizontalAlignment(JTextField.RIGHT);
        input.setPreferredSize(new Dimension(300, 70));
    }

    private void styleResult() {
        result.setHorizontalAlignment(SwingConstants.CENTER);
        result.setForeground(BLUE);
        result.setFont(RESULT_FONT);
        result.setPreferredSize(new Dimension(0, 120));
        result.setOpaque(true);
        result.setBackground(RESULT_BG);
        result.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BLUE, 2),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));
    }

    private JPanel makeButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 4, 15, 15));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 60, 20, 60));

        for (String key : KEYS) {
            JButton btn = makeButton(key);
            btn.addActionListener(e -> handleInput(key));
            panel.add(btn);
        }

        for (int i = KEYS.length; i < 24; i++) panel.add(new JLabel());
        return panel;
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(BTN);
        btn.setForeground(Color.BLACK);
        btn.setFont(BUTTON_FONT);
        btn.setPreferredSize(new Dimension(90, 70));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(BLUE);
                btn.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(BTN);
                btn.setForeground(Color.BLACK);
            }
        });
        return btn;
    }

    private void handleInput(String key) {
        switch (key) {
            case "C" -> {
                input.setText("");
                result.setText("Result will appear here");
            }
            case "=" -> evaluateInput();
            case "<-" -> backspace();
            case "a/b" -> insert("/");
            default -> insert(key);
        }
        input.requestFocus();
    }

    private void insert(String s) {
        int pos = input.getCaretPosition();
        String text = input.getText();
        input.setText(text.substring(0, pos) + s + text.substring(pos));
        input.setCaretPosition(pos + s.length());
    }

    private void backspace() {
        int pos = input.getCaretPosition();
        String text = input.getText();
        if (pos > 0 && !text.isEmpty()) {
            input.setText(text.substring(0, pos - 1) + text.substring(pos));
            input.setCaretPosition(pos - 1);
        }
    }

    private void evaluateInput() {
        try {
            String expr = input.getText().replace("×", "*").replace("÷", "/");
            Fraction res = evaluate(expr);
            result.setText("<html><center><b>" + res + "</b><br>Decimal: " + res.toDouble() + "</center></html>");
        } catch (Exception e) {
            result.setText("Error");
        }
    }

    private Fraction evaluate(String expr) {
        Stack<Fraction> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            if (ch == ' ') continue;

            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '/')) {
                    sb.append(expr.charAt(i++));
                }
                i--;
                values.push(parseFraction(sb.toString()));
            } else if (ch == '(') {
                ops.push(ch);
            } else if (ch == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    Fraction b = values.pop(), a = values.pop();
                    values.push(applyOp(ops.pop(), a, b));
                }
                ops.pop();
            } else if (isOperator(ch)) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    Fraction b = values.pop(), a = values.pop();
                    values.push(applyOp(ops.pop(), a, b));
                }
                ops.push(ch);
            }
        }

        while (!ops.isEmpty()) {
            Fraction b = values.pop(), a = values.pop();
            values.push(applyOp(ops.pop(), a, b));
        }

        return values.pop();
    }

    private Fraction parseFraction(String s) {
        if (!s.contains("/")) return new Fraction(Integer.parseInt(s), 1);
        String[] p = s.split("/");
        return new Fraction(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
    }

    private boolean isOperator(char ch) {
        return "+-*/".indexOf(ch) >= 0;
    }

    private int precedence(char op) {
        return (op == '+' || op == '-') ? 1 : (op == '*' || op == '/') ? 2 : 0;
    }

    private Fraction applyOp(char op, Fraction a, Fraction b) {
        return switch (op) {
            case '+' -> a.add(b);
            case '-' -> a.subtract(b);
            case '*' -> a.multiplyBy(b);
            case '/' -> a.divideBy(b);
            default -> throw new IllegalArgumentException("Invalid operator");
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FractionTester::new);
    }
}