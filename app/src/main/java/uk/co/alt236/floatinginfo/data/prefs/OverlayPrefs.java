package uk.co.alt236.floatinginfo.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;

import uk.co.alt236.floatinginfo.R;

public class OverlayPrefs {
    private final SharedPreferences mPrefs;
    private final Resources mResources;

    public OverlayPrefs(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mResources = context.getResources();
    }

    public int getTextColor() {
        final int alpha = mPrefs.getInt(
                mResources.getString(R.string.pref_key_text_opacity),
                mResources.getInteger(R.integer.default_text_opacity));
        final int red = mPrefs.getInt(
                mResources.getString(R.string.pref_key_text_color_red),
                mResources.getInteger(R.integer.default_text_red));
        final int green = mPrefs.getInt(
                mResources.getString(R.string.pref_key_text_color_green),
                mResources.getInteger(R.integer.default_text_green));
        final int blue = mPrefs.getInt(
                mResources.getString(R.string.pref_key_text_color_blue),
                mResources.getInteger(R.integer.default_text_blue));

        return Color.argb(alpha, red, green, blue);
    }

    public int getBackgroundColor() {
        final int v = mPrefs.getInt(mResources.getString(R.string.pref_key_bg_opacity), 0);
        final int level = 0;

        final int retVal;
        if (v > 0) {
            final int a = (int) ((float) v / 100f * 255);
            retVal = Color.argb(a, level, level, level);
        } else {
            retVal = 0;
        }

        return retVal;
    }

    public int getTextSize() {
        return 6 + mPrefs.getInt(
                mResources.getString(R.string.pref_key_text_size),
                0);
    }

    public Alignment getAlignment() {
        final String key = mResources.getString(R.string.pref_key_screen_position);
        final String value = mPrefs.getString(key, null);

        final Alignment alignment = Alignment.fromString(value);
        return alignment == null ? Alignment.TOP_LEFT : alignment;
    }

    public enum Alignment {
        TOP_LEFT("TOP_LEFT"),
        TOP_CENTER("TOP_CENTER"),
        TOP_RIGHT("TOP_CENTER"),
        CENTER_LEFT("CENTER_LEFT"),
        CENTER_CENTER("CENTER_CENTER"),
        CENTER_RIGHT("CENTER_RIGHT"),
        BOTTOM_LEFT("BOTTOM_LEFT"),
        BOTTOM_CENTER("BOTTOM_CENTER"),
        BOTTOM_RIGHT("BOTTOM_RIGHT");

        private final String key;

        Alignment(final String key) {
            this.key = key;
        }

        public static Alignment fromString(final String value) {
            Alignment retVal = null;

            for (final Alignment alignment : Alignment.values()) {
                if (alignment.key.equalsIgnoreCase(value)) {
                    retVal = alignment;
                    break;
                }
            }
            return retVal;
        }
    }
}
