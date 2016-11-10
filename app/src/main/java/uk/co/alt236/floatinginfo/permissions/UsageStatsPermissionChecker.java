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
