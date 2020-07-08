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

import uk.co.alt236.floatinginfo.common.data.model.CpuData
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper

/*package*/
internal class CpuTextWriter : TextWriter<CpuData> {

    override fun writeText(input: CpuData?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }
        sb.appendBold("Global CPU Utilisation")
        sb.startKeyValueSection()
        if (input.hasError) {
            sb.append("Error", "Failed to open " + input.statFile)
        } else {
            sb.append("Total", input.overallCpu.toString() + "%")
            val list = input.getPerCpuUtilisation()

            for ((count, value) in list.withIndex()) {
                sb.append("CPU$count", "$value%")
            }
        }
        sb.endKeyValueSection()
        sb.appendNewLine()
    }

    override fun clear() {
        // NOOP
    }
}