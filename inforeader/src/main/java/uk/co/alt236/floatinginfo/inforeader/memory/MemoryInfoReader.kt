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
package uk.co.alt236.floatinginfo.inforeader.memory

import android.app.ActivityManager
import android.content.Context
import uk.co.alt236.floatinginfo.common.data.model.MemoryData

class MemoryInfoReader(context: Context) {
    private val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    var info: MemoryData? = null
        private set

    fun update(pid: Int) {
        info = if (pid > 0) {
            val mi = mActivityManager.getProcessMemoryInfo(intArrayOf(pid))[0]
            MemoryData(pid, mi)
        } else {
            MemoryData.getBlank(pid)
        }
    }

}