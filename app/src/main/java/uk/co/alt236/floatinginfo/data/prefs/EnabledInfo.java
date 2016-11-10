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
