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
package uk.co.alt236.floatinginfo.overlay.writers

import uk.co.alt236.floatinginfo.common.data.model.LocaleData
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper
import java.util.*

/**
 *
 */
/*package*/
internal class LocaleDataTextWriter : TextWriter<LocaleData> {
    override fun writeText(input: LocaleData?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }
        sb.appendBold("Locale Info")
        sb.startKeyValueSection()
        sb.append("Def Locale", input.defaultLocale.toString())
        sb.append("Context Locale", toString(input.contextLocales))
        sb.endKeyValueSection()
        sb.appendNewLine()
    }

    private fun toString(contextLocales: List<Locale>?): String {
        val sb = StringBuilder()
        if (contextLocales == null || contextLocales.isEmpty()) {
            sb.append("<null>")
        } else {
            for (i in contextLocales.indices) {
                if (sb.length > 0) {
                    sb.append(", ")
                }
                sb.append(contextLocales[i])
            }
        }
        return sb.toString()
    }

    override fun clear() {
        // NOOP
    }
}