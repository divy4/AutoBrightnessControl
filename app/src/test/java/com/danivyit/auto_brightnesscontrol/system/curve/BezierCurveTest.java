package com.danivyit.auto_brightnesscontrol.system.curve;

import android.support.v4.util.Pair;

import com.danivyit.auto_brightnesscontrol.ExtraAssertions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BezierCurveTest extends ExtraAssertions {

    private BezierCurve curve;
    private Pair<Double, Double> p0, p1, p2, p3;
    private double err;

    @Before
    public void setUp() throws Exception {
        curve = new BezierCurve(100);
        p0 = new Pair(0.0, 0.0);
        p1 = new Pair(1.0, 1.0);
        p2 = new Pair(2.0, 4.0);
        p3 = new Pair(3.0, 9.0);
        err = 1e-5;
    }

    @Test
    public void setNumApproxPoints() {
        curve.put(p0);
        curve.put(p1);
        curve.put(p2);
        curve.setNumApproxPoints(2);
        Curve p2p = new PointToPointCurve();
        p2p.put(p0);
        p2p.put(p2);
        for (double x = p0.first; x <= p2.first; x += (p2.first - p0.first) / 100) {
            assertSimilar(p2p.predict(x), curve.predict(x), err);
        }
    }

    @Test
    public void predict() {
        curve.put(p0);
        curve.put(p1);
        Curve p2p = new PointToPointCurve();
        p2p.put(p0);
        p2p.put(p1);
        for (double x = p0.first; x <= p1.first; x += (p1.first - p0.first) / 100) {
            assertSimilar(p2p.predict(x), curve.predict(x), err);
        }

        curve.put(p2);
        curve.put(p3);
        p2p.put(p2);
        p2p.put(p3);
        for (double x = p0.first; x <= p3.first; x += (p3.first - p0.first) / 100) {
            assertTrue(p2p.predict(x) <= curve.predict(x));
        }
    }
}