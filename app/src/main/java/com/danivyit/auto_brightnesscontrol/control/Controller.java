package com.danivyit.auto_brightnesscontrol.control;

import com.danivyit.auto_brightnesscontrol.gui.AutoBrightnessGUI;
import com.danivyit.auto_brightnesscontrol.system.BacklightUpdater;
import com.danivyit.auto_brightnesscontrol.system.curve.BezierCurve;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

public class Controller {

    private AutoBrightnessGUI gui;
    private BacklightUpdater lightUpdater;

    /**
     * Creates a new Controller.
     * @param gui
     */
    public Controller(AutoBrightnessGUI gui) {
        this.gui = gui;
        // TEMP: create basic adjustment curve
        Curve curve = new BezierCurve(10);
        curve.put(0, 0.2);
        curve.put(0.9, 0.4);
        curve.put(1, 0.8);
        this.lightUpdater = new BacklightUpdater(0.5, gui.getApplicationContext());
        lightUpdater.setAdjustmentCurve(curve);
        lightUpdater.start();
    }

    /**
     * Called when the application is being destroyed.
     */
    public void onDestroy() {
        lightUpdater.queueStop();
    }

}
