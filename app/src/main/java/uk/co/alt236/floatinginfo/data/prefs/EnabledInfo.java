package uk.co.alt236.floatinginfo.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import uk.co.alt236.floatinginfo.R;

public class EnabledInfo {
    private final SharedPreferences mSharedPreferences;
    private final Resources mResources;

    public EnabledInfo(final Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mResources = context.getResources();
    }

    public boolean isNetInfoEnabled() {
        return getBoolean(R.string.pref_key_show_net_info, false);
    }

    public boolean isCpuInfoEnabled() {
        return getBoolean(R.string.pref_key_show_cpu_info, false);
    }

    public boolean isMemoryInfoEnabled() {
        return getBoolean(R.string.pref_key_show_memory_info, false);
    }

    private boolean getBoolean(@StringRes final int resId, final boolean defVal) {
        final String key = mResources.getString(resId);
        return mSharedPreferences.getBoolean(key, defVal);
    }

}
