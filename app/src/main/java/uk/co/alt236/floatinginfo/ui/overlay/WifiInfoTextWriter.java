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

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.Locale;

import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/*package*/ class WifiInfoTextWriter implements TextWriter<WifiInfo> {
    private static final String NUMBER_WITH_UNIT = "%d %s";
    private static final String RSSI = "%d %s (%d/%d)";
    private static final int RSSI_LEVELS = 5;

    @Override
    public void writeText(final WifiInfo input, final StringBuilderHelper sb) {
        final String ssid;
        final String bssid;
        final int linkSpeed;
        final int rssi;

        if (input == null) {
            bssid = null;
            ssid = null;
            linkSpeed = 0;
            rssi = 0;
        } else {
            bssid = input.getBSSID();
            ssid = input.getSSID();
            linkSpeed = input.getLinkSpeed();
            rssi = input.getRssi();
        }

        sb.append("SSID", pretty(ssid));
        sb.append("BSSID", pretty(bssid));
        sb.append("RSSI", getRssi(input));
        sb.append("Speed", String.format(Locale.US, NUMBER_WITH_UNIT, linkSpeed, WifiInfo.LINK_SPEED_UNITS));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && input != null) {
            final int freq = input.getFrequency();
            sb.append("Freq", String.format(Locale.US, NUMBER_WITH_UNIT, freq, WifiInfo.FREQUENCY_UNITS));
        }
    }

    private String getRssi(final WifiInfo wifiInfo) {
        final int rssi = wifiInfo.getRssi();
        final int humanValue = WifiManager.calculateSignalLevel(rssi, RSSI_LEVELS);

        return String.format(Locale.US, RSSI, rssi, "dBm", humanValue, RSSI_LEVELS);
    }

    private String pretty(final String string) {
        return string == null ? "n/a" : string;
    }

    @Override
    public void clear() {
        // NOOP
    }
}
