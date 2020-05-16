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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.alt236.floatinginfo.BuildConfig;
import uk.co.alt236.floatinginfo.common.data.model.CpuData;

// Taken http://stackoverflow.com/questions/7593829/how-to-get-the-processor-number-on-android
public class CpuUtilisationReader {
    private static final String TAG = "CpuUtilisationReader";
    private static final String STAT_FILE = BuildConfig.STAT_FILE;

    private RandomAccessFile statFile;
    private CpuInfo mCpuInfoTotal;
    private ArrayList<CpuInfo> mCpuInfoList;
    private final AtomicBoolean mFileOpenedOk = new AtomicBoolean();

    public CpuUtilisationReader() {
        update();
    }

    private void closeFile() throws IOException {
        if (statFile != null) {
            statFile.close();
        }
    }

    private void createCpuInfo(final int cpuId, final String[] parts) {
        if (cpuId == -1) {
            if (mCpuInfoTotal == null) {
                mCpuInfoTotal = new CpuInfo();
            }
            mCpuInfoTotal.update(parts);
        } else {
            if (mCpuInfoList == null) {
                mCpuInfoList = new ArrayList<>();
            }

            if (cpuId < mCpuInfoList.size()) {
                mCpuInfoList.get(cpuId).update(parts);
            } else {
                final CpuInfo info = new CpuInfo();
                info.update(parts);
                mCpuInfoList.add(info);
            }
        }
    }

    private void openFile() throws FileNotFoundException {
        statFile = new RandomAccessFile(STAT_FILE, "r");
        mFileOpenedOk.set(true);
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

    private int getTotalCpuUsage() {
        int usage = 0;
        if (mCpuInfoTotal != null) {
            usage = mCpuInfoTotal.getUsage();
        }
        return usage;
    }

    private void parseCpuLine(final int cpuId, final String cpuLine) {
        if (cpuLine != null && cpuLine.length() > 0) {
            final String[] parts = cpuLine.split("[ ]+");
            final String cpuLabel = "cpu";

            if (parts[0].contains(cpuLabel)) {
                createCpuInfo(cpuId, parts);
            }
        } else {
            Log.e(TAG, "unable to get cpu line");
        }
    }

    public CpuData getCpuInfo() {
        final CpuData retVal;

        if (mFileOpenedOk.get()) {
            final List<Integer> cpuUtilisation = new ArrayList<>();

            if (mCpuInfoList != null) {
                for (int i = 0; i < mCpuInfoList.size(); i++) {
                    final CpuInfo info = mCpuInfoList.get(i);
                    cpuUtilisation.add(info.getUsage());
                }
            }

            retVal = new CpuData(STAT_FILE, getTotalCpuUsage(), cpuUtilisation);
        } else {
            retVal = new CpuData(STAT_FILE, true);
        }

        return retVal;
    }

    private void parseFile() {
        if (statFile != null) {
            try {
                statFile.seek(0);
                String cpuLine;
                int cpuId = -1;
                do {
                    cpuLine = statFile.readLine();
                    parseCpuLine(cpuId, cpuLine);
                    cpuId++;
                } while (cpuLine != null);
            } catch (final IOException e) {
                Log.e(TAG, "Error parsing file: " + e);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (mFileOpenedOk.get()) {
            if (mCpuInfoTotal != null) {
                sb.append("Cpu Total : ");
                sb.append(mCpuInfoTotal.getUsage());
                sb.append("%");
            }
            if (mCpuInfoList != null) {
                for (int i = 0; i < mCpuInfoList.size(); i++) {
                    final CpuInfo info = mCpuInfoList.get(i);
                    sb.append(" Cpu Core(");
                    sb.append(i);
                    sb.append(") : ");
                    sb.append(info.getUsage());
                    sb.append("%");
                    info.getUsage();
                }
            }
        } else {
            sb.append("Error: Failed to open " + STAT_FILE);
        }
        return sb.toString();
    }

    public void update() {
        try {
            openFile();
            parseFile();
            closeFile();
        } catch (final FileNotFoundException e) {
            mFileOpenedOk.set(false);
            statFile = null;
            Log.e(TAG, "cannot open " + STAT_FILE + ":" + e);
        } catch (final IOException e) {
            Log.e(TAG, "cannot close " + STAT_FILE + ":" + e);
        }
    }

}
