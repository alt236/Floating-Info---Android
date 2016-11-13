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

package uk.co.alt236.floatinginfo.ui.overlay;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.text.TextUtils;

import java.util.Locale;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.NetData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/**
 *
 */
/*package*/ class NetDataTextWriter implements TextWriter<NetData> {
    private final TextWriter<WifiInfo> wifiInfoTextWriter;

    public NetDataTextWriter() {
        wifiInfoTextWriter = new WifiInfoTextWriter();
    }

    @Override
    public void writeText(final NetData input, final StringBuilderHelper sb) {
        if (input != null) {

            final NetworkInfo netInfo = input.getNetworkInfo();

            sb.appendBold("Network Info");
            sb.startKeyValueSection();

            if (netInfo == null) {
                sb.append("State", "Offline");
            } else {
                final NetInfoConstantResolver resolver = new NetInfoConstantResolver();
                sb.append("Type", resolver.getNetworkType(netInfo));
                sb.append("State", String.valueOf(netInfo.getState()).toLowerCase(Locale.US));

                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    wifiInfoTextWriter.writeText(input.getWifiInfo(), sb);
                }

                final String proxy = TextUtils.isEmpty(input.getProxy()) ? "OFF" : input.getProxy();
                sb.append("Proxy", proxy);
            }

            sb.endKeyValueSection();
            sb.appendNewLine();
        }
    }

    @Override
    public void clear() {
        // NOOP
    }

}
