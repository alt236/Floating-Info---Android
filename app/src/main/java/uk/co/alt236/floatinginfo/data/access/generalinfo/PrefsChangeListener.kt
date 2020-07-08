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
package uk.co.alt236.floatinginfo.data.access.generalinfo

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.preference.PreferenceManager
import uk.co.alt236.floatinginfo.R
import uk.co.alt236.floatinginfo.overlay.OverlayManager

/*package*/
internal class PrefsChangeListener(private val mContext: Context,
                                   private val mOverlayManager: OverlayManager) : OnSharedPreferenceChangeListener {

    private val mPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            mContext.getString(R.string.pref_key_bg_opacity) -> {
                mOverlayManager.updateBackground()
            }
            mContext.getString(R.string.pref_key_text_alpha) -> {
                mOverlayManager.updateTextColor()
            }
            mContext.getString(R.string.pref_key_text_size) -> {
                mOverlayManager.updateTextSize()
            }
            mContext.getString(R.string.pref_key_text_color_red) -> {
                mOverlayManager.updateTextColor()
            }
            mContext.getString(R.string.pref_key_text_color_green) -> {
                mOverlayManager.updateTextColor()
            }
            mContext.getString(R.string.pref_key_text_color_blue) -> {
                mOverlayManager.updateTextColor()
            }
            mContext.getString(R.string.pref_key_screen_position) -> {
                mOverlayManager.updateAlignment()
            }
        }
    }

    fun register() {
        mPrefs.registerOnSharedPreferenceChangeListener(this)
    }

    fun unRegister() {
        mPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

}