package prog2.edu.slu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class FractionTester extends JFrame {

    private JTextField input1 = new JTextField(10);
    private JTextField input2 = new JTextField(10);
    private JLabel result = new JLabel("Result will appear here");

    public FractionTester() {
        setTitle("Mixed Fraction Calculator");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        String[] labels = {"Input 1:", "Input 2:"};
        JTextField[] fields = {input1, input2};
        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0;
            c.gridy = i;
            panel.add(new JLabel(labels[i]), c);
            c.gridx = 1;
            panel.add(fields[i], c);
        }

        String[] ops = {"Add", "Subtract", "Multiply", "Divide"};
        JButton[] opButtons = new JButton[ops.length];
        ActionListener handler = e -> compute(e.getActionCommand());
        for (int i = 0; i < ops.length; i++) {
            opButtons[i] = createButton(ops[i]);
            opButtons[i].setActionCommand(ops[i]);
            opButtons[i].addActionListener(handler);
            c.gridx = i % 2;
            c.gridy = 2 + i / 2;
            panel.add(opButtons[i], c);
        }

        JButton fracBtn = createButton("a/b");
        JButton acBtn = createButton("AC");

        c.gridx = 0;
        c.gridy = 4;
        panel.add(fracBtn, c);
        c.gridx = 1;
        panel.add(acBtn, c);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        result.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(result, c);

        setLayout(new GridBagLayout());
        add(panel);

        fracBtn.addActionListener(e -> {
            JTextField target = input1.isFocusOwner() ? input1 : input2.isFocusOwner() ? input2 : null;
            if (target != null) {
                int pos = target.getCaretPosition();
                String text = target.getText();
                target.setText(text.substring(0, pos) + "/" + text.substring(pos));
                target.setCaretPosition(pos + 1);
            }
        });

        acBtn.addActionListener(e -> {
            input1.setText("");
            input2.setText("");
            result.setText("Result will appear here");
        });

        setMinimumSize(new Dimension(350, 300));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        return btn;
    }

    private void compute(String op) {
        try {
            MixedNumber a = Parser.parse(input1.getText());
            MixedNumber b = Parser.parse(input2.getText());
            MixedNumber r;
            switch (op) {
                case "Add": r = a.add(b); break;
                case "Subtract": r = a.subtract(b); break;
                case "Multiply": r = a.multiplyBy(b); break;
                case "Divide": r = a.divideBy(b); break;
                default: return;
            }
            result.setText("Result: " + r + " (" + r.toDouble() + ")");
        } catch (InvalidMixedNumberException e) {
            result.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FractionTester::new);
    }
}