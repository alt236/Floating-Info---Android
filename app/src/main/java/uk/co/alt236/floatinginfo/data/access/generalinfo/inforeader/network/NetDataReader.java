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

import android.content.Context;
import android.net.wifi.WifiInfo;

/**
 *
 */
public class NetDataReader {
    private final ProxyInfo mProxyInfo;
    private final NetworkInfo mNetworkInfo;
    private NetData mNetData;

    public NetDataReader(final Context context) {
        final Context appContext = context.getApplicationContext();
        mProxyInfo = new ProxyInfo(appContext);
        mNetworkInfo = new NetworkInfo(appContext);

        update();
    }

    public void update() {
        final String proxyInfo = mProxyInfo.getProxyUrl();
        final WifiInfo wifiInfo = mNetworkInfo.getCurrentWifiInfo();
        final android.net.NetworkInfo networkInfo = mNetworkInfo.getActiveNetInfo();
        mNetData = new NetData(networkInfo, wifiInfo, proxyInfo);
    }

    public NetData getNetData() {
        return mNetData;
    }
}
