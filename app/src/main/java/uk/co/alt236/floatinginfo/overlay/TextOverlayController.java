package uk.co.alt236.floatinginfo.overlay;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import uk.co.alt236.floatinginfo.R;

/*package*/ class TextOverlayController {

    private final SharedPreferences mPrefs;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final TextView mTextView;

    public TextOverlayController(final Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mTextView = (TextView) generateView();
    }


    private View generateView() {
        return mInflater.inflate(R.layout.screen_overlay, null);
    }

    public void updateBackground() {
        final int v = mPrefs.getInt(mContext.getString(R.string.pref_bg_opacity), 0);
        final int level = 0;
        if (v > 0) {
            final int a = (int) ((float) v / 100f * 255);
            mTextView.setBackgroundColor(Color.argb(a, level, level, level));
        } else {
            mTextView.setBackground(null);
        }
    }

    public void updateTextColor() {
        final int alpha = mPrefs.getInt(
                mContext.getString(R.string.pref_text_opacity),
                mContext.getResources().getInteger(R.integer.default_text_opacity));
        final int red = mPrefs.getInt(
                mContext.getString(R.string.pref_text_color_red),
                mContext.getResources().getInteger(R.integer.default_text_red));
        final int green = mPrefs.getInt(
                mContext.getString(R.string.pref_text_color_green),
                mContext.getResources().getInteger(R.integer.default_text_green));
        final int blue = mPrefs.getInt(
                mContext.getString(R.string.pref_text_color_blue),
                mContext.getResources().getInteger(R.integer.default_text_blue));
        mTextView.setTextColor(Color.argb(alpha, red, green, blue));
        mTextView.setShadowLayer(1.5f, -1, 1, Color.DKGRAY);
    }

    public void updateTextSize() {
        final int sp = 6 + mPrefs.getInt(
                mContext.getString(R.string.pref_text_size),
                0);
        mTextView.setTextSize(sp);
    }

    public View getView() {
        return mTextView;
    }

    public void setText(final CharSequence charSequence) {
        mTextView.setText(charSequence);
    }
}
