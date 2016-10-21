package uk.co.alt236.floatinginfo.ui.activity.main;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import uk.co.alt236.floatinginfo.R;

/**
 *
 */
public class EnabledInfoPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_enabled_info);
    }
}
