package prog2.edu.slu;

import prog2.edu.slu.pregroup01.Fraction;

public class Parser {

    public static MixedNumber parse(String input) throws InvalidMixedNumberException {
        try {
            input = input.trim();

            if (input.contains(" ")) {
                String[] parts = input.split(" ");
                int whole = Integer.parseInt(parts[0]);

                String[] frac = parts[1].split("/");
                int num = Integer.parseInt(frac[0]);
                int den = Integer.parseInt(frac[1]);

                if (den == 0) throw new InvalidMixedNumberException("Denominator cannot be zero");

                return new MixedNumber(whole, num, den);
            }
            else if (input.contains("/")) {
                String[] frac = input.split("/");
                int num = Integer.parseInt(frac[0]);
                int den = Integer.parseInt(frac[1]);

                if (den == 0) throw new InvalidMixedNumberException("Denominator cannot be zero");

                return new MixedNumber(new Fraction(num, den));
            }
            else {
                int whole = Integer.parseInt(input);
                return new MixedNumber(whole, 0, 1);
            }

        } catch (Exception e) {
            throw new InvalidMixedNumberException("Invalid input format");
        }
    }
}
