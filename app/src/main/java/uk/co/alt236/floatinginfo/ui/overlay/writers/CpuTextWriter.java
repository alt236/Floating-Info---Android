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

package uk.co.alt236.floatinginfo.ui.overlay.writers;

import java.util.List;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/*package*/ class CpuTextWriter implements TextWriter<CpuData> {
    @Override
    public void writeText(final CpuData input, final StringBuilderHelper sb) {
        if (input != null) {
            sb.appendBold("Global CPU Utilisation");
            sb.startKeyValueSection();
            sb.append("Total", String.valueOf(input.getOveralCpu()) + "%");
            final List<Integer> list = input.getPerCpuUtilisation();

            int count = 0;

            for (final Integer value : list) {
                sb.append("CPU" + count, String.valueOf(value) + "%");
                count++;
            }
            sb.endKeyValueSection();
            sb.appendNewLine();
        }
    }

    @Override
    public void clear() {
        // NOOP
    }
}
