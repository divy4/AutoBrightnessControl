package com.danivyit.auto_brightnesscontrol.control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.danivyit.auto_brightnesscontrol.gui.AutoBrightnessGUI;
import com.danivyit.auto_brightnesscontrol.system.BackgroundService;

public class Controller {

    private AutoBrightnessGUI gui;
    private Intent serviceIntent;

    /**
     * Creates a new Controller.
     * @param gui
     */
    public Controller(AutoBrightnessGUI gui) {
        this.gui = gui;
        startService();
    }

    /**
     * Starts the background service.
     */
    public void startService() {
        serviceIntent = new Intent(gui.getBaseContext(), BackgroundService.class);
        gui.startService(serviceIntent);

    }

    /**
     * Stops the background service.
     */
    public void stopService() {
        gui.stopService(serviceIntent);
        serviceIntent = null;
    }
}
