package uk.co.alt236.floatinginfo.permissions;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

public class UsageStatsPermissionChecker implements PermissionChecker {
    private final Context mContext;

    public UsageStatsPermissionChecker(final Context context) {
        mContext = context.getApplicationContext();
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public boolean isNeeded() {
        final boolean retVal;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService("usagestats");
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
}
