/*
 * Copyright 2016 Alexandros Schillings
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

package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;

/**
 *
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
/*package*/ class FgAppDiscovery21 extends FgAppDiscovery {
    private static final long TIME_WINDOW = 1000 * 1000;
    private final UsageStatsManager mUsageStatsManager;
    private final ProcessStore mProcessStore;

    public FgAppDiscovery21(final Context context) {
        super(context);
        mProcessStore = new ProcessStore();
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
                mProcessStore.update();
                final UsageStats currentApp = mySortedMap.get(mySortedMap.lastKey());
                final String packageName = currentApp.getPackageName();
                final int pid = mProcessStore.getPidFromName(packageName);
                appInfo = new ForegroundAppData(pid, packageName, getAppName(packageName));
            } else {
                appInfo = FALLBACK_APP_INFO;
            }
        } else {
            appInfo = FALLBACK_APP_INFO;
        }

        return appInfo;
    }
}
