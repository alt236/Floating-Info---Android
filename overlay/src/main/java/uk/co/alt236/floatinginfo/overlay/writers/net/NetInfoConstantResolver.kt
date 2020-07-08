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

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

/**
 *
 */
/*package*/
internal class NetInfoConstantResolver {

    fun getNetworkType(info: NetworkInfo): String {
        return when (info.type) {
            ConnectivityManager.TYPE_BLUETOOTH -> "BLUETOOTH"
            ConnectivityManager.TYPE_DUMMY -> "DUMMY"
            ConnectivityManager.TYPE_ETHERNET -> "ETHERNET"
            ConnectivityManager.TYPE_MOBILE -> getMobileType(info)
            ConnectivityManager.TYPE_MOBILE_DUN -> "MOBILE DUN"
            ConnectivityManager.TYPE_MOBILE_HIPRI -> "MOBILE HIPRI"
            ConnectivityManager.TYPE_MOBILE_MMS -> "MOBILE MMS"
            ConnectivityManager.TYPE_VPN -> "VPN"
            ConnectivityManager.TYPE_WIFI -> "WIFI"
            ConnectivityManager.TYPE_WIMAX -> "WIMAX"
            else -> "???"
        }
    }

    private fun getMobileType(info: NetworkInfo): String {
        val subtypeAsString = when (info.subtype) {
            TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
            TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
            TelephonyManager.NETWORK_TYPE_EHRPD -> "EHRPD"
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO_0"
            TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO_A"
            TelephonyManager.NETWORK_TYPE_EVDO_B -> "EVDO_B"
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
            TelephonyManager.NETWORK_TYPE_GSM -> "GSM"
            TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPAP"
            TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
            TelephonyManager.NETWORK_TYPE_IDEN -> "IDEN"
            TelephonyManager.NETWORK_TYPE_IWLAN -> "IWLAN"
            TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "TD_SCDMA"
            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> "UNKNOWN"
            else -> "???"
        }

        val prefix = "MOBILE/"
        return if (info.isRoaming) {
            "$prefix$subtypeAsString/ROAMING"
        } else {
            prefix + subtypeAsString
        }
    }
}