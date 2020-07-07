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

package uk.co.alt236.floatinginfo.inforeader.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.Nullable;

/*package*/ class NetworkInfoReader {

    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final WifiManager mWifiManager;

    public NetworkInfoReader(final Context context) {
        mContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    @Nullable
    public WifiInfo getCurrentWifiInfo() {
        final WifiInfo retVal;
        if (mWifiManager == null) {
            retVal = null;
        } else {
            retVal = mWifiManager.getConnectionInfo();
        }

        return retVal;
    }

    @Nullable
    public android.net.NetworkInfo getActiveNetInfo() {
        final android.net.NetworkInfo retVal;

        if (mConnectivityManager == null) {
            retVal = null;
        } else {
            retVal = mConnectivityManager.getActiveNetworkInfo();
        }

        return retVal;
    }
}
