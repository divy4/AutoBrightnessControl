package com.danivyit.auto_brightnesscontrol.gui.tab;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.gui.CurveView;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;

public class GraphTab extends com.danivyit.auto_brightnesscontrol.gui.tab.Tab {

    CurveView curveView;
    View.OnTouchListener touchListener;

    /**
     * Called when the Fragment is created.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        curveView = v.findViewById(R.id.curveView);
        if (touchListener != null) {
            curveView.setOnTouchListener(touchListener);
        }
        return v;
    }

    /**
     * Gets the id of the tab's layout.
     * @return An int
     */
    protected int getLayoutId() {
        return R.layout.graph_tab;
    }

    /**
     * Adds a listener for when the curve editor is touched.
     * @param listener
     */
    public void addCurveTouchListener(View.OnTouchListener listener) {
        touchListener = listener;
        if (curveView != null) {
            curveView.setOnTouchListener(listener);
        }
    }

    /**
     * Updates the view.
     * @param curve
     */
    public void update(Curve curve) {
        if (curveView != null) {
            curveView.update(curve);
        }
    }

}
