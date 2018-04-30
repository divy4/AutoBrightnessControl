package com.danivyit.auto_brightnesscontrol.system;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.Util;
import com.danivyit.auto_brightnesscontrol.system.curve.BezierCurve;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

import java.util.Observable;

public class BrightnessSystem extends java.util.Observable {

    private Context context;

    private Thread backgroundThread;
    private BacklightUpdater lightUpdater;
    private boolean enabled;

    private Curve curve;
    private Pair<Double, Double> lastTouch;

    public BrightnessSystem(Context context) {
        this.context = context;
        // curve
        String curveStr = Util.loadString(context, "curve");
        if (curveStr != null) {
            this.curve = new BezierCurve(50, 10, curveStr);
        } else {
            resetCurve();
        }
        resetCurve();
        this.lastTouch = null;
        // updater
        this.lightUpdater = new BacklightUpdater(context, 2, 0.1);
        lightUpdater.setAdjustmentCurve(curve);
        this.enabled = false;
        notifyChange();
    }

    /**
     * Called when the application is being destroyed.
     */
    public void onDestroy() {
        lightUpdater.queueStop();
    }

    /**
     * Enables the curve.
     */
    public void enable() {
        if (!enabled) {
            backgroundThread = new Thread(lightUpdater);
            backgroundThread.start();
            enabled = true;
        }
    }

    /**
     * Disables the curve.
     */
    public void disable() {
        if (enabled) {
            lightUpdater.queueStop();
            backgroundThread = null;
            enabled = false;
        }
    }

    /**
     * Gets the update frequency.
     */
    public double getUpdateFreq() {
        return lightUpdater.getDelay();
    }

    /**
     * Sets the update frequency.
     * @param sec
     */
    public void setUpdateFreq(double sec) {
        lightUpdater.setDelay(sec);
        notifyChange();
    }

    /**
     * Resets the brightness curve.
     */
    public void resetCurve() {
        this.curve = new BezierCurve(50, 10);
        curve.put(0, 0.3);
        curve.put(1, 0.8);
        notifyChange();
    }

    /**
     * Returns the brightness curve.
     * @return
     */
    public Curve getCurve() {
        return curve;
    }

    /**
     * Makes sure an x or y value is on the curve.
     * @param xOrY
     * @return
     */
    private double curveRegulateCoord(double xOrY) {
        return Math.max(0, Math.min(1, xOrY));
    }

    /**
     * Removes the nearest point to x,y, if it's close enough.
     * @param x
     * @param y
     */
    private void curveRemoveNearby(double x, double y) {
        Pair<Double, Double> nearby = curve.closest(x);
        if (Math.abs(nearby.first - x) < Util.readRDouble(context.getResources(), R.dimen.minGraphDotDist)) {
            curve.remove(nearby.first);
        }
    }

    /**
     * Removes the last created point when the user moved their finger.
     */
    private void curveRemoveLast() {
        if (lastTouch != null) {
            curve.remove(lastTouch.first);
            lastTouch = null;
        }
    }

    /**
     * Places a point on the curve.
     * @param x
     * @param y
     */
    private void curvePlacePoint(double x, double y) {
        // snap to edges
        if (curve.first().first != 0) {
            x = 0;
        } else if (curve.last().first != 1) {
            x = 1;
        // don't get too close to other points
        } else {
            Pair<Double, Double> closestPt = curve.closest(x);
            double minDist = Util.readRDouble(context.getResources(), R.dimen.minGraphDotDist);
            if (Math.abs(x - closestPt.first) < minDist) {
                x = closestPt.first + Math.signum(x - closestPt.first) * minDist;
            }
        }
        curve.put(x, y);
    }

    /**
     * Starts a touch on the curve editor.
     * @param x
     * @param y
     */
    public void curveStartTouch(double x, double y) {
        x = curveRegulateCoord(x);
        y = curveRegulateCoord(y);
        curveRemoveNearby(x, y);
        curvePlacePoint(x, y);
        lastTouch = new Pair(x, y);
        notifyChange();
    }

    /**
     * Continues a touch on the curve editor.
     * @param x
     * @param y
     */
    public void curveContinueTouch(double x, double y) {
        x = curveRegulateCoord(x);
        y = curveRegulateCoord(y);
        curveRemoveLast();
        curvePlacePoint(x, y);
        lastTouch = new Pair(x, y);
        notifyChange();
    }

    /**
     * Stops a touch on the curve editor.
     * @param x
     * @param y
     */
    public void curveStopTouch(double x, double y) {
        x = curveRegulateCoord(x);
        y = curveRegulateCoord(y);
        curveRemoveLast();
        curvePlacePoint(x, y);
        lastTouch = null;
        // store curve
        lightUpdater.setAdjustmentCurve(curve);
        Util.storeString(context, "curve", curve.toString());
        notifyChange();
    }

    /**
     * Notifies any observers that the system has changed.
     */
    private void notifyChange() {
        this.setChanged();
        notifyObservers();
    }

}
