/*
 * Copyright 2020 Alexandros Schillings
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

package uk.co.alt236.floatinginfo.data.access.generalinfo.monitortask;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import uk.co.alt236.floatinginfo.common.data.model.CpuData;
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;
import uk.co.alt236.floatinginfo.common.data.model.LocaleData;
import uk.co.alt236.floatinginfo.common.data.model.MemoryData;
import uk.co.alt236.floatinginfo.common.data.model.net.NetData;
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs;
import uk.co.alt236.floatinginfo.inforeader.cpu.CpuUtilisationReader;
import uk.co.alt236.floatinginfo.inforeader.fgappinfo.ForegroundAppDiscovery;
import uk.co.alt236.floatinginfo.inforeader.general.LocaleInfoReader;
import uk.co.alt236.floatinginfo.inforeader.memory.MemoryInfoReader;
import uk.co.alt236.floatinginfo.inforeader.network.NetDataReader;
import uk.co.alt236.floatinginfo.util.Constants;

class InnerTask extends AsyncTask<Void, Update, Void> {

    private final ForegroundAppDiscovery mForegroundAppDiscovery;
    private final NetDataReader mNetDataReader;
    private final LocaleInfoReader mLocaleInfoReader;
    private final CpuUtilisationReader mCpuUtilisationReader;
    private final MemoryInfoReader mMemoryInfoReader;
    private final EnabledInfoPrefs mEnabledInfoPrefs;
    private final ProcessMonitor.UpdateCallback callback;

    InnerTask(final Context context, final EnabledInfoPrefs enabledInfoPrefs, @NonNull final ProcessMonitor.UpdateCallback callback) {
        mEnabledInfoPrefs = enabledInfoPrefs;
        mForegroundAppDiscovery = new ForegroundAppDiscovery(context);
        mNetDataReader = new NetDataReader(context);
        mMemoryInfoReader = new MemoryInfoReader(context);
        mCpuUtilisationReader = new CpuUtilisationReader();
        mLocaleInfoReader = new LocaleInfoReader(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(final Void... voids) {

        while (!isCancelled()) {
            final ForegroundAppData appData = mForegroundAppDiscovery.getForegroundApp();
            publishProgress(
                    new Update(
                            appData,
                            getNetData(),
                            getMemoryData(appData.getPid()),
                            getCpuData(),
                            getLocaleData()));

            if (!isCancelled()) {
                try {
                    Thread.sleep(Constants.PROC_MONITOR_SLEEP);
                } catch (final InterruptedException e) {
                    // NOOP
                }
            }
        }

        return null;
    }

    private NetData getNetData() {
        final NetData retVal;
        if (mEnabledInfoPrefs.isNetInfoEnabled()) {
            mNetDataReader.update();
            retVal = mNetDataReader.getNetData();
        } else {
            retVal = null;
        }
        return retVal;
    }

    private MemoryData getMemoryData(final int pid) {
        final MemoryData retVal;
        if (mEnabledInfoPrefs.isMemoryInfoEnabled()) {
            mMemoryInfoReader.update(pid);
            retVal = mMemoryInfoReader.getInfo();
        } else {
            retVal = null;
        }
        return retVal;
    }

    private CpuData getCpuData() {
        final CpuData retVal;
        if (mEnabledInfoPrefs.isCpuInfoEnabled()) {
            mCpuUtilisationReader.update();
            retVal = mCpuUtilisationReader.getCpuInfo();
        } else {
            retVal = null;
        }
        return retVal;
    }

    private LocaleData getLocaleData() {
        final LocaleData retVal;
        if (mEnabledInfoPrefs.isLocaleInfoEnabled()) {
            mLocaleInfoReader.update();
            retVal = mLocaleInfoReader.getData();
        } else {
            retVal = null;
        }
        return retVal;
    }

    @Override
    @CallSuper
    protected void onProgressUpdate(final Update... values) {
        callback.onUpdate(values[0]);
    }
}
