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
import android.graphics.Color
import android.preference.PreferenceManager
import uk.co.alt236.floatinginfo.common.R
import uk.co.alt236.floatinginfo.common.prefs.Alignment.Companion.fromString

class OverlayPrefs(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val resources: Resources = context.resources

    val textColor: Int
        get() {
            val alpha = prefs.getInt(
                    resources.getString(R.string.pref_key_text_alpha),
                    resources.getInteger(R.integer.default_text_alpha))
            val red = prefs.getInt(
                    resources.getString(R.string.pref_key_text_color_red),
                    resources.getInteger(R.integer.default_text_red))
            val green = prefs.getInt(
                    resources.getString(R.string.pref_key_text_color_green),
                    resources.getInteger(R.integer.default_text_green))
            val blue = prefs.getInt(
                    resources.getString(R.string.pref_key_text_color_blue),
                    resources.getInteger(R.integer.default_text_blue))
            return Color.argb(alpha, red, green, blue)
        }

    val backgroundColor: Int
        get() {
            val v = prefs.getInt(
                    resources.getString(R.string.pref_key_bg_opacity),
                    resources.getInteger(R.integer.background_opacity_default))
            val level = 0
            val retVal: Int
            retVal = if (v > 0) {
                val a = (v.toFloat() / 100f * 255).toInt()
                Color.argb(a, level, level, level)
            } else {
                0
            }
            return retVal
        }

    val textSize: Float
        get() {
            val baseValue = resources.getDimension(R.dimen.text_size_base)
            val prefsValue = prefs.getInt(resources.getString(R.string.pref_key_text_size), 0).toFloat()
            return baseValue + prefsValue
        }

    val alignment: Alignment
        get() {
            val key = resources.getString(R.string.pref_key_screen_position)
            val value = prefs.getString(key, null)
            val alignment = fromString(value)
            return alignment ?: Alignment.TOP_LEFT
        }

}