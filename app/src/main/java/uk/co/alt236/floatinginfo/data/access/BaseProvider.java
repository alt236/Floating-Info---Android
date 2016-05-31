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
package uk.co.alt236.floatinginfo.data.access;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

public abstract class BaseProvider implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final Service mService;


    public BaseProvider(final Service service) {
        mService = service;
    }

    public abstract void destroy();

    public Context getApplicationContext() {
        return mService.getApplicationContext();
    }

    public Context getContext() {
        return mService;
    }

    public int getInteger(final int resId) {
        return mService.getResources().getInteger(resId);
    }

    public String getString(final int resId) {
        return mService.getString(resId);
    }

    public Object getSystemService(final String name) {
        return mService.getSystemService(name);
    }

    protected void registerReceiver(final BroadcastReceiver receiver, final IntentFilter filter) {
        getContext().registerReceiver(receiver, filter);
    }

    public abstract boolean start();

    protected void startActivity(final Intent intent) {
        getContext().startActivity(intent);
    }

    protected void startForeground(final int id, final Notification notification) {
        mService.startForeground(id, notification);
    }

    protected void unregisterReceiver(final BroadcastReceiver receiver) {
        getContext().unregisterReceiver(receiver);
    }
}
