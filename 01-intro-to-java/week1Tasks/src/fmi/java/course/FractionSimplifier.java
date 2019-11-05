package fmi.java.course;

public class FractionSimplifier {
    public static String simplify(String fraction) {
        String[] fractionParts = fraction.split("/", 2);

        int numerator = Integer.parseInt(fractionParts[0]);
        int denominator = Integer.parseInt(fractionParts[1]);

        if (numerator == 0) {
            return "0";
        }

        // simplifying
        int gcd;
        do {
            gcd = getGcd(numerator, denominator);

            numerator /= gcd;
            denominator /= gcd;
        } while(gcd != 1);

        String result = Integer.toString(numerator);

        // only add the denominator if it isn't 1
        if (denominator != 1) {
            result += "/" + denominator;
        }

        return result;
    }

    private static int getGcd(int a, int b) {
        while(true) {
            if (a == b) {
                return a;
            }
            if (a > b) {
                a -= b;
            }
            else {
                b -= a;
            }
        }
    }
}
