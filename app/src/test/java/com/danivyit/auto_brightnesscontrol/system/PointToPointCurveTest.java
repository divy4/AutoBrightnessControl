package com.danivyit.auto_brightnesscontrol.system;

import android.support.v4.util.Pair;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PointToPointCurveTest {

    double err;
    Pair<Double, Double> p0, p1, p2, p3;
    PointToPointCurve curve;

    /**
     * Asserts that two double values are similar.
     * @param expected
     */
    public void assertSimilar(double expected, double actual, double error) {
        double absDiff = Math.abs(expected - actual);
        assertEquals(String.valueOf(expected), String.valueOf(actual));
        assert(absDiff < error);
    }

    @Before
    public void setUp() throws Exception {
        err = 1e-10;
        p0 = new Pair(0.0, 0.0);
        p1 = new Pair(1.0, 1.0);
        p2 = new Pair(2.0, 4.0);
        p3 = new Pair(3.0, 9.0);
        curve = new PointToPointCurve();
        curve.put(p0);
        curve.put(p1);
        curve.put(p2);
        curve.put(p3);
    }

    @Test
    public void predict() {
        assertSimilar(p0.second, curve.predict(-0.1), err);
        assertSimilar(p0.second, curve.predict(0), err);
        assertSimilar(2.5, curve.predict(1.5), err);
        assertSimilar(p3.second, curve.predict(3), err);
        assertSimilar(p3.second, curve.predict(3.1), err);
    }
}