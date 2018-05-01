package com.danivyit.auto_brightnesscontrol.system;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.danivyit.auto_brightnesscontrol.Util;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

import java.util.concurrent.atomic.AtomicReference;

public class BacklightUpdater extends RepeatingThread implements SensorEventListener {

    private AtomicReference<Double> baseDelay;
    private AtomicReference<Curve> adjustmentCurve;

    private Backlight backlight;

    private SensorManager manager;
    private Sensor lightSensor;
    private double ambientLight;
    private double lastBrightness;

    /**
     * Creates a new BacklightUpdater.
     * @param applicationContext The application context.
     * @param baseDelay The amount of time (in seconds) to sleep before the backlight is updated again.
     * @param transitionTime The amount of time the updater uses to transition between brightness levels. Should be less than delay.
     */
    public BacklightUpdater(Context applicationContext, double baseDelay, double transitionTime) {
        super(baseDelay);
        // other
        this.baseDelay = new AtomicReference(baseDelay);
        this.adjustmentCurve = new AtomicReference(null);
        // components
        this.backlight = new Backlight(applicationContext, transitionTime);
        // get light sensor
        this.manager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
        this.lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // register light sensor
        this.manager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        this.ambientLight = 0;
        this.lastBrightness = -1;
    }

    /**
     * Returns the delay between backlight updates.
     * @return
     */
    public double getDelay() {
        if (baseDelay != null) {
            return baseDelay.get();
        } else {
            return 0;
        }
    }

    /**
     * Sets the delay between backlight updates.
     * @param sec
     */
    @Override
    public void setDelay(double sec) {
        if (baseDelay != null) {
            baseDelay.set(sec);
        }
    }

    /**
     * Queues the thread to stop.
     */
    @Override
    public void queueStop() {
        super.queueStop();
        manager.unregisterListener(this);
    }

    /**
     * Sets a curve whose predict() function will map the value of the ambient light to the brightness
     * @param curve
     */
    public void setAdjustmentCurve(Curve curve) {
        adjustmentCurve.set(curve);
    }

    /**
     * Runs repeatably until queueStop() is called.
     */
    @Override
    protected void runTask() {
        // map ambient light to brightness (linear)
        double brightness = getAmbientLight();
        // use curve to adjust value
        Curve curve = adjustmentCurve.get();
        if (curve != null) {
            brightness = curve.predict(brightness);
        }
        backlight.transitionTo(brightness);
        // compute next delay
        double delay = baseDelay.get();
        if (brightness != lastBrightness) {
            double f = 0.01 / Math.abs(brightness - lastBrightness);
            delay *= Math.max(0.1, Math.min(1, f));
        }
        lastBrightness = 0.3 * lastBrightness + 0.7 * brightness;
        super.setDelay(delay);
    }

    /**
     * Called when the light sensor changes values.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // geometric rolling average
        ambientLight = (ambientLight + event.values[0]) * 0.5;
    }

    /**
     * Called when the accuracy of the light sensor changes.
     * @param sensor
     * @param i
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    /**
     * Returns the ambient light level.
     */
    public double getAmbientLight() {
        return Util.mapRange(ambientLight, 0, lightSensor.getMaximumRange(), 0, 1);
    }
}
