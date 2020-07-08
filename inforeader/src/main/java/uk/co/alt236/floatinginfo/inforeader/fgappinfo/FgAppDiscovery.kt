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
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData

/**
 *
 */
/*package*/
internal abstract class FgAppDiscovery(context: Context) {
    private val mPackageManager: PackageManager = context.applicationContext.packageManager

    protected fun getAppName(packageName: String?): CharSequence {
        val ai: ApplicationInfo? = try {
            mPackageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }

        return if (ai != null) mPackageManager.getApplicationLabel(ai) else UNKNOWN_APP_NAME
    }

    abstract fun getForegroundAppData(): ForegroundAppData

    internal companion object {
        const val UNKNOWN_APP_NAME = "???"
        const val UNKNOWN_PKG_NAME = "???"

        @JvmStatic
        val FALLBACK_APP_INFO = ForegroundAppData(-1, UNKNOWN_PKG_NAME, UNKNOWN_APP_NAME)
    }

}