package com.danivyit.auto_brightnesscontrol.gui.tab;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.gui.CurveViewAdapter;

import java.util.List;
import java.util.Vector;

public class ProfilesTab extends com.danivyit.auto_brightnesscontrol.gui.tab.Tab {

    private RecyclerView view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> items;

    /**
     * Called when the Tab is created.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (RecyclerView) super.onCreateView(inflater, container, savedInstanceState);
        // Don't need to update size due to content changes
        view.setHasFixedSize(true);
        // manager
        layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        // data
        items = new Vector();
        items.add("asdf");
        items.add("asdf2");
        adapter = new CurveViewAdapter(items);
        view.setAdapter(adapter);
        return view;
    }

    /**
     * Gets the id of the tab's layout.
     * @return An int
     */
    protected int getLayoutId() {
        return R.layout.profiles_tab;
    }

}
