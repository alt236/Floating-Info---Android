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
package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug.MemoryInfo;

public class MemoryInfoReader {
    private final ActivityManager mActivityManager;
    private MemoryData mMemoryInfo;

    public MemoryInfoReader(final Context context) {
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }


    public void update(final int pid) {
        if (pid > 0) {
            final MemoryInfo mi = mActivityManager.getProcessMemoryInfo(new int[]{pid})[0];
            mMemoryInfo = new MemoryData(pid, mi);
        } else {
            mMemoryInfo = MemoryData.getBlank(pid);
        }
    }

    public MemoryData getInfo() {
        return mMemoryInfo;
    }
}
