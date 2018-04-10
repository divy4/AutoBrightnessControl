package com.danivyit.auto_brightnesscontrol.system;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundService extends IntentService {

    Backlight backlight;

    /**
     * Creates a new BackgroundService.
     */
    public BackgroundService() {
        super("Auto-Brightness Control Background");
    }

    /**
     * Called when the service is created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        backlight = new Backlight(getApplicationContext(), 1);
        backlight.transitionTo(1);
    }

    /**
     * Called when the service handles an event.
     * @param workIntent
     */
    @Override
    protected void onHandleIntent(Intent workIntent) {
        String input = workIntent.getDataString();
    }

}
