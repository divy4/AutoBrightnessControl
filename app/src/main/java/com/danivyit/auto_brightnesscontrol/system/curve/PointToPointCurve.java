package com.danivyit.auto_brightnesscontrol.system.curve;

import android.support.v4.util.Pair;

import com.danivyit.auto_brightnesscontrol.Util;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

public class PointToPointCurve extends Curve {

    // the minimum distance the x values of two points should be before they are considered the same point.
    private static double planck = 1e-10;

    /**
     * See Curve.predict.
     * @param x
     * @return
     */
    @Override
    public Double predict(double x) {
        // empty curve
        if (isEmpty()) {
            return null;
        }
        // get points
        Pair<Double, Double> prev = prev(x);
        Pair<Double, Double> next = next(x);
        Double predicted;
        // before first
        if (prev == null) {
            predicted = next.second;
        // after last
        } else if (next == null) {
            predicted = prev.second;
        // on point
        } else if (next.first - prev.first < planck) {
            predicted = prev.second;
        // in between points
        } else {
            predicted = Util.mapRange(x, prev.first, next.first, prev.second, next.second);
        }
        return predicted;
    }

}
