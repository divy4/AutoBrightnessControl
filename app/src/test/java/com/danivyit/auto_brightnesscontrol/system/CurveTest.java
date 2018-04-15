package com.danivyit.auto_brightnesscontrol.system;

import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurveTest {

    private Curve curve;
    private Pair<Double, Double> p0, p1, p2, p3, p4;
    private double err;

    @Before
    public void setUp() throws Exception {
        p0 = new Pair(0.0, 4.0);
        p1 = new Pair(1.0, 1.0);
        p2 = new Pair(2.0, 0.0);
        p3 = new Pair(3.0, 3.0);
        p4 = new Pair(4.0, 2.0);
        curve = new PointToPointCurve();
        curve.put(p0);
        curve.put(p1);
        curve.put(p2);
        curve.put(p3);
        curve.put(p4);
        err = 1e-10;
    }

    @Test
    public void put() {
        curve = new PointToPointCurve();
        assertEquals(null, curve.first());
        curve.put(p0.first, p0.second);
        assertEquals(p0, curve.first());
        curve.put(p1);
        assertEquals(p1, curve.last());
    }

    @Test
    public void remove() {
        assertEquals(p0, curve.first());
        curve.remove(0);
        assertEquals(p1, curve.first());
        curve.remove(0);
        assertEquals(p2, curve.first());
        curve.remove(2.4);
        assertEquals(p3, curve.first());
    }

    @Test
    public void first() {
        curve = new PointToPointCurve();
        assertEquals(null, curve.first());
        curve.put(p1);
        assertEquals(p1, curve.first());
        curve.put(p0);
        assertEquals(p0, curve.first());
        curve.put(p2);
        assertEquals(p0, curve.first());
    }

    @Test
    public void last() {
        curve = new PointToPointCurve();
        assertEquals(null, curve.last());
        curve.put(p1);
        assertEquals(p1, curve.last());
        curve.put(p0);
        assertEquals(p1, curve.last());
        curve.put(p2);
        assertEquals(p2, curve.last());
    }

    @Test
    public void floor() {
        assertEquals(null, curve.prev(-1));
        assertEquals(p0, curve.prev(0));
        assertEquals(p0, curve.prev(0.99));
        assertEquals(p1, curve.prev(1));
        assertEquals(p4, curve.prev(1e10));
        curve = new PointToPointCurve();
        assertEquals(null, curve.prev(0));
    }

    @Test
    public void ceil() {
        assertEquals(null, curve.next(5));
        assertEquals(p4, curve.next(4));
        assertEquals(p4, curve.next(3.1));
        assertEquals(p3, curve.next(3));
        assertEquals(p0, curve.next(-1e10));
        curve = new PointToPointCurve();
        assertEquals(null, curve.next(0));
    }

    @Test
    public void closest() {
        assertEquals(p0, curve.closest(-1e10));
        assertEquals(p0, curve.closest(0));
        assertEquals(p0, curve.closest(0.4));
        try {
            assertEquals(p0, curve.closest(0.5));
        } catch (Exception e) {
            assertEquals(p1, curve.closest(0.5));
        }
        assertEquals(p1, curve.closest(0.6));
        assertEquals(p4, curve.closest(1e10));

    }
}