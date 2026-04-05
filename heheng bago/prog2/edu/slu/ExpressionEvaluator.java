package prog2.edu.slu;

import pregroup01.Fraction;
import java.util.*;

public class ExpressionEvaluator {

    public static Fraction evaluate(String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        List<String> postfix = infixToPostfix(expr);
        return evalPostfix(postfix);
    }

    // Convert to postfix (Shunting Yard Algorithm)
    private static List<String> infixToPostfix(String expr) {

        expr = expr.replaceAll("\\s+", ""); // remove spaces

        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        StringTokenizer st = new StringTokenizer(expr, "+-*/^()", true);

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (token.isEmpty()) continue;

            // Handle negative numbers (e.g. -1/2)
            if (token.equals("-") && (output.isEmpty() || stack.isEmpty())) {
                if (st.hasMoreTokens()) {
                    token = "-" + st.nextToken();
                }
            }

            if (isNumber(token)) {
                output.add(token);
            }
            else if (token.equals("(")) {
                stack.push(token);
            }
            else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop();
                else throw new IllegalArgumentException("Mismatched parentheses");
            }
            else {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            String op = stack.pop();
            if (op.equals("(")) throw new IllegalArgumentException("Mismatched parentheses");
            output.add(op);
        }

        return output;
    }

    private static Fraction evalPostfix(List<String> tokens) {

        Stack<Fraction> stack = new Stack<>();

        for (String token : tokens) {

            if (isNumber(token)) {
                stack.push(parseFraction(token));
            }
            else {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression");
                }

                Fraction b = stack.pop();
                Fraction a = stack.pop();

                switch (token) {
                    case "+": stack.push(a.add(b)); break;
                    case "-": stack.push(a.subtract(b)); break;
                    case "*": stack.push(a.multiplyBy(b)); break;
                    case "/": stack.push(a.divideBy(b)); break;

                    case "^":
                        int power = (int) b.toDouble();

                        if (power < 0) {
                            throw new IllegalArgumentException("Negative exponent not supported");
                        }

                        Fraction result = new Fraction(1,1);
                        for (int i = 0; i < power; i++) {
                            result = result.multiplyBy(a);
                        }
                        stack.push(result);
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown operator: " + token);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return stack.pop();
    }

    private static boolean isNumber(String s) {
        return s.matches("-?[0-9]+(\\/[0-9]+)?|[0-9]*\\.?[0-9]+");
    }

    private static Fraction parseFraction(String s) {

        // Fraction (e.g. 3/4)
        if (s.contains("/")) {
            String[] f = s.split("/");
            return new Fraction(Integer.parseInt(f[0]), Integer.parseInt(f[1]));
        }

        // Decimal (e.g. 0.5)
        else if (s.contains(".")) {
            double d = Double.parseDouble(s);

            int denominator = 1000;
            int numerator = (int)(d * denominator);

            return new Fraction(numerator, denominator);
        }

        // Integer
        else {
            return new Fraction(Integer.parseInt(s), 1);
        }
    }

    private static int precedence(String op) {
        switch (op) {
            case "+": case "-": return 1;
            case "*": case "/": return 2;
            case "^": return 3;
        }
        return 0;
    }
}