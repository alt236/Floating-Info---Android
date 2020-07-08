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
import android.text.TextUtils
import uk.co.alt236.floatinginfo.common.data.model.net.NetData
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper
import uk.co.alt236.floatinginfo.overlay.writers.TextWriter
import java.util.*

/**
 *
 */
internal class NetDataTextWriter : TextWriter<NetData?> {
    private val wifiInfoTextWriter = WifiInfoTextWriter()

    override fun writeText(input: NetData?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }

        val netInfo = input.networkInfo
        sb.appendBold("Network Info")
        sb.startKeyValueSection()

        if (netInfo == null) {
            sb.append("State", "Offline")
        } else {
            val resolver = NetInfoConstantResolver()
            sb.append("Type", resolver.getNetworkType(netInfo))
            sb.append("State", netInfo.state.toString().toLowerCase(Locale.US))
            if (netInfo.type == ConnectivityManager.TYPE_WIFI) {
                wifiInfoTextWriter.writeText(input.wifiInfo, sb)
            }
            val proxy = if (TextUtils.isEmpty(input.proxy)) "OFF" else input.proxy
            sb.append("Proxy", proxy)
        }

        sb.endKeyValueSection()
        sb.appendNewLine()
    }

    override fun clear() {
        // NOOP
    }
}