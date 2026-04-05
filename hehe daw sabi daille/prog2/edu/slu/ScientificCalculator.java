package prog2.edu.slu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScientificCalculator extends JFrame implements ActionListener {

    private JTextField display;
    private String expression = "";

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 22));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(6, 6, 5, 5));

        String[] buttons = {
                "sin","cos","tan","log","√","C",
                "(",")","^","π","e","⌫",
                "7","8","9","/","x²","",
                "4","5","6","*","%","",
                "1","2","3","-","1/x","",
                "0",".","=","+","",""
        };

        for (String text : buttons) {
            if (text.equals("")) {
                panel.add(new JLabel());
            } else {
                panel.add(createButton(text));
            }
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);

        // Styling
        if ("0123456789.".contains(text)) {
            btn.setBackground(new Color(230,230,230));
        } else if (text.equals("=")) {
            btn.setBackground(new Color(120,50,255));
            btn.setForeground(Color.WHITE);
        } else if ("+-*/".contains(text)) {
            btn.setBackground(new Color(200,200,200));
        } else {
            btn.setBackground(new Color(220,215,235));
        }

        btn.addActionListener(this);
        return btn;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            switch (cmd) {
                case "C":
                    expression = "";
                    break;

                case "⌫":
                    if (!expression.isEmpty()) {
                        expression = expression.substring(0, expression.length()-1);
                    }
                    break;

                case "=":
                    double result = evaluate(expression);
                    expression = String.valueOf(result);
                    break;

                case "sin":
                    expression = String.valueOf(Math.sin(Math.toRadians(evalSimple())));
                    break;

                case "cos":
                    expression = String.valueOf(Math.cos(Math.toRadians(evalSimple())));
                    break;

                case "tan":
                    expression = String.valueOf(Math.tan(Math.toRadians(evalSimple())));
                    break;

                case "log":
                    expression = String.valueOf(Math.log10(evalSimple()));
                    break;

                case "√":
                    expression = String.valueOf(Math.sqrt(evalSimple()));
                    break;

                case "x²":
                    expression = String.valueOf(Math.pow(evalSimple(), 2));
                    break;

                case "1/x":
                    expression = String.valueOf(1 / evalSimple());
                    break;

                case "π":
                    expression += Math.PI;
                    break;

                case "e":
                    expression += Math.E;
                    break;

                case "^":
                    expression += "^";
                    break;

                default:
                    expression += cmd;
            }

            display.setText(expression);

        } catch (Exception ex) {
            display.setText("Error");
            expression = "";
        }
    }

    // Simple evaluator (supports + - * / ^)
    private double evaluate(String expr) {
        expr = expr.replaceAll("π", String.valueOf(Math.PI));
        expr = expr.replaceAll("e", String.valueOf(Math.E));

        // Very basic evaluation using JavaScript engine
        try {
            return (double) new javax.script.ScriptEngineManager()
                    .getEngineByName("JavaScript")
                    .eval(expr.replace("^", "**"));
        } catch (Exception e) {
            return 0;
        }
    }

    private double evalSimple() {
        return evaluate(expression);
    }

    public static void main(String[] args) {
        new ScientificCalculator();
    }
}
