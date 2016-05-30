package uk.co.alt236.floatinginfo.activity.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.preference.Preference;

/*package*/ final class PrefsUtils {
    private PrefsUtils() {
        // NOOP
    }

    public static void setupOpenSourceInfoPreference(final Activity activity, final Preference preference) {
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

    public static void setupVersionPref(final Activity activity, final Preference preference) {
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
}
