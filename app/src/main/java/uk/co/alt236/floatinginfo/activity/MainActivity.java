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
package uk.co.alt236.floatinginfo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.service.FloatingInfoService;

public class MainActivity extends BasePreferenceActivity {
    private SharedPreferences mPrefs;

    private static void setupOpenSourceInfoPreference(final Activity activity, final Preference preference) {
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                final FragmentManager fm = activity.getFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                final Fragment prev = fm.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                new OpenSourceLicensesDialog().show(ft, "dialog");
                return true;
            }
        });
    }

    private static void setupVersionPref(final Activity activity, final Preference preference) {
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                final FragmentManager fm = activity.getFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                final Fragment prev = fm.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                new AboutDialog().show(ft, "dialog");
                return true;
            }
        });
    }

    @Override
    public void onBuildHeaders(final List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        final ActionBar bar = getActionBar();
        final ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        lp.rightMargin = getResources().getDimensionPixelSize(R.dimen.main_switch_margin_right);
        bar.setCustomView(mainSwitch, lp);
        bar.setDisplayShowCustomEnabled(true);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!mPrefs.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false)) {
            PreferenceManager.setDefaultValues(this, R.xml.pref_appearance, true);

            final SharedPreferences.Editor edit = mPrefs.edit();
            edit.putBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, true);
            edit.apply();
        }

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
        fakeHeader.setTitle(R.string.appearance);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_appearance);

        // Add 'text color' preferences.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.text_color);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_textcolor);

        // Add 'info' preferences.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.information);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_info);

        // Add others
        setupOpenSourceInfoPreference(this, findPreference(getString(R.string.pref_info_open_source)));
        setupVersionPref(this, findPreference(getString(R.string.pref_version)));

    }

    public static class AboutDialog extends DialogFragment {

        public AboutDialog() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View content = inflater.inflate(R.layout.dialog_about, null, false);
            final TextView version = (TextView) content.findViewById(R.id.version);

            try {
                final String name = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
                version.setText(getString(R.string.version) + " " + name);
            } catch (final PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setView(content)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, final int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
        }
    }

    public static class AppearancePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_appearance);
        }
    }

    public static class TextColorPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_textcolor);
        }
    }

    public static class InfoPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_info);
            setupOpenSourceInfoPreference(getActivity(), findPreference(getString(R.string.pref_info_open_source)));
            setupVersionPref(getActivity(), findPreference(getString(R.string.pref_version)));
        }
    }

    public static class OpenSourceLicensesDialog extends DialogFragment {

        public OpenSourceLicensesDialog() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            final WebView webView = new WebView(getActivity());
            webView.loadUrl("file:///android_asset/licenses.html");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.open_source_licences)
                    .setView(webView)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, final int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
        }
    }
}
