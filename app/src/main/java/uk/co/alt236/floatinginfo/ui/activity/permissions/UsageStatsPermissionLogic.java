package uk.co.alt236.floatinginfo.ui.activity.permissions;


import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import java.util.List;

/*package*/ class UsageStatsPermissionLogic {
    public final static int REQUEST_CODE = -1010101;

    private final Activity mActivity;

    public UsageStatsPermissionLogic(final Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("WrongConstant")
    public boolean needToAsk() {
        final boolean retVal;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final UsageStatsManager usageStatsManager = (UsageStatsManager) mActivity.getSystemService("usagestats");
            final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY,
                    0,
                    System.currentTimeMillis());

            retVal = queryUsageStats == null || queryUsageStats.isEmpty();
        } else {
            retVal = false;
        }

        return retVal;
    }


    public void ask() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** check if we already  have permission to draw over other apps */
            if (needToAsk()) {
                try {
                    final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(intent);
                } catch (final ActivityNotFoundException exception) {
                    final Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    final ComponentName componentName
                            = new ComponentName("com.android.settings", "com.android.settings.Settings$SecuritySettingsActivity");
                    intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(intent);
                }
            }
        }
    }
}
