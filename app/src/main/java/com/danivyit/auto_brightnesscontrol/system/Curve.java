package com.danivyit.auto_brightnesscontrol.system;

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
     * Deletes the point on the curve whose x value is closest to x.
     * @param x
     */
    public void remove(double x) {
        Double key = closestX(x);
        if (key != null) {
            points.remove(key);
        }
    }

    /**
     * Returns the smallest x value of a point on the curve.
     * @return null if no points are on the curve.
     */
    public Double firstX() {
        if (points.isEmpty()) {
            return null;
        } else {
            return points.firstKey();
        }
    }

    /**
     * Returns the largest x value of a point on the curve.
     * @return null if no points are on the curve.
     */
    public Double lastX() {
        if (points.isEmpty()) {
            return null;
        } else {
            return points.lastKey();
        }
    }

    /**
     * Returns the x value of the point closest to x that is less than or equal to x.
     * @param x
     * @return
     */
    public Double floorX(double x) {
        return points.floorKey(x);
    }

    /**
     * Returns the x value of the point closest to x that is greater than or equal to x.
     * @param x
     * @return
     */
    public Double ceilX(double x) {
        return points.ceilingKey(x);
    }

    /**
     * Returns the x value of a point on the curve that is closest to x.
     * @param x
     * @return The x value of the point if one exists, null otherwise.
     */
    public Double closestX(double x) {
        Double best = points.floorKey(x);
        Double ceil = points.ceilingKey(x);
        if (ceil != null && (best == null || ceil - x < x - best)) {
            best = ceil;
        }
        return best;
    }

    /**
     * Predicts the y value of the curve at x.
     * @param x
     * @return Null if no points are on the curve.
     */
    public abstract Double predict(double x);
}
