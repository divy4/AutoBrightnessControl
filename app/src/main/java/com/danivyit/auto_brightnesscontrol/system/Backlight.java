package com.danivyit.auto_brightnesscontrol.system;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.danivyit.auto_brightnesscontrol.Util;

public class Backlight {

    private Context context;
    private double transitionTime;
    private int currBrighteness;

    /**
     * Creates a new Backlight.
     * @param applicationContext The application context of the app.
     * @param transitionTime The amount of time the backlight should spend transitioning between brightness values.
     */
    public Backlight(Context applicationContext, double transitionTime) {
        this.context = applicationContext;
        this.transitionTime = transitionTime;
        this.currBrighteness = 0;
        // check permissions
        if (!Settings.System.canWrite(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            context.startActivity(intent);
        }
        // nightlight settings
        try {
            Intent intent = new Intent(Settings.ACTION_NIGHT_DISPLAY_SETTINGS);
            context.startActivity(intent);
        } catch(Exception e) {
            Log.i("App","Night display not supported.");
        }
    }

    /**
     * Converts a integer brightness to a double.
     * @param raw An integer in [0, 255].
     * @return A double in [0, 1].
     */
    private double rawToDouble(int raw) {
        return Util.mapRange(raw, 0, 255, 0, 1);
    }

    /**
     * Converts a double to an integer brightness.
     * @param d A double in [0, 1].
     * @return An integer in [0, 255].
     */
    private int doubleToRaw(double d) {
        return (int) Util.mapRange(d, 0, 1, 0, 255);
    }

    /**
     * Transitions from the current brightness to newBrightness.
     * @param brightness The brightness value in [0,1] to transition to.
     */
    public void transitionTo(double brightness) {
        double startBrightness = rawToDouble(currBrighteness);
        // calculate times
        long startTime = System.currentTimeMillis();
        long currTime = startTime;
        long endTime = (long) (startTime + transitionTime * 1000);
        // loop until end time
        while (currTime < endTime) {
            // map time to brightness
            double curr = Util.mapRange(currTime, startTime, endTime, startBrightness, brightness);
            setBrightness(curr);
            SystemClock.sleep(16);
            currTime = System.currentTimeMillis();
        }
        // set final value
        setBrightness(brightness);
    }

    /**
     * Sets the screen brightness without transition.
     * @param brightness A number in [0,1] indicating what percentage the backlight should be set to.
     */
    public void setBrightness(double brightness) {
        // map to [0, 255]
        int intBrightness = doubleToRaw(brightness);
        // update only if brightness is different than before and we have permission
        if (Settings.System.canWrite(context)) {
            ContentResolver resolver = context.getContentResolver();
            // try to set brightness
            try {
                Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, intBrightness);
                // store integer brightness
                currBrighteness = intBrightness;
            } catch (Exception e) {
                Log.e("Error", "Unable to set screen brightness:" + e.toString());
            }
        }
    }

    /**
     * Sets the transition time.
     * @param time
     */
    public void setTransitionTime(double time) {
        transitionTime = time;
    }

}
