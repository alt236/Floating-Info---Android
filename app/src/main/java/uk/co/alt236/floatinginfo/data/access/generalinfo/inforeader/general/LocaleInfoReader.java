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

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import uk.co.alt236.floatinginfo.common.data.model.LocaleData;

public class LocaleInfoReader {
    private final Context context;
    private LocaleData data;

    public LocaleInfoReader(final Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        if (data != null) {
            buf.append("Default Locale : ");
            buf.append(data.getDefaultLocale());
        }
        return buf.toString();
    }

    public LocaleData getData() {
        return data;
    }

    public void update() {
        final Locale defaultLocale = Locale.getDefault();
        data = new LocaleData(
                defaultLocale,
                getLocaleList());
    }

    @NonNull
    private List<Locale> getLocaleList() {
        final List<Locale> retVal = new ArrayList<>();

        final Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final LocaleList localeList = configuration.getLocales();

            for (int i = 0; i < localeList.size(); i++) {
                retVal.add(localeList.get(i));
            }
        } else {
            final Locale locale = configuration.locale;
            retVal.add(locale);
        }

        return retVal;
    }
}
