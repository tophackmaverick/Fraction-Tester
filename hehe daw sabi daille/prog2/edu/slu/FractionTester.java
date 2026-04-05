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

        // MAIN PANEL (your original layout)
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.NONE; // 🔥 prevents stretching

        // INPUT 1
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Input 1:"), c);

        c.gridx = 1;
        panel.add(input1, c);

        // INPUT 2
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Input 2:"), c);

        c.gridx = 1;
        panel.add(input2, c);

        // BUTTONS
        JButton addBtn = createButton("Add");
        JButton subBtn = createButton("Subtract");
        JButton mulBtn = createButton("Multiply");
        JButton divBtn = createButton("Divide");

        c.gridx = 0;
        c.gridy = 2;
        panel.add(addBtn, c);

        c.gridx = 1;
        panel.add(subBtn, c);

        c.gridx = 0;
        c.gridy = 3;
        panel.add(mulBtn, c);

        c.gridx = 1;
        panel.add(divBtn, c);

        // RESULT
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        result.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(result, c);

        // 🔥 CENTER THE WHOLE UI
        setLayout(new GridBagLayout());
        add(panel);

        // BUTTON ACTIONS
        ActionListener handler = e -> compute(e.getActionCommand());

        addBtn.setActionCommand("Add");
        subBtn.setActionCommand("Subtract");
        mulBtn.setActionCommand("Multiply");
        divBtn.setActionCommand("Divide");

        addBtn.addActionListener(handler);
        subBtn.addActionListener(handler);
        mulBtn.addActionListener(handler);
        divBtn.addActionListener(handler);

        // WINDOW SETTINGS
        panel.setPreferredSize(new Dimension(300, 200)); // 🔥 keeps compact size
        pack(); // auto-size based on preferred size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(110, 30)); // consistent button size
        return btn;
    }

    private void compute(String op) {
        try {
            MixedNumber a = Parser.parse(input1.getText());
            MixedNumber b = Parser.parse(input2.getText());
            MixedNumber r;

            switch (op) {
                case "Add":
                    r = a.add(b);
                    break;
                case "Subtract":
                    r = a.subtract(b);
                    break;
                case "Multiply":
                    r = a.multiplyBy(b);
                    break;
                case "Divide":
                    r = a.divideBy(b);
                    break;
                default:
                    return;
            }

            result.setText("Result: " + r + " (" + r.toDouble() + ")");

        } catch (InvalidMixedNumberException e) {
            result.setText("Error: " + e.getMessage());
        }
    }
}