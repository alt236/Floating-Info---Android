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

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

/*package*/
internal class ProxyInfoReader(context: Context) {
    private val mConnectivityManager: ConnectivityManager?
    val proxyUrl: String
        get() {
            var proxyHost: String? = null
            var proxyPort: String? = null

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mConnectivityManager != null) {
                        val proxyInfo = mConnectivityManager.defaultProxy
                        if (proxyInfo != null) {
                            proxyHost = proxyInfo.host
                            proxyPort = proxyInfo.port.toString()
                        }
                    }
                } else {
                    proxyHost = System.getProperty("http.proxyHost")
                    proxyPort = System.getProperty("http.proxyPort")
                }
            } catch (ex: Exception) {
                proxyHost = null
                proxyPort = null
            }

            return if (proxyHost != null) {
                "$proxyHost:$proxyPort"
            } else {
                ""
            }
        }

    init {
        val appContext = context.applicationContext
        mConnectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}