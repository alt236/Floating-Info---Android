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

package uk.co.alt236.floatinginfo.data.access.generalinfo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import uk.co.alt236.floatinginfo.common.data.model.CpuData;
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;
import uk.co.alt236.floatinginfo.common.data.model.LocaleData;
import uk.co.alt236.floatinginfo.common.data.model.MemoryData;
import uk.co.alt236.floatinginfo.common.data.model.net.NetData;
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu.CpuUtilisationReader;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppDiscovery;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.general.LocaleInfoReader;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryInfoReader;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.NetDataReader;
import uk.co.alt236.floatinginfo.util.Constants;

/*package*/ class MonitorTask {
    private final Context mContext;
    private final EnabledInfoPrefs mEnabledInfoPrefs;
    private InnerTask mTask;

    public MonitorTask(final Context context,
                       final EnabledInfoPrefs prefs) {
        mContext = context.getApplicationContext();
        mEnabledInfoPrefs = prefs;
    }

    public void start(@NonNull final UpdateCallback callback) {
        mTask = new InnerTask(mContext, mEnabledInfoPrefs) {
            @Override
            protected void onProgressUpdate(final MonitorTask.MonitorUpdate... values) {
                callback.onUpdate(values[0]);
            }
        };

        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void stop() {
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
    }

    public interface UpdateCallback {
        void onUpdate(MonitorUpdate update);
    }

    private static class InnerTask extends AsyncTask<Void, MonitorTask.MonitorUpdate, Void> {

        private final ForegroundAppDiscovery mForegroundAppDiscovery;
        private final NetDataReader mNetDataReader;
        private final LocaleInfoReader mLocaleInfoReader;
        private final CpuUtilisationReader mCpuUtilisationReader;
        private final MemoryInfoReader mMemoryInfoReader;
        private final EnabledInfoPrefs mEnabledInfoPrefs;

        public InnerTask(final Context context, final EnabledInfoPrefs enabledInfoPrefs) {
            mEnabledInfoPrefs = enabledInfoPrefs;
            mForegroundAppDiscovery = new ForegroundAppDiscovery(context);
            mNetDataReader = new NetDataReader(context);
            mMemoryInfoReader = new MemoryInfoReader(context);
            mCpuUtilisationReader = new CpuUtilisationReader();
            mLocaleInfoReader = new LocaleInfoReader(context);
        }

        @Override
        protected Void doInBackground(final Void... voids) {

            while (!isCancelled()) {
                final ForegroundAppData appData = mForegroundAppDiscovery.getForegroundApp();
                publishProgress(
                        new MonitorUpdate(
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
    }

    public static class MonitorUpdate {
        private final ForegroundAppData mForegroundAppData;
        private final NetData mNetData;
        private final MemoryData mMemoryData;
        private final CpuData mCpuData;
        private final LocaleData mLocaleData;

        public MonitorUpdate(final ForegroundAppData appData,
                             final NetData netData,
                             final MemoryData info,
                             final CpuData cpuInfo,
                             final LocaleData localeData) {

            mForegroundAppData = appData;
            mNetData = netData;
            mMemoryData = info;
            mCpuData = cpuInfo;
            mLocaleData = localeData;
        }

        public ForegroundAppData getForegroundAppData() {
            return mForegroundAppData;
        }

        public NetData getNetData() {
            return mNetData;
        }

        public MemoryData getMemoryData() {
            return mMemoryData;
        }

        public CpuData getCpuData() {
            return mCpuData;
        }

        public LocaleData getGeneralData() {
            return mLocaleData;
        }
    }
}