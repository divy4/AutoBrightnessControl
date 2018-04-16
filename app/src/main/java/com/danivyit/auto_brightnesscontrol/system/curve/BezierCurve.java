package com.danivyit.auto_brightnesscontrol.system.curve;


import android.support.v4.util.Pair;

import com.danivyit.auto_brightnesscontrol.Util;

import java.util.Vector;

import static org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficientDouble;

public class BezierCurve extends Curve {

    PointToPointCurve p2pCurve;
    int numApproxPoints;

    /**
     * Creates a new BezierCurve.
     * @param numPoints The number of points that should be used to approximate the bezier curve.
     */
    public BezierCurve(int numPoints) {
        super();
        this.p2pCurve = null;
        this.numApproxPoints = numPoints;
    }

    /**
     * Sets the number of points that should be used to approximate the bezier curve.
     * @param numPoints
     */
    public void setNumApproxPoints(int numPoints) {
        if (numPoints != this.numApproxPoints) {
            this.numApproxPoints = numPoints;
            this.p2pCurve = null;
        }
    }

    /**
     * Adds a point to the curve.
     * @param x
     * @param y
     */
    @Override
    public void put(double x, double y) {
        super.put(x, y);
        this.p2pCurve = null;
    }

    /**
     * Adds a point to the curve.
     * @param p
     */
    @Override
    public void put(Pair<Double, Double> p) {
        super.put(p);
        this.p2pCurve = null;
    }

    /**
     * Deletes the point on the curve whose x value is closest to x.
     * @param x
     */
    @Override
    public void remove(double x) {
        super.remove(x);
        this.p2pCurve = null;
    }

    /**
     * Computes the position on the curve at t.
     * @param t The distance along the curve (i.e. t in [0,1])
     * @param controlPts The control points of the curve.
     * @return
     */
    private Pair<Double, Double> computePoint(double t, Vector<Pair<Double, Double>> controlPts) {
        int numControlPts = controlPts.size();
        double x = 0;
        double y = 0;
        double denominator = 0;
        // for each control point
        for (int controlIndex = 0; controlIndex < controlPts.size(); controlIndex++) {
            Pair<Double, Double> point = controlPts.get(controlIndex);
            // The bezier coefficient
            double coef = binomialCoefficientDouble(numControlPts - 1, controlIndex) *
                    Math.pow(t, controlIndex) *
                    Math.pow(1 - t, numControlPts - 1 - controlIndex);
            // add weighted control point
            x += coef * point.first;
            y += coef * point.second;
            denominator += coef;
        }
        return new Pair(x / denominator, y / denominator);
    }

    /**
     * Computes evenly timed points on the curve.
     * @param numPoints The number of points to use to approximate the curve.
     * @return
     */
    private Vector<Pair<Double, Double>> computePoints(int numPoints) {
        Vector<Pair<Double, Double>> controlPts = getPoints();
        Vector<Pair<Double, Double>> approxPts = new Vector<>();
        // for each approximation point
        for (int approxIndex = 0; approxIndex < numPoints; approxIndex++) {
            double t = Util.mapRange(approxIndex, 0, numPoints - 1, 0, 1);
            approxPts.add(computePoint(t, controlPts));
        }
        return approxPts;
    }

    /**
     * Updates the PointToPointCurve that estimates the bezier curve.
     * @param numPoints The number of points to use to approximate the curve.
     */
    private void updateP2PCurve(int numPoints) {
        Vector<Pair<Double, Double>> points = computePoints(numPoints);
        this.p2pCurve = new PointToPointCurve();
        for (Pair<Double, Double> point: points) {
            this.p2pCurve.put(point);
        }
    }

    /**
     * See Curve.predict.
     * @param x
     * @return
     */
    @Override
    public Double predict(double x) {
        if (isEmpty()) {
            return null;
        }
        if (this.p2pCurve == null) {
            updateP2PCurve(this.numApproxPoints);
        }
        return this.p2pCurve.predict(x);
    }
}
