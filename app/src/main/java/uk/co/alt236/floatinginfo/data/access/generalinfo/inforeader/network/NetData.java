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

package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

/**
 *
 */
public class NetData {
    private final String mProxy;
    private final android.net.NetworkInfo mNetworkInfo;
    private final WifiInfo mWifiInfo;

    public NetData(final NetworkInfo networkInfo, final WifiInfo wifiInfo, final String proxy) {
        mProxy = proxy;
        mNetworkInfo = networkInfo;
        mWifiInfo = wifiInfo;
    }

    public String getProxy() {
        return mProxy;
    }

    public WifiInfo getWifiInfo() {
        return mWifiInfo;
    }

    public android.net.NetworkInfo getNetworkInfo() {
        return mNetworkInfo;
    }
}
