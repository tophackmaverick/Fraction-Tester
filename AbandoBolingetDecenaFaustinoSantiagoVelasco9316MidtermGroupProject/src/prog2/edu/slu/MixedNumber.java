package prog2.edu.slu;
import prog2.edu.slu.pregroup01.Fraction;
public class MixedNumber extends Fraction {

    private int whole;

    public MixedNumber() {
        this(0, 0, 1);
    }

    public MixedNumber(int w, int n, int d) {
        super(n, d);
        whole = w;
        normalize();
    }

    public MixedNumber(Fraction f) {
        this(0, f.getNumerator(), f.getDenominator());
    }

    public Fraction toFraction() {
        return new Fraction(whole * getDenominator() + getNumerator(), getDenominator());
    }

    private void normalize() {
        Fraction f = toFraction();
        int num = f.getNumerator();
        int den = f.getDenominator();

        whole = num / den;
        numerator = Math.abs(num % den);
        denominator = den;
    }

    private MixedNumber fromFraction(Fraction f) {
        int num = f.getNumerator();
        int den = f.getDenominator();
        return new MixedNumber(num / den, Math.abs(num % den), den);
    }

    public MixedNumber add(MixedNumber o) {
        return fromFraction(toFraction().add(o.toFraction()));
    }

    public MixedNumber subtract(MixedNumber o) {
        return fromFraction(toFraction().subtract(o.toFraction()));
    }

    public MixedNumber multiplyBy(MixedNumber o) {
        return fromFraction(toFraction().multiplyBy(o.toFraction()));
    }

    public MixedNumber divideBy(MixedNumber o) {
        return fromFraction(toFraction().divideBy(o.toFraction()));
    }

    @Override
    public double toDouble() {
        return toFraction().toDouble();
    }

    @Override
    public String toString() {
        if (numerator == 0) return "" + whole;
        if (whole == 0) return numerator + "/" + denominator;
        return whole + " " + numerator + "/" + denominator;
    }
}