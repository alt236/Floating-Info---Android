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
import uk.co.alt236.floatinginfo.common.data.model.net.NetData

/**
 *
 */
class NetDataReader(context: Context) {
    private val proxyInfoReader = ProxyInfoReader(context.applicationContext)
    private val networkInfoReader = NetworkInfoReader(context.applicationContext)
    private val interfaceReader = InterfaceReader()

    var netData: NetData? = null
        private set

    fun update() {
        val proxyInfo = proxyInfoReader.proxyUrl
        val wifiInfo = networkInfoReader.currentWifiInfo
        val networkInfo = networkInfoReader.activeNetInfo
        val interfaces = interfaceReader.interfaces
        println("interfaces: $interfaces")
        netData = NetData(networkInfo, wifiInfo, proxyInfo, interfaces)
    }

    init {
        update()
    }
}