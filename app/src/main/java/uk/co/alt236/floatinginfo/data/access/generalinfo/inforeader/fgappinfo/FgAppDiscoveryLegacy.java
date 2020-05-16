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

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;

/*package*/ class FgAppDiscoveryLegacy extends FgAppDiscovery {

    private final ActivityManager mActivityManager;

    public FgAppDiscoveryLegacy(final Context context) {
        super(context);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

    }

    @Override
    public ForegroundAppData getForegroundApp() {
        final ActivityManager.RunningTaskInfo foregroundTaskInfo = mActivityManager.getRunningTasks(1).get(0);
        final String pkg = foregroundTaskInfo.topActivity.getPackageName();

        final List<ActivityManager.RunningAppProcessInfo> appProcesses = mActivityManager.getRunningAppProcesses();

        for (final ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (final String ap : appProcess.pkgList) {
                    if (ap.equals(pkg)) {
                        return new ForegroundAppData(
                                appProcess.pid,
                                pkg,
                                getAppName(pkg));
                    }
                }
            }
        }

        return new ForegroundAppData(0, pkg, UNKNOWN_APP_NAME);
    }
}
