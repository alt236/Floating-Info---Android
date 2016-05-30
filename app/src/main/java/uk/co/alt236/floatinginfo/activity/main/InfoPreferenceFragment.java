package uk.co.alt236.floatinginfo.activity.main;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import uk.co.alt236.floatinginfo.R;

/**
 *
 */
public class InfoPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_info);
        PrefsUtils.setupOpenSourceInfoPreference(getActivity(), findPreference(getString(R.string.pref_info_open_source)));
        PrefsUtils.setupVersionPref(getActivity(), findPreference(getString(R.string.pref_version)));
    }
}
