package com.danivyit.auto_brightnesscontrol.system;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.danivyit.auto_brightnesscontrol.Util;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

public class BacklightUpdater extends RepeatingThread implements SensorEventListener {

    private Backlight backlight;
    private Curve adjustmentCurve;
    private SensorManager manager;
    private Sensor lightSensor;
    private double ambientLight;

    /**
     * Creates a new BacklightUpdater.
     * @param applicationContext The application context.
     * @param delay The amount of time (in seconds) to sleep before the backlight is updated again.
     * @param transitionTime The amount of time the updater uses to transition between brightness levels. Should be less than delay.
     */
    public BacklightUpdater(Context applicationContext, double delay, double transitionTime) {
        super(delay);
        this.backlight = new Backlight(applicationContext, transitionTime);
        this.adjustmentCurve = null;
        // get light sensor
        this.manager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
        this.lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // register light sensor
        this.manager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        this.ambientLight = 0;
    }

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
        this.adjustmentCurve = curve;
    }

    /**
     * Runs repeatably until queueStop() is called.
     */
    @Override
    protected void runTask() {
        // map ambient light to brightness (linear)
        double brightness = Util.mapRange(ambientLight, 0, lightSensor.getMaximumRange(), 0, 1);
        // use curve to adjust value
        if (adjustmentCurve != null) {
            brightness = adjustmentCurve.predict(brightness);
        }
        backlight.transitionTo(brightness);
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
}
