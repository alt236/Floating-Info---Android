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
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                replace(activity.getFragmentManager(), new OpenSourceLicensesDialog());
                return true;
            }
        });
    }

    public static void setupVersionPref(final Activity activity, final Preference preference) {
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                replace(activity.getFragmentManager(), new AboutDialog());
                return true;
            }
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
