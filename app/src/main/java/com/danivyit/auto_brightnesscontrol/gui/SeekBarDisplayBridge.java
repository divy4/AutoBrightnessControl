package com.danivyit.auto_brightnesscontrol.gui;

import android.widget.SeekBar;
import android.widget.TextView;

import com.danivyit.auto_brightnesscontrol.Util;

/**
 * A class that causes a TextView to display the value of a SeekBar.
 */
public class SeekBarDisplayBridge implements SeekBar.OnSeekBarChangeListener {

    private SeekBar bar;
    private TextView display;
    private double min;
    private double max;
    private int precision;

    /**
     * Creates a new SeekBarDisplayBridge.
     * @param bar A SeekBar.
     * @param display The TextView to display the value of the bar.
     * @param min The minimum value of the bar.
     * @param max The maximum value of the bar.
     * @param precision The number of decimal points the display should show.
     */
    public SeekBarDisplayBridge(SeekBar bar, TextView display, double min, double max, int precision) {
        this.bar = bar;
        this.display = display;
        this.min = min;
        this.max = max;
        this.precision = precision;
        updateDisplay();
    }

    /**
     * Gets the current value of the bar. This should be used instead of SeekBar.getProgress() because the return value is mapped in [min, max].
     * @return A double
     */
    public double getValue() {
        int raw = bar.getProgress();
        return Util.mapRange(raw, 0, bar.getMax(), min, max);
    }

    /**
     * Updates the display to match the bar.
     */
    protected void updateDisplay() {
        String valStr = Util.formatDouble(getValue(), precision);
        display.setText(valStr);
    }

    /**
     * Called when the value updates.
     * @param seekBar The bar.
     * @param seekVal The value of the bar.
     * @param fromUser True if the user was responsible for the change.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int seekVal, boolean fromUser) {
        updateDisplay();
    }

    /**
     * Called when the user starts moving the bar.
     * @param seekBar The bar.
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    /**
     * Called when the user stops moving the bar.
     * @param seekBar The bar.
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

}
