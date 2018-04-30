package com.danivyit.auto_brightnesscontrol.gui.tab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class Tab extends Fragment {

    /**
     * Gets the id of the tab's layout.
     * @return An int
     */
    protected abstract int getLayoutId();

    /**
     * Called when the Fragment is created.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(getLayoutId(), container, false);
    }
}
