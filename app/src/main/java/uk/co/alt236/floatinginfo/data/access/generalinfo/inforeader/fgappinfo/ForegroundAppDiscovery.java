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

package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo;

import android.content.Context;

import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;

public class ForegroundAppDiscovery {

    private final FgAppDiscovery mAppDiscovery;

    public ForegroundAppDiscovery(final Context context) {
        final Context appContext = context.getApplicationContext();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mAppDiscovery = new FgAppDiscovery21(appContext);
        } else {
            mAppDiscovery = new FgAppDiscoveryLegacy(appContext);
        }

    }

    public ForegroundAppData getForegroundApp() {
        return mAppDiscovery.getForegroundApp();
    }
}
