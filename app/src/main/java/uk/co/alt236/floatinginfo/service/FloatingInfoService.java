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
package uk.co.alt236.floatinginfo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import uk.co.alt236.floatinginfo.data.access.BaseProvider;
import uk.co.alt236.floatinginfo.data.access.generalinfo.GeneralInfoProvider;

public class FloatingInfoService extends Service {

    private static boolean sIsRunning = false;
    private BaseProvider mMonitor;

    public static boolean isRunning() {
        return sIsRunning;
    }

    @Override
    public IBinder onBind(final Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMonitor = new GeneralInfoProvider(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sIsRunning = false;
        mMonitor.destroy();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        sIsRunning = true;
        mMonitor.start();
        return Service.START_STICKY;
    }

}
