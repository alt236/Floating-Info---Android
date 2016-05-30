package uk.co.alt236.floatinginfo.activity.permissions;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

public final class PermissionWrapper {

    private final Activity mActivity;
    private final OverlayPermissionLogic mOverlayPermissionLogic;
    private final UsageStatsPermissionLogic mUsageStatsPermissionLogic;

    public PermissionWrapper(final Activity activity) {
        this.mActivity = activity;
        this.mOverlayPermissionLogic = new OverlayPermissionLogic(activity);
        this.mUsageStatsPermissionLogic = new UsageStatsPermissionLogic(activity);
    }

    private static void showFailDialog(final Activity activity) {
        final FragmentManager fm = activity.getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        new PermissionsRequiredDialog().show(ft, "dialog");
    }

    public void onResume() {
        if (mOverlayPermissionLogic.needToAsk()) {
            mOverlayPermissionLogic.ask();
        } else if (mUsageStatsPermissionLogic.needToAsk()) {
            mUsageStatsPermissionLogic.ask();
        } else {
            PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(
                    mActivity,
                    new PermissionsResultAction() {
                        @Override
                        public void onGranted() {
                            // Proceed with initialization
                        }

                        @Override
                        public void onDenied(final String permission) {
                            // Notify the user that you need all of the permissions
                            showFailDialog(mActivity);
                        }
                    });
        }
    }
}
