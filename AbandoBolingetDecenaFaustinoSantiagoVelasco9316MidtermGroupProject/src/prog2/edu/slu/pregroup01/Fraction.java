package prog2.edu.slu.pregroup01;

public class Fraction {

    protected int numerator;
    protected int denominator;

    public Fraction() {
        this(0, 1);
    }

    public Fraction(int n, int d) {
        if (d == 0) throw new ArithmeticException("Denominator cannot be zero");
        numerator = n;
        denominator = d;
        simplify();
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public Fraction add(Fraction o) {
        return new Fraction(
                numerator * o.denominator + o.numerator * denominator,
                denominator * o.denominator
        );
    }

    public Fraction subtract(Fraction o) {
        return new Fraction(
        numerator * o.denominator - o.numerator * denominator,
        denominator * o.denominator
        );
    }

    public Fraction multiplyBy(Fraction o) {
        return new Fraction(numerator * o.numerator, denominator * o.denominator);
    }

    public Fraction divideBy(Fraction o) {
        return new Fraction(numerator * o.denominator, denominator * o.numerator);
    }

    public double toDouble() {
        return (double) numerator / denominator;
    }

    public String toString() {
        return denominator == 1 ? "" + numerator : numerator + "/" + denominator;
    }

    private void simplify() {
        int g = gcd(Math.abs(numerator), Math.abs(denominator));
        numerator /= g;
        denominator /= g;
        if (denominator < 0) {
            numerator *= -1;
            denominator *= -1;
        }
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}