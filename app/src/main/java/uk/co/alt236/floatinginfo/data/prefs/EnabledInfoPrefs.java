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

package uk.co.alt236.floatinginfo.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import uk.co.alt236.floatinginfo.R;

public class EnabledInfoPrefs {
    private final SharedPreferences mSharedPreferences;
    private final Resources mResources;

    public EnabledInfoPrefs(final Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mResources = context.getResources();
    }

    public boolean isNetInfoEnabled() {
        final boolean defValue = mResources.getBoolean(R.bool.default_enabled_info_value);
        return getBoolean(R.string.pref_key_show_net_info, defValue);
    }

    public boolean isIpInfoEnabled() {
        final boolean defValue = mResources.getBoolean(R.bool.default_enabled_info_value);
        return getBoolean(R.string.pref_key_show_ip_info, defValue);
    }

    public boolean isCpuInfoEnabled() {
        final boolean defValue = mResources.getBoolean(R.bool.default_enabled_info_value);
        return getBoolean(R.string.pref_key_show_cpu_info, defValue);
    }

    public boolean isMemoryInfoEnabled() {
        final boolean defValue = mResources.getBoolean(R.bool.default_enabled_info_value);
        return getBoolean(R.string.pref_key_show_memory_info, defValue);
    }

    public boolean showZeroMemoryItems() {
        final boolean defValue = mResources.getBoolean(R.bool.default_enabled_info_value);
        return getBoolean(R.string.pref_key_show_zero_memory_items, defValue);
    }

    private boolean getBoolean(@StringRes final int resId, final boolean defVal) {
        final String key = mResources.getString(resId);
        return mSharedPreferences.getBoolean(key, defVal);
    }

}
