package com.danivyit.auto_brightnesscontrol.system;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class Curve {

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double minXSep;
    private NavigableMap<Double, Double> points;

    /**
     * Creates a new Curve.
     * @param minX The minimum X value.
     * @param maxX The maximum X value.
     * @param minY The minimum Y value.
     * @param maxY The maximum Y value.
     * @param minXSep The minimum separation between two X values.
     */
    public Curve(double minX, double maxX, double minY, double maxY, double minXSep) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minXSep = minXSep;
        points = new TreeMap<>();
    }

    /**
     * Adds a point to the curve.
     * @param x
     * @param y
     */
    public void addPoint(double x, double y) {
        points.put(x, y);
    }

    /**
     * Returns the x value on the curve that is closest to x.
     * @param x
     * @return
     */
    private double closestX(double x) {
        // find closest values above and below
        double less = points.floorKey(x);
        double more = points.ceilingKey(x);
    }
}
