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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;

/**
 *
 */
/*package*/ abstract class FgAppDiscovery {
    protected static final String UNKNOWN_APP_NAME = "???";
    protected static final String UNKNOWN_PKG_NAME = "???";
    protected static final ForegroundAppData FALLBACK_APP_INFO
            = new ForegroundAppData(-1, UNKNOWN_PKG_NAME, UNKNOWN_APP_NAME);

    private final PackageManager mPackageManager;

    public FgAppDiscovery(final Context context) {
        mPackageManager = context.getApplicationContext().getPackageManager();
    }

    protected CharSequence getAppName(final String packageName) {
        ApplicationInfo ai;

        try {
            ai = mPackageManager.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }

        return (ai != null ? mPackageManager.getApplicationLabel(ai) : UNKNOWN_APP_NAME);
    }

    public abstract ForegroundAppData getForegroundApp();
}
