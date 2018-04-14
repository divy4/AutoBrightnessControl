package com.danivyit.auto_brightnesscontrol.system;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurveTest {

    private Curve curve;
    private double err;

    /**
     * Asserts that two double values are similar.
     * @param expected
     */
    public void assertSimilar(double expected, double actual, double error) {
        double absDiff = Math.abs(expected - actual);
        assert(absDiff < error);
    }

    @Before
    public void setUp() throws Exception {
        curve = new PointToPointCurve();
        curve.put(0, 4);
        curve.put(1, 1);
        curve.put(2, 0);
        curve.put(3, 3);
        curve.put(4, 2);
        err = 1e-10;
    }

    @Test
    public void put() {
        curve = new PointToPointCurve();
        assertEquals(null, curve.firstX());
        curve.put(0, 0);
        assertSimilar(0, curve.firstX(), err);
    }

    @Test
    public void remove() {
        assertSimilar(0, curve.firstX(), err);
        curve.remove(0);
        assertSimilar(1, curve.firstX(), err);
        curve.remove(0);
        assertSimilar(2, curve.firstX(), err);
        curve.remove(2.4);
        assertSimilar(3, curve.firstX(), err);
    }

    @Test
    public void firstX() {
        curve = new PointToPointCurve();
        assertEquals(null, curve.firstX());
        curve.put(0, 0);
        assertSimilar(0, curve.firstX(), err);
        curve.put(-1, 0);
        assertSimilar(-1, curve.firstX(), err);
        curve.put(1, 0);
        assertSimilar(-1, curve.firstX(), err);
    }

    @Test
    public void lastX() {
        curve = new PointToPointCurve();
        assertEquals(null, curve.lastX());
        curve.put(0, 0);
        assertSimilar(0, curve.lastX(), err);
        curve.put(-1, 0);
        assertSimilar(0, curve.lastX(), err);
        curve.put(1, 0);
        assertSimilar(1, curve.lastX(), err);
    }

    @Test
    public void floorX() {
        assertEquals(null, curve.floorX(-1));
        assertSimilar(0, curve.floorX(0), err);
        assertSimilar(0, curve.floorX(0.99), err);
        assertSimilar(1, curve.floorX(1), err);
        assertSimilar(4, curve.floorX(1e10), err);
        curve = new PointToPointCurve();
        assertEquals(null, curve.floorX(0));
    }

    @Test
    public void ceilX() {
        assertEquals(null, curve.ceilX(5));
        assertSimilar(4, curve.ceilX(4), err);
        assertSimilar(4, curve.ceilX(3.1), err);
        assertSimilar(3, curve.ceilX(3), err);
        assertSimilar(0, curve.ceilX(-1e10), err);
        curve = new PointToPointCurve();
        assertEquals(null, curve.ceilX(0));
    }

    @Test
    public void closestX() {
        assertSimilar(0, curve.closestX(-1e10), err);
        assertSimilar(0, curve.closestX(0), err);
        assertSimilar(0, curve.closestX(0.4), err);
        try {
            assertSimilar(0, curve.closestX(0.5), err);
        } catch (Exception e) {
            assertSimilar(1, curve.closestX(0.5), err);
        }
        assertSimilar(1, curve.closestX(0.6), err);
        assertSimilar(4, curve.closestX(1e10), err);

    }
}