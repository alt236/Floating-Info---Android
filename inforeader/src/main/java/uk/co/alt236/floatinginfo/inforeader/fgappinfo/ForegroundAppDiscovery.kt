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
package uk.co.alt236.floatinginfo.inforeader.fgappinfo

import android.content.Context
import android.os.Build
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData

class ForegroundAppDiscovery(context: Context) {
    private var appDiscovery: FgAppDiscovery

    init {
        val appContext = context.applicationContext
        appDiscovery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FgAppDiscovery21(appContext)
        } else {
            FgAppDiscoveryLegacy(appContext)
        }
    }

    val foregroundApp: ForegroundAppData = appDiscovery.foregroundApp
}