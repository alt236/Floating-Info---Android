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

package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.general;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LocaleData {

    private final Locale defaultLocale;
    private final List<Locale> contextLocales;

    public LocaleData(@NonNull Locale defaultLocale, List<Locale> contextLocales) {
        this.defaultLocale = defaultLocale;
        this.contextLocales = contextLocales;
    }

    @Nullable
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Nullable
    public List<Locale> getContextLocales() {
        return contextLocales;
    }
}
