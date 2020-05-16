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

package uk.co.alt236.floatinginfo.data.access.generalinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.overlay.OverlayManager;

/*package*/ class PrefsChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final OverlayManager mOverlayManager;
    private final Context mContext;
    private final SharedPreferences mPrefs;

    public PrefsChangeListener(final Context context,
                               final OverlayManager overlayManager) {
        mOverlayManager = overlayManager;
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(mContext.getString(R.string.pref_key_bg_opacity))) {
            mOverlayManager.updateBackground();
        } else if (key.equals(mContext.getString(R.string.pref_key_text_alpha))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(mContext.getString(R.string.pref_key_text_size))) {
            mOverlayManager.updateTextSize();
        } else if (key.equals(mContext.getString(R.string.pref_key_text_color_red))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(mContext.getString(R.string.pref_key_text_color_green))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(mContext.getString(R.string.pref_key_text_color_blue))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(mContext.getString(R.string.pref_key_screen_position))) {
            mOverlayManager.updateAlignment();
        }
    }

    public void register() {
        mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    public void unRegister() {
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }
}
