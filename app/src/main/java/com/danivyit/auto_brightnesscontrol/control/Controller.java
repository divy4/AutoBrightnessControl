package com.danivyit.auto_brightnesscontrol.control;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.Util;
import com.danivyit.auto_brightnesscontrol.gui.AutoBrightnessGUI;
import com.danivyit.auto_brightnesscontrol.gui.CurveView;
import com.danivyit.auto_brightnesscontrol.system.BacklightUpdater;
import com.danivyit.auto_brightnesscontrol.system.BrightnessSystem;
import com.danivyit.auto_brightnesscontrol.system.curve.BezierCurve;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

import java.util.Observable;

public class Controller implements java.util.Observer {

    /**
     * Listens for changes in whether or not the system should be enabled.
     */
    private class EnableListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                system.enable();
            } else {
                system.disable();
            }
        }
    }

    /**
     * Listens for when the update frequency should be updated.
     */
    private class UpdateFreqListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int seekVal, boolean fromUser) {
            double min = Util.readRDouble(gui.getResources(), R.dimen.minUpdateFreq);
            double max = Util.readRDouble(gui.getResources(), R.dimen.maxUpdateFreq);
            double freq = Util.mapRange(seekVal, 0, seekBar.getMax(), min, max);
            system.setUpdateFreq(freq);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    /**
     * Listens for touches on the curve editor.
     */
    private class CurveTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            CurveView curveView = (CurveView)view;
            double x = curveView.xPixelToPos(event.getX());
            double y = curveView.yPixelToPos(event.getY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    system.curveStartTouch(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    system.curveContinueTouch(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    system.curveStopTouch(x, y);
                    break;
            }
            return true;
        }
    }

    private AutoBrightnessGUI gui;
    private BrightnessSystem system;

    /**
     * Creates a new Controller.
     * @param gui
     */
    public Controller(AutoBrightnessGUI gui) {
        this.gui = gui;
        this.system = new BrightnessSystem(gui.getApplicationContext(), gui);
        // observers and listeners
        system.addObserver(this);
        gui.addEnableListener(new EnableListener());
        gui.addUpdateFreqListener(new UpdateFreqListener());
        gui.addCurveTouchListener(new CurveTouchListener());
    }

    /**
     * Called when the application is being destroyed.
     */
    public void onDestroy() {
        system.onDestroy();
    }

    /**
     * Called when the BrightnessSystem updates.
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        gui.update(system.getUpdateFreq(), system.getCurve());
    }
}
