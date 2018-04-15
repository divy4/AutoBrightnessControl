package com.danivyit.auto_brightnesscontrol.system.curve;

import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

public class BezierCurve extends Curve {

    PointToPointCurve p2pCurve;
    int numInterpolPts;

    public BezierCurve(int numInterpolPts) {
        this.p2pCurve = null;
        this.numInterpolPts = numInterpolPts;
    }

    @Override
    public Double predict(double x) {
        return null;
    }
}
