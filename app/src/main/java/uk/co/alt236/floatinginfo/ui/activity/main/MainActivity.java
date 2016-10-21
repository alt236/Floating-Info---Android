/*******************************************************************************
 * Copyright 2014 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uk.co.alt236.floatinginfo.ui.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.permissions.PermissionCheckerWrapper;
import uk.co.alt236.floatinginfo.service.FloatingInfoService;
import uk.co.alt236.floatinginfo.ui.activity.base.TabletAwarePreferenceActivity;
import uk.co.alt236.floatinginfo.ui.activity.onboarding.OnBoardingActivity;

public class MainActivity extends TabletAwarePreferenceActivity {

    @Override
    public void onBuildHeaders(final List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionbar();
        setDefaultPrefsValues();
    }

    private void setDefaultPrefsValues() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false)) {
            PreferenceManager.setDefaultValues(this, R.xml.pref_appearance, true);

            final SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, true);
            edit.apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (new PermissionCheckerWrapper(this).needToAsk()) {
            final Intent intent = new Intent(this, OnBoardingActivity.class);
            startActivity(intent);
        }

    }

    private void setupActionbar() {
        final ViewGroup root = (ViewGroup) findViewById(android.R.id.list).getParent().getParent().getParent();
        final Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(toolbar, 0); // insert at top
        setSupportActionBar(toolbar);

        final Switch mainSwitch = new Switch(this);

        mainSwitch.setChecked(FloatingInfoService.isRunning());
        mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                final Intent intent = new Intent(MainActivity.this, FloatingInfoService.class);
                if (b) {
                    if (!FloatingInfoService.isRunning()) {
                        startService(intent);
                    }
                } else {
                    stopService(intent);
                }
            }
        });

        final ActionBar bar = getSupportActionBar();
        final ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        lp.rightMargin = getResources().getDimensionPixelSize(R.dimen.main_switch_margin_right);
        bar.setCustomView(mainSwitch, lp);
        bar.setDisplayShowCustomEnabled(true);

    }


    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    @SuppressWarnings("deprecation")
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        addPreferencesFromResource(R.xml.pref_blank);

        // Add 'filters' preferences.
        PreferenceCategory fakeHeader = new PreferenceCategory(this);

        // Add 'appearance' preferences.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.prefs_header_appearance);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_appearance);

        // Add 'text color' preferences.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.prefs_header_text_color);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_textcolor);

        // Add 'enabled info' preferences.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.prefs_header_info_to_display);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_enabled_info);

        // Add 'info' preferences.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.prefs_header_information);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_info);

        // Add others
        PrefsUtils.setupOpenSourceInfoPreference(this, findPreference(getString(R.string.pref_key_info_open_source)));
        PrefsUtils.setupVersionPref(this, findPreference(getString(R.string.pref_key_version)));
    }

}
