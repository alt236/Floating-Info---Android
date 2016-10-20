package uk.co.alt236.floatinginfo.permissions;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class OverlayPermissionChecker implements PermissionChecker {
    private final Context mContext;

    public OverlayPermissionChecker(final Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public boolean isNeeded() {
        final boolean retVal;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = !Settings.canDrawOverlays(mContext);
        } else {
            retVal = false;
        }

        return retVal;
    }
}
