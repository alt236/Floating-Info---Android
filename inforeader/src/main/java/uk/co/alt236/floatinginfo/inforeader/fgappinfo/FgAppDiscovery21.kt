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

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.util.Log
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData
import java.util.*

/**
 *
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal class FgAppDiscovery21(context: Context) : FgAppDiscovery(context) {
    private val usageStatsManager = getUsageStatsManager(context)
    private val processStore: ProcessStore = ProcessStore()

    override fun getForegroundAppData(): ForegroundAppData {
        if (usageStatsManager == null) {
            Log.i("FgAppDiscovery21", "manager is null")
            return FALLBACK_APP_INFO
        }

        val time = System.currentTimeMillis()
        val appList = getUsageStats(time)

        if (appList.isEmpty()) {
            return FALLBACK_APP_INFO
        }

        val sortedMap: SortedMap<Long, UsageStats> = TreeMap()
        for (usageStats in appList) {
            sortedMap[usageStats.lastTimeUsed] = usageStats
        }

        return if (sortedMap.isEmpty()) {
            FALLBACK_APP_INFO
        } else {
            processStore.update()
            val currentApp = sortedMap[sortedMap.lastKey()]
            val packageName = currentApp!!.packageName
            val pid = processStore.getPidFromName(packageName)
            ForegroundAppData(pid, packageName, getAppName(packageName))
        }
    }


    private fun getUsageStats(endTime: Long): List<UsageStats> {
        val beginTime = endTime - TIME_WINDOW
        return usageStatsManager?.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                beginTime,
                endTime) ?: emptyList()
    }

    companion object {
        private const val TIME_WINDOW = 1000 * 1000.toLong()

        @SuppressLint("WrongConstant")
        private fun getUsageStatsManager(context: Context): UsageStatsManager? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // intentionally using string value as Context.USAGE_STATS_SERVICE was
                // strangely only added in API 22 (LOLLIPOP_MR1)
                context.getSystemService("usagestats") as UsageStatsManager
            } else {
                null
            }
        }
    }
}