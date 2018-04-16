package com.danivyit.auto_brightnesscontrol;

public class ExtraAssertions {

    /**
     * Asserts that two double values are similar.
     * @param expected
     */
    public void assertSimilar(double expected, double actual, double error) {
        double absDiff = Math.abs(expected - actual);
        assert(absDiff < error);
    }

    /**
     * Asserts that two double values are not similar.
     * @param expected
     */
    public void assertNotSimilar(double expected, double actual, double error) {
        double absDiff = Math.abs(expected - actual);
        assert(absDiff >= error);
    }

}
