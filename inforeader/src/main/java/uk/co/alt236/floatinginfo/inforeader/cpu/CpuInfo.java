/*
 * Copyright 2017 Alexandros Schillings
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

package uk.co.alt236.floatinginfo.inforeader.cpu;

class CpuInfo {
    private int mUsage;
    private long mLastTotal;
    private long mLastIdle;

    public CpuInfo() {
        mUsage = 0;
        mLastTotal = 0;
        mLastIdle = 0;
    }

    public int getUsage() {
        return mUsage;
    }

    public void update(final String[] parts) {
        // the columns are:
        //
        // 0 "cpu": the string "cpu" that identifies the line
        // 1 user: normal processes executing in user mode
        // 2 nice: niced processes executing in user mode
        // 3 system: processes executing in kernel mode
        // 4 idle: twiddling thumbs
        // 5 iowait: waiting for I/O to complete
        // 6 irq: servicing interrupts
        // 7 softirq: servicing softirqs
        //
        final long idle = Long.parseLong(parts[4], 10);
        long total = 0;
        boolean head = true;
        for (final String part : parts) {
            if (head) {
                head = false;
                continue;
            }
            total += Long.parseLong(part, 10);
        }
        final long diffIdle = idle - mLastIdle;
        final long diffTotal = total - mLastTotal;
        mUsage = (int) ((float) (diffTotal - diffIdle) / diffTotal * 100);
        mLastTotal = total;
        mLastIdle = idle;
        //Log.i(TAG, "CPU total=" + total + "; idle=" + idle + "; usage=" + mUsage);
    }
}
