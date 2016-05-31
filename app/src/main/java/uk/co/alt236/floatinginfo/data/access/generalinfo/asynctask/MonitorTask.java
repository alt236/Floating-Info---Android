/*******************************************************************************
 * Copyright 2014 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uk.co.alt236.floatinginfo.data.access.generalinfo.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppDiscovery;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.NetData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.NetDataReader;
import uk.co.alt236.floatinginfo.util.Constants;

public class MonitorTask extends AsyncTask<Void, MonitorTask.MonitorUpdate, Void> {

    private final ForegroundAppDiscovery mForegroundAppDiscovery;
    private final NetDataReader mNetDataReader;

    public MonitorTask(final Context context) {
        mForegroundAppDiscovery = new ForegroundAppDiscovery(context.getApplicationContext());
        mNetDataReader = new NetDataReader(context.getApplicationContext());
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        MonitorUpdate p;

        while (!isCancelled()) {
            mNetDataReader.update();

            final ForegroundAppData appData = mForegroundAppDiscovery.getForegroundApp();
            final NetData netData = mNetDataReader.getNetData();

            p = new MonitorUpdate(appData, netData);

            publishProgress(p);

            try {
                Thread.sleep(Constants.PROC_MONITOR_SLEEP);
            } catch (final InterruptedException e) {
                // NOOP
            }
        }

        return null;
    }


    public static class MonitorUpdate {
        private final ForegroundAppData mForegroundAppData;
        private final NetData mNetData;

        public MonitorUpdate(final ForegroundAppData foregroundAppData,
                             final NetData netData) {
            this.mForegroundAppData = foregroundAppData;
            this.mNetData = netData;
        }

        public ForegroundAppData getForegroundAppData() {
            return mForegroundAppData;
        }

        public NetData getNetData() {
            return mNetData;
        }
    }
}
