package com.danivyit.auto_brightnesscontrol.system;

import android.support.v4.util.Pair;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public abstract class Curve {

    private NavigableMap<Double, Double> points;

    /**
     * Creates a new Curve.
     */
    public Curve() {
        points = new TreeMap<>();
    }

    /**
     * Adds a point to the curve.
     * @param x
     * @param y
     */
    public void put(double x, double y) {
        points.put(x, y);
    }

    /**
     * Adds a point to the curve.
     * @param p
     */
    public void put(Pair<Double, Double> p) {
        put(p.first, p.second);
    }

    /**
     * Deletes the point on the curve whose x value is closest to x.
     * @param x
     */
    public void remove(double x) {
        Pair<Double, Double> p = closest(x);
        if (p != null) {
            points.remove(p.first);
        }
    }

    /**
     * Tests if the curve contains no points.
     * @return
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * Returns the leftmost point on the curve.
     * @return null if no points are on the curve.
     */
    public Pair<Double, Double> first() {
        if (points.isEmpty()) {
            return null;
        } else {
            return entryToPair(points.firstEntry());
        }
    }

    /**
     * Returns the largest x value of a point on the curve.
     * @return null if no point exists.
     */
    public Pair<Double, Double> last() {
        if (points.isEmpty()) {
            return null;
        } else {
            return entryToPair(points.lastEntry());
        }
    }

    /**
     * Returns the point whose x value is closest to x and is less than or equal to x.
     * @param x
     * @return null if no point exists.
     */
    public Pair<Double, Double> floor(double x) {
        return entryToPair(points.floorEntry(x));
    }

    /**
     * Returns the point whose x value is closest to x and is greater than or equal to x.
     * @param x
     * @return null if no point exists.
     */
    public Pair<Double, Double> ceil(double x) {
        return entryToPair(points.ceilingEntry(x));
    }

    /**
     * Returns the point whose x value is closest to x.
     * @param x
     * @return null if no point exists.
     */
    public Pair<Double, Double> closest(double x) {
        Pair<Double, Double> best = floor(x);
        Pair<Double, Double> ceil = ceil(x);
        if (ceil != null && (best == null || ceil.first - x < x - best.first)) {
            best = ceil;
        }
        return best;
    }

    /**
     * Converts a Map.Entry to a pair.
     * @param entry
     * @return
     */
    private Pair<Double, Double> entryToPair(Map.Entry<Double, Double> entry) {
        if (entry == null) {
            return null;
        } else {
            return new Pair(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Predicts the y value of the curve at x.
     * @param x
     * @return Null if no points are on the curve.
     */
    public abstract Double predict(double x);
}
