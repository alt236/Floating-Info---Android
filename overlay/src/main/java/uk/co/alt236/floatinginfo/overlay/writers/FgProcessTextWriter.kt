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

import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper

/**
 *
 */
/*package*/
internal class FgProcessTextWriter : TextWriter<ForegroundAppData> {

    override fun writeText(input: ForegroundAppData?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }
        sb.appendBold("Foreground Application Info")
        sb.startKeyValueSection()
        sb.append("App Name", input.appName.toString())
        sb.append("Package", input.packageName)
        sb.append("PID", input.pid.toLong())
        sb.endKeyValueSection()
        sb.appendNewLine()
    }

    override fun clear() {
        // NOOP
    }
}