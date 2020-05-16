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

package uk.co.alt236.floatinginfo.overlay.writers;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import uk.co.alt236.floatinginfo.common.data.model.LocaleData;
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper;

/**
 *
 */
/*package*/ class LocaleDataTextWriter implements TextWriter<LocaleData> {

    @Override
    public void writeText(final LocaleData input, @NonNull final StringBuilderHelper sb) {
        if (input != null) {
            sb.appendBold("Locale Info");
            sb.startKeyValueSection();
            sb.append("Def Locale", String.valueOf(input.getDefaultLocale()));
            sb.append("Context Locale", toString(input.getContextLocales()));

            sb.endKeyValueSection();
            sb.appendNewLine();
        }
    }

    private String toString(final List<Locale> contextLocales) {
        final StringBuilder sb = new StringBuilder();

        if (contextLocales == null || contextLocales.isEmpty()) {
            sb.append("<null>");
        } else {
            for (int i = 0; i < contextLocales.size(); i++) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(contextLocales.get(i));
            }
        }

        return sb.toString();
    }

    @Override
    public void clear() {
        // NOOP
    }

}
