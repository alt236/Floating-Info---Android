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

import java.util.List;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.Interface;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.NetData;

/**
 *
 */
public class NetDataReader {
    private final ProxyInfoReader mProxyInfoReader;
    private final NetworkInfoReader mNetworkInfoReader;
    private final InterfaceReader mInterfaceReader;
    private NetData mNetData;

    public NetDataReader(final Context context) {
        final Context appContext = context.getApplicationContext();
        mProxyInfoReader = new ProxyInfoReader(appContext);
        mNetworkInfoReader = new NetworkInfoReader(appContext);
        mInterfaceReader = new InterfaceReader();

        update();
    }

    public void update() {
        final String proxyInfo = mProxyInfoReader.getProxyUrl();
        final WifiInfo wifiInfo = mNetworkInfoReader.getCurrentWifiInfo();
        final android.net.NetworkInfo networkInfo = mNetworkInfoReader.getActiveNetInfo();
        final List<Interface> interfaces = mInterfaceReader.getInterfaces();

        mNetData = new NetData(networkInfo, wifiInfo, proxyInfo, interfaces);
    }

    public NetData getNetData() {
        return mNetData;
    }
}
