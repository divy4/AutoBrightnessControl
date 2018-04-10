package com.danivyit.auto_brightnesscontrol.gui.tab;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.gui.SeekBarDisplayBridge;
import com.danivyit.auto_brightnesscontrol.Util;

public class SettingsTab extends com.danivyit.auto_brightnesscontrol.gui.tab.Tab {

    /**
     * Called when the Fragment is created.
     * @param inflater See Android documentation.
     * @param container See Android documentation.
     * @param savedInstanceState See Android documentation.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Context context = getActivity().getApplicationContext();
        Resources resources = context.getResources();
        // get components
        Switch enableControl = view.findViewById(R.id.enableControl);
        Switch enableGraph = view.findViewById(R.id.enableGraph);
        // setup seek bars
        initBrightnessSeekBar(view, R.id.staticBrightnessValue, R.id.staticBrightnessDisplay);
        initFreqSeekBar(      view, R.id.updateFreqValue,       R.id.updateFreqDisplay);
        initTransTimeSeekBar( view, R.id.transitionTimeValue,   R.id.transitionTimeDisplay);
        return view;
    }

    /**
     * Gets the id of the tab's layout.
     * @return An int
     */
    protected int getLayoutId() {
        return R.layout.settings_tab;
    }

    /**
     * Initializes a slider with its display.
     * @param view The view of the tab.
     * @param barId The id of the seek bar.
     * @param displayId The id of the display.
     * @param minId The id of the minimum value the SeekBar can take.
     * @param maxId The id of the maximum value the SeekBar can take.
     * @param precision The number of decimal points to display.
     */
    private void initSeekBar(View view, int barId, int displayId, int minId, int maxId, int precision) {
        Context context = getActivity().getApplicationContext();
        Resources resources = context.getResources();
        // get objects
        TextView display = view.findViewById(displayId);
        SeekBar bar = view.findViewById(barId);
        // get values
        double min = Util.readRDouble(resources, minId);
        double max = Util.readRDouble(resources, maxId);
        // set listener
        SeekBarDisplayBridge bridge = new SeekBarDisplayBridge(bar, display, min, max, precision);
        bar.setOnSeekBarChangeListener(bridge);
    }

    /**
     * Initializes the brightness SeekBar and display.
     * @param view The view of the Fragment.
     * @param barId The id of the bar.
     * @param displayId The id of the display.
     */
    private void initBrightnessSeekBar(View view, int barId, int displayId) {
        initSeekBar(view, barId, displayId, R.dimen.minBrightness, R.dimen.maxBrightness,0);
    }

    /**
     * Initializes the update frequency SeekBar and display.
     * @param view The view of the Fragment.
     * @param barId The id of the bar.
     * @param displayId The id of the display.
     */
    private void initFreqSeekBar(View view, int barId, int displayId) {
        initSeekBar(view, barId, displayId, R.dimen.minUpdateFreq, R.dimen.maxUpdateFreq, 1);
    }

    /**
     * Initializes the transition time SeekBar and display.
     * @param view The view of the Fragment.
     * @param barId The id of the bar.
     * @param displayId The id of the display.
     */
    private void initTransTimeSeekBar(View view, int barId, int displayId) {
        initSeekBar(view, barId, displayId, R.dimen.minTransition, R.dimen.maxTransition, 1);
    }

}
