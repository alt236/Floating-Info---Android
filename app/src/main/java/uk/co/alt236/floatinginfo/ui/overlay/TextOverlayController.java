package uk.co.alt236.floatinginfo.ui.overlay;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.data.prefs.OverlayPrefs;

/*package*/ class TextOverlayController {

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
        mTextView.setBackgroundColor(color);
    }

    public void updateTextColor() {
        final int color = mPrefs.getTextColor();
        mTextView.setTextColor(color);
        mTextView.setShadowLayer(1.5f, -1, 1, Color.DKGRAY);
    }

    public void updateTextSize() {
        final int size = mPrefs.getTextSize();
        mTextView.setTextSize(size);
    }

    public View getView() {
        return mView;
    }


    public void updateAlignment() {
        final OverlayPrefs.Alignment alignment = mPrefs.getAlignment();

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
