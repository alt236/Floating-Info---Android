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

package uk.co.alt236.floatinginfo.permissions;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class AndroidPermissionChecker implements PermissionChecker {

    private final Context mContext;

    public AndroidPermissionChecker(final Context context) {
        mContext = context.getApplicationContext();
    }

    private boolean hasAllPermissions(@NonNull String[] permissions) {
        boolean hasAllPermissions = true;
        for (String perm : permissions) {
            hasAllPermissions &= ActivityCompat.checkSelfPermission(mContext, perm)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return hasAllPermissions;
    }

    @NonNull
    public String[] getRequiredPermissions() {
        final String packageName = mContext.getPackageName();
        final PackageManager packageManager = mContext.getPackageManager();

        try {
            final String[] permissions;

            final PackageInfo info = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                permissions = info.requestedPermissions;
            } else {
                permissions = new String[0];
            }

            return permissions;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isNeeded() {
        final String[] permissions = getRequiredPermissions();
        return hasAllPermissions(permissions);
    }

}
