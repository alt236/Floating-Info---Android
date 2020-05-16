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

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.preference.Preference;

import uk.co.alt236.floatinginfo.ui.activity.main.dialogs.AboutDialog;
import uk.co.alt236.floatinginfo.ui.activity.main.dialogs.OpenSourceLicensesDialog;

/*package*/ final class PrefsUtils {

    private static final String DIALOG_TAG = "dialog";

    private PrefsUtils() {
        // NOOP
    }

    public static void setupOpenSourceInfoPreference(final Activity activity, final Preference preference) {
        preference.setOnPreferenceClickListener(preference1 -> {
            replace(activity.getFragmentManager(), new OpenSourceLicensesDialog());
            return true;
        });
    }

    public static void setupVersionPref(final Activity activity, final Preference preference) {
        preference.setOnPreferenceClickListener(preference1 -> {
            replace(activity.getFragmentManager(), new AboutDialog());
            return true;
        });
    }


    private static void replace(final FragmentManager fm, final DialogFragment dialog) {
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment prev = fm.findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialog.show(ft, DIALOG_TAG);
    }
}
