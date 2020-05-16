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

package uk.co.alt236.floatinginfo.overlay;

import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import uk.co.alt236.floatinginfo.common.prefs.Alignment;
import uk.co.alt236.floatinginfo.common.prefs.OverlayPrefs;

/*package*/ class TextOverlayController {
    private static final String TAG = TextOverlayController.class.getSimpleName();

    private final OverlayPrefs mPrefs;
    private final LayoutInflater mInflater;
    private final View mView;
    private final TextView mTextView;

    public TextOverlayController(final LayoutInflater layoutInflater,
                                 final OverlayPrefs overlayPrefs) {
        mInflater = layoutInflater;
        mPrefs = overlayPrefs;
        mView = generateView();
        mTextView = (TextView) mView.findViewById(R.id.text);
    }

    private View generateView() {
        return mInflater.inflate(R.layout.screen_overlay, null);
    }

    public void updateBackground() {
        final int color = mPrefs.getBackgroundColor();
        Log.d(TAG, "Updating BgColor to : #" + Integer.toHexString(color));
        mTextView.setBackgroundColor(color);
    }

    public void updateTextColor() {
        final int color = mPrefs.getTextColor();
        Log.d(TAG, "Updating TextColor to : #" + Integer.toHexString(color));
        mTextView.setTextColor(color);
        mTextView.setShadowLayer(1.5f, -1, 1, Color.DKGRAY);
    }

    public void updateTextSize() {
        final float size = mPrefs.getTextSize();
        Log.d(TAG, "Updating TextSize to : " + size);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public View getView() {
        return mView;
    }


    public void updateAlignment() {
        final Alignment alignment = mPrefs.getAlignment();
        Log.d(TAG, "Updating View alignment to: " + alignment);

        final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        switch (alignment) {
            case TOP_LEFT:
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case TOP_CENTER:
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case TOP_RIGHT:
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case CENTER_LEFT:
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case CENTER_CENTER:
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
            case CENTER_RIGHT:
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            case BOTTOM_LEFT:
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case BOTTOM_CENTER:
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case BOTTOM_RIGHT:
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            default:
                throw new IllegalStateException("Unknown alignment: " + alignment);
        }

        mTextView.setLayoutParams(lp);
    }

    public void setText(final CharSequence charSequence) {
        mTextView.setText(charSequence);
    }
}
