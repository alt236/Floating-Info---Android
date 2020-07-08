/*
 * Copyright 2020 Alexandros Schillings
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
package uk.co.alt236.floatinginfo.overlay.writers.net

import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper
import uk.co.alt236.floatinginfo.overlay.writers.TextWriter
import java.util.*

internal class WifiInfoTextWriter : TextWriter<WifiInfo> {

    override fun writeText(input: WifiInfo?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }

        val bssid = input.bssid
        val ssid = input.ssid
        val linkSpeed = input.linkSpeed

        sb.append("SSID", pretty(ssid))
        sb.append("BSSID", pretty(bssid))
        sb.append("RSSI", getRssi(input))
        sb.append("Speed", String.format(Locale.US, NUMBER_WITH_UNIT, linkSpeed, WifiInfo.LINK_SPEED_UNITS))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val freq = input.frequency
            sb.append("Freq", String.format(Locale.US, NUMBER_WITH_UNIT, freq, WifiInfo.FREQUENCY_UNITS))
        }
    }

    private fun getRssi(wifiInfo: WifiInfo): String {
        val rssi = wifiInfo.rssi
        val humanValue = WifiManager.calculateSignalLevel(rssi, RSSI_LEVELS)
        return String.format(Locale.US, RSSI, rssi, "dBm", humanValue, RSSI_LEVELS)
    }

    private fun pretty(string: String?): String {
        return string ?: "n/a"
    }

    override fun clear() {
        // NOOP
    }

    companion object {
        private const val NUMBER_WITH_UNIT = "%d %s"
        private const val RSSI = "%d %s (%d/%d)"
        private const val RSSI_LEVELS = 5
    }
}