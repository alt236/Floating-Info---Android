/*
 * Copyright 2016 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.alt236.floatinginfo.ui.activity.main;

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
        PrefsUtils.setupOpenSourceInfoPreference(getActivity(), findPreference(getString(R.string.pref_key_info_open_source)));
        PrefsUtils.setupVersionPref(getActivity(), findPreference(getString(R.string.pref_key_version)));
    }
}
