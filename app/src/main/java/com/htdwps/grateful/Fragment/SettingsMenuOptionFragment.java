package com.htdwps.grateful.Fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 7/11/18.
 */
public class SettingsMenuOptionFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        // Generate a Preference Screen from this xml file.
        addPreferencesFromResource(R.xml.preference_settings);

    }
}
