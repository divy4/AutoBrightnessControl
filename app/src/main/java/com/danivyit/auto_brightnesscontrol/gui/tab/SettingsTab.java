package com.danivyit.auto_brightnesscontrol.gui.tab;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.Util;

public class SettingsTab extends com.danivyit.auto_brightnesscontrol.gui.tab.Tab {

    Switch enableSwitch;
    CompoundButton.OnCheckedChangeListener enableListener;
    SeekBar freqBar;
    SeekBar.OnSeekBarChangeListener freqListener;
    TextView freqDisplay;

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
        // get components
        enableSwitch = view.findViewById(R.id.enableControl);
        freqBar = view.findViewById(R.id.updateFreqValue);
        freqDisplay = view.findViewById(R.id.updateFreqDisplay);
        // setup listeners
        if (enableListener != null) {
            enableSwitch.setOnCheckedChangeListener(enableListener);
        }
        if (freqListener != null) {
            freqBar.setOnSeekBarChangeListener(freqListener);
        }
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
     * Adds a listener for when the system is enabled.
     * @param listener
     */
    public void addEnableListener(CompoundButton.OnCheckedChangeListener listener) {
        enableListener = listener;
        if (enableSwitch != null) {
            enableSwitch.setOnCheckedChangeListener(listener);
        }
    }

    /**
     * Adds a listener for when the update frequency is changed.
     * @param listener
     */
    public void addUpdateFreqListener(SeekBar.OnSeekBarChangeListener listener) {
        freqListener = listener;
        if (freqBar != null) {
            freqBar.setOnSeekBarChangeListener(freqListener);
        }
    }

    /**
     * Updates the view.
     * @param updateFreq
     */
    public void update(double updateFreq) {
        if (freqDisplay != null) {
            int precision = Util.readRInt(getResources(), R.integer.seekBarPrecision);
            String str = Util.formatDouble(updateFreq, precision);
            freqDisplay.setText(str);
        }
    }
}
