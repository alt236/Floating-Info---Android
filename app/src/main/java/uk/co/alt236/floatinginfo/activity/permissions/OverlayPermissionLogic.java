package uk.co.alt236.floatinginfo.activity.permissions;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import uk.co.alt236.floatinginfo.BuildConfig;

/*package*/ class OverlayPermissionLogic {
    public final static int REQUEST_CODE = -1010101;

    private final Activity mActivity;

    public OverlayPermissionLogic(final Activity activity) {
        mActivity = activity;
    }

    public boolean needToAsk() {
        final boolean retVal;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = !Settings.canDrawOverlays(mActivity);
        } else {
            retVal = false;
        }

        return retVal;
    }


    public void ask() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** check if we already  have permission to draw over other apps */
            if (needToAsk()) {
                /** if not construct intent to request permission */
                final Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                /** request permission via start activity for result */
                mActivity.startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

}
