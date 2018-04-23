package com.danivyit.auto_brightnesscontrol.gui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.control.Controller;
import com.danivyit.auto_brightnesscontrol.gui.tab.GraphTab;
import com.danivyit.auto_brightnesscontrol.gui.tab.HelpTab;
import com.danivyit.auto_brightnesscontrol.gui.tab.ProfilesTab;
import com.danivyit.auto_brightnesscontrol.gui.tab.SettingsTab;

public class AutoBrightnessGUI extends AppCompatActivity {

    /**
     * A listener that handles when a tab is selected.
     */
    public class TabSelectListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.settingsTabButton:
                    setTab(settingsTab);
                    break;
                case R.id.graphTabButton:
                    setTab(graphTab);
                    break;
                case R.id.profilesTabButton:
                    setTab(profilesTab);
                    break;
                case R.id.helpTabButton:
                    setTab(helpTab);
                    break;
            }
            return true;
        }
    }

    private GraphTab graphTab;
    private ProfilesTab profilesTab;
    private SettingsTab settingsTab;
    private HelpTab helpTab;
    private Controller controller;

    /**
     * Called when the activity is created.
     * @param savedInstanceState See Android documentation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        // create tabs
        graphTab = new GraphTab();
        profilesTab = new ProfilesTab();
        settingsTab = new SettingsTab();
        helpTab = new HelpTab();
        setTab(settingsTab);
        // change tab when one is selected
        BottomNavigationView tabSelector = findViewById(R.id.tabSelector);
        tabSelector.setOnNavigationItemSelectedListener(new TabSelectListener());
        // controller
        controller = new Controller(this);
    }

    /**
     * Called when the activity is being destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }

    /**
     * Sets the current tab given a Fragment.
     * @param tab A Fragment that will be the active tab.
     */
    private void setTab(Fragment tab) {
        // get manager and transaction
        if (tab != null) {
            FragmentManager manager = getFragmentManager();
            if (manager != null) {
                FragmentTransaction trans = manager.beginTransaction();
                if (trans != null) {
                    // swap layout with tab
                    trans.replace(R.id.tabLayout, tab);
                    trans.commit();
                }
            }
        }
    }
}
