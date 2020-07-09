/*
 * Copyright 2020 Alexandros Schillings
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
package uk.co.alt236.floatinginfo.common.prefs

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import androidx.annotation.StringRes
import uk.co.alt236.floatinginfo.common.R

class EnabledInfoPrefs(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val resources: Resources = context.resources

    val isNetInfoEnabled: Boolean
        get() {
            val defValue = resources.getBoolean(R.bool.default_enabled_info_value)
            return getBoolean(R.string.pref_key_show_net_info, defValue)
        }

    val isIpInfoEnabled: Boolean
        get() {
            val defValue = resources.getBoolean(R.bool.default_enabled_info_value)
            return getBoolean(R.string.pref_key_show_ip_info, defValue)
        }

    val isCpuInfoEnabled: Boolean
        get() {
            val defValue = resources.getBoolean(R.bool.default_enabled_info_value)
            return getBoolean(R.string.pref_key_show_cpu_info, defValue)
        }

    val isLocaleInfoEnabled: Boolean
        get() {
            val defValue = resources.getBoolean(R.bool.default_enabled_info_value)
            return getBoolean(R.string.pref_key_show_locale_info, defValue)
        }

    val isMemoryInfoEnabled: Boolean
        get() {
            val defValue = resources.getBoolean(R.bool.default_enabled_info_value)
            return getBoolean(R.string.pref_key_show_memory_info, defValue)
        }

    val isBluetoothInfoEnabled: Boolean
        get() {
            val defValue = resources.getBoolean(R.bool.default_enabled_info_value)
            return getBoolean(R.string.pref_key_show_bluetooth_info, defValue)
        }

    fun showZeroMemoryItems(): Boolean {
        val defValue = resources.getBoolean(R.bool.default_enabled_info_value)
        return getBoolean(R.string.pref_key_show_zero_memory_items, defValue)
    }

    private fun getBoolean(@StringRes resId: Int, defVal: Boolean): Boolean {
        val key = resources.getString(resId)
        return prefs.getBoolean(key, defVal)
    }

}