/*
 * Copyright 2017 Alexandros Schillings
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
package uk.co.alt236.floatinginfo.inforeader.general

import android.content.Context
import android.os.Build
import uk.co.alt236.floatinginfo.common.data.model.LocaleData
import java.util.*

class LocaleInfoReader(private val context: Context) {
    var data: LocaleData? = null
        private set

    override fun toString(): String {
        val buf = StringBuilder()

        data?.let {
            buf.append("Default Locale : ")
            buf.append(it.defaultLocale)
        }

        return buf.toString()
    }

    fun update() {
        val defaultLocale = Locale.getDefault()
        data = LocaleData(
                defaultLocale,
                localeList)
    }

    private val localeList: List<Locale>
        get() {
            val retVal: MutableList<Locale> = ArrayList()
            val configuration = context.resources.configuration

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = configuration.locales
                for (i in 0 until localeList.size()) {
                    retVal.add(localeList[i])
                }
            } else {
                val locale = configuration.locale
                retVal.add(locale)
            }

            return retVal
        }

}