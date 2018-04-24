package com.danivyit.auto_brightnesscontrol.system.curve;

import android.graphics.Point;
import android.support.v4.util.Pair;

import java.io.File;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Vector;


public abstract class Curve {

    private NavigableMap<Double, Double> points;

    /**
     * Creates a new Curve.
     */
    public Curve() {
        points = new TreeMap<>();
    }

    /**
     * Constructs a curve from a string.
     * @param str A string created from the toString() method.
     */
    public Curve(String str) {
        points = new TreeMap<>();
        for (String line: str.split("\n")) {
            if (line != "") {
                String[] values = line.split(",");
                double x = Double.parseDouble(values[0]);
                double y = Double.parseDouble(values[1]);
                put(x, y);
            }
        }
    }

    /**
     * Prints out information about the curve in string format.
     * @return
     */
    public String toString() {
        String str = "";
        for (Pair<Double, Double> point: getPoints()) {
            str += Double.toString(point.first) + "," + Double.toString(point.second) + "\n";
        }
        return str;
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
     * Returns the points on the curve in ascending order of x value.
     * @return A sorted set of points.
     */
    public Vector<Pair<Double, Double>> getPoints() {
        // convert navigable map to vector.
        Vector<Pair<Double, Double>> pts = new Vector();
        for (Map.Entry<Double, Double> entry: points.entrySet()) {
            pts.add(new Pair(entry.getKey(), entry.getValue()));
        }
        return pts;
    }

    /**
     * Returns a copy of the curve as a point to point curve.
     * @return
     */
    public PointToPointCurve asPointToPoint() {
        PointToPointCurve p2p = new PointToPointCurve();
        for (Pair<Double, Double> p : getPoints()) {
            p2p.put(p);
        }
        return p2p;
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
    public Pair<Double, Double> prev(double x) {
        return entryToPair(points.floorEntry(x));
    }

    /**
     * Returns the point whose x value is closest to x and is greater than or equal to x.
     * @param x
     * @return null if no point exists.
     */
    public Pair<Double, Double> next(double x) {
        return entryToPair(points.ceilingEntry(x));
    }

    /**
     * Returns the point whose x value is closest to x.
     * @param x
     * @return null if no point exists.
     */
    public Pair<Double, Double> closest(double x) {
        Pair<Double, Double> best = prev(x);
        Pair<Double, Double> ceil = next(x);
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
