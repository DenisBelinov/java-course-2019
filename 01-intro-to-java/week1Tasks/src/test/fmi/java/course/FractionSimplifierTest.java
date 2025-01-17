package test.fmi.java.course;

import fmi.java.course.FractionSimplifier;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FractionSimplifierTest {

    private static FractionSimplifier fractionSimplifier;

    @BeforeClass
    public static void setUp() {
        fractionSimplifier = new FractionSimplifier();
    }

    @Test
    public void testFraction_ProperFraction() {
        assertEquals("2/3", fractionSimplifier.simplify("4/6"));
    }

    @Test
    public void testFraction_ProperSimplifiedFraction() {
        assertEquals("10/11", fractionSimplifier.simplify("10/11"));
    }

    @Test
    public void testFraction_ProperFractionManyCommonDivisors() {
        assertEquals("1/4", fractionSimplifier.simplify("100/400"));
    }

    @Test
    public void testFraction_ImproperFractionToInteger() {
        assertEquals("2", fractionSimplifier.simplify("8/4"));
    }

    @Test
    public void testFraction_zero() {
        assertEquals("0", fractionSimplifier.simplify("0/3"));
    }
}
