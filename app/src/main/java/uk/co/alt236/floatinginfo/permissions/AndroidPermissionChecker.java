package uk.co.alt236.floatinginfo.permissions;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class AndroidPermissionChecker implements PermissionChecker {

    private final Context mContext;

    public AndroidPermissionChecker(final Context context) {
        mContext = context.getApplicationContext();
    }

    private boolean hasAllPermissions(@NonNull String[] permissions) {
        boolean hasAllPermissions = true;
        for (String perm : permissions) {
            hasAllPermissions &= ActivityCompat.checkSelfPermission(mContext, perm)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return hasAllPermissions;
    }

    @NonNull
    public String[] getRequiredPermissions() {
        final String packageName = mContext.getPackageName();
        final PackageManager packageManager = mContext.getPackageManager();

        try {
            final String[] permissions;

            final PackageInfo info = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                permissions = info.requestedPermissions;
            } else {
                permissions = new String[0];
            }

            return permissions;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isNeeded() {
        final String[] permissions = getRequiredPermissions();
        return hasAllPermissions(permissions);
    }

}
