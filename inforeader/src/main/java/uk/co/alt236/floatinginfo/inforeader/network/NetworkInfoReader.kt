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
package uk.co.alt236.floatinginfo.inforeader.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager

/*package*/
internal class NetworkInfoReader(context: Context) {
    private val mConnectivityManager: ConnectivityManager?
    private val mWifiManager: WifiManager?

    init {
        val application = context.applicationContext as Application
        mConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        mWifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager?
    }

    val currentWifiInfo: WifiInfo?
        get() = mWifiManager?.connectionInfo


    val activeNetInfo: NetworkInfo?
        get() = mConnectivityManager?.activeNetworkInfo

}