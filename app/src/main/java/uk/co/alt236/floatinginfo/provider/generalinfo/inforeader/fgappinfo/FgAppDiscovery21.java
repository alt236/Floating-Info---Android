package uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.fgappinfo;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
/*package*/ class FgAppDiscovery21 extends FgAppDiscovery {
    private static final long TIME_WINDOW = 1000 * 1000;
    private final UsageStatsManager mUsageStatsManager;

    public FgAppDiscovery21(final Context context) {
        super(context);
        mUsageStatsManager = getUsageStatsManager(context);
    }

    @SuppressWarnings("WrongConstant")
    private static UsageStatsManager getUsageStatsManager(final Context context) {
        final UsageStatsManager manager;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // intentionally using string value as Context.USAGE_STATS_SERVICE was
            // strangely only added in API 22 (LOLLIPOP_MR1)
            manager = (UsageStatsManager) context.getSystemService("usagestats");
        } else {
            manager = null;
        }

        return manager;
    }

    @Override
    public ForegroundAppData getForegroundApp() {
        final ForegroundAppData appInfo;

        final long time = System.currentTimeMillis();

        final List<UsageStats> appList = mUsageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, time - TIME_WINDOW, time);
        if (appList != null && appList.size() > 0) {
            final SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();

            for (final UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }

            if (!mySortedMap.isEmpty()) {
                final UsageStats currentApp = mySortedMap.get(mySortedMap.lastKey());
                final String packageName = currentApp.getPackageName();
                appInfo = new ForegroundAppData(-1, packageName, getAppName(packageName));
            } else {
                appInfo = FALLBACK_APP_INFO;
            }
        } else {
            appInfo = FALLBACK_APP_INFO;
        }

        return appInfo;
    }
}
