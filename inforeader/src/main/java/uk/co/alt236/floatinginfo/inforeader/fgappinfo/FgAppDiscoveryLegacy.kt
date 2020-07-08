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
package uk.co.alt236.floatinginfo.inforeader.fgappinfo

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData

/*package*/
internal class FgAppDiscoveryLegacy(context: Context) : FgAppDiscovery(context) {
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    override fun getForegroundAppData(): ForegroundAppData {
        val foregroundTaskInfo = activityManager.getRunningTasks(1)[0]

        val pkg = foregroundTaskInfo.topActivity.packageName
        val appProcesses = activityManager.runningAppProcesses

        for (appProcess in appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (ap in appProcess.pkgList) {
                    if (ap == pkg) {
                        return ForegroundAppData(
                                appProcess.pid,
                                pkg,
                                getAppName(pkg))
                    }
                }
            }
        }

        return ForegroundAppData(0, pkg, FgAppDiscovery.Companion.UNKNOWN_APP_NAME)
    }

}