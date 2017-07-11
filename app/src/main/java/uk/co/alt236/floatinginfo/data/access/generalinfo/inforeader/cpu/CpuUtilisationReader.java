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
package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

// Taken http://stackoverflow.com/questions/7593829/how-to-get-the-processor-number-on-android
public class CpuUtilisationReader {
    private static final String TAG = "CpuUtilisationReader";
    private RandomAccessFile statFile;
    private CpuInfo mCpuInfoTotal;
    private ArrayList<CpuInfo> mCpuInfoList;

    public CpuUtilisationReader() {
        update();
    }

    private void closeFile() throws IOException {
        if (statFile != null)
            statFile.close();
    }

    private void createCpuInfo(final int cpuId, final String[] parts) {
        if (cpuId == -1) {
            if (mCpuInfoTotal == null)
                mCpuInfoTotal = new CpuInfo();
            mCpuInfoTotal.update(parts);
        } else {
            if (mCpuInfoList == null)
                mCpuInfoList = new ArrayList<CpuInfo>();
            if (cpuId < mCpuInfoList.size())
                mCpuInfoList.get(cpuId).update(parts);
            else {
                final CpuInfo info = new CpuInfo();
                info.update(parts);
                mCpuInfoList.add(info);
            }
        }
    }

    private void createFile() throws FileNotFoundException {
        statFile = new RandomAccessFile("/proc/stat", "r");
    }

    public int getCpuUsage(final int cpuId) {
        int usage = 0;
        if (mCpuInfoList != null) {
            int cpuCount = mCpuInfoList.size();
            if (cpuCount > 0) {
                cpuCount--;
                if (cpuId == cpuCount) { // -1 total cpu usage
                    usage = mCpuInfoList.get(0).getUsage();
                } else {
                    if (cpuId <= cpuCount)
                        usage = mCpuInfoList.get(cpuId).getUsage();
                    else
                        usage = -1;
                }
            }
        }
        return usage;
    }

    public int getTotalCpuUsage() {
        int usage = 0;
        if (mCpuInfoTotal != null)
            usage = mCpuInfoTotal.getUsage();
        return usage;
    }

    private void parseCpuLine(final int cpuId, final String cpuLine) {
        if (cpuLine != null && cpuLine.length() > 0) {
            final String[] parts = cpuLine.split("[ ]+");
            final String cpuLabel = "cpu";

            if (parts[0].indexOf(cpuLabel) != -1) {
                createCpuInfo(cpuId, parts);
            }
        } else {
            Log.e(TAG, "unable to get cpu line");
        }
    }

    public CpuData getCpuInfo() {
        final CpuData result = new CpuData(getTotalCpuUsage());

        if (mCpuInfoList != null) {
            for (int i = 0; i < mCpuInfoList.size(); i++) {
                final CpuInfo info = mCpuInfoList.get(i);
                result.addCpuUtil(info.getUsage());
            }
        }

        return result;
    }

    private void parseFile() {
        if (statFile != null) {
            try {
                statFile.seek(0);
                String cpuLine = "";
                int cpuId = -1;
                do {
                    cpuLine = statFile.readLine();
                    parseCpuLine(cpuId, cpuLine);
                    cpuId++;
                } while (cpuLine != null);
            } catch (final IOException e) {
                Log.e(TAG, "Ops: " + e);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        if (mCpuInfoTotal != null) {
            buf.append("Cpu Total : ");
            buf.append(mCpuInfoTotal.getUsage());
            buf.append("%");
        }
        if (mCpuInfoList != null) {
            for (int i = 0; i < mCpuInfoList.size(); i++) {
                final CpuInfo info = mCpuInfoList.get(i);
                buf.append(" Cpu Core(");
                buf.append(i);
                buf.append(") : ");
                buf.append(info.getUsage());
                buf.append("%");
                info.getUsage();
            }
        }
        return buf.toString();
    }

    public void update() {
        try {
            createFile();
            parseFile();
            closeFile();
        } catch (final FileNotFoundException e) {
            statFile = null;
            Log.e(TAG, "cannot open /proc/stat: " + e);
        } catch (final IOException e) {
            Log.e(TAG, "cannot close /proc/stat: " + e);
        }
    }

    private class CpuInfo {
        private int mUsage;
        private long mLastTotal;
        private long mLastIdle;

        public CpuInfo() {
            mUsage = 0;
            mLastTotal = 0;
            mLastIdle = 0;
        }

        private int getUsage() {
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
}
