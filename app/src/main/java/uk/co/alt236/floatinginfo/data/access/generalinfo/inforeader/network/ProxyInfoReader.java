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
import android.net.ConnectivityManager;


/*package*/ class ProxyInfoReader {

    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;

    public ProxyInfoReader(final Context context) {
        mContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public String getProxyUrl() {
        String proxyHost = null;
        String proxyPort = null;

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (mConnectivityManager != null) {
                    final android.net.ProxyInfo proxyInfo = mConnectivityManager.getDefaultProxy();
                    if (proxyInfo != null) {
                        proxyHost = proxyInfo.getHost();
                        proxyPort = String.valueOf(proxyInfo.getPort());
                    }
                }
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                proxyHost = System.getProperty("http.proxyHost");
                proxyPort = System.getProperty("http.proxyPort");
            } else {
                proxyHost = android.net.Proxy.getHost(mContext);
                proxyPort = String.valueOf(android.net.Proxy.getPort(mContext));
            }

        } catch (final Exception ex) {
            proxyHost = null;
            proxyPort = null;
        }

        final String proxyAddress;
        if (proxyHost != null) {
            proxyAddress = proxyHost + ":" + proxyPort;
        } else {
            proxyAddress = null;
        }

        return proxyAddress;
    }
}
