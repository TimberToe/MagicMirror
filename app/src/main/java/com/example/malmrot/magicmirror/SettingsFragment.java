package com.example.malmrot.magicmirror;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by malmrot on 2017-09-14.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}