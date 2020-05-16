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
package uk.co.alt236.floatinginfo.common.data.model;

public class ForegroundAppData {
    private final int pid;
    private final String packageName;
    private final CharSequence appName;

    public ForegroundAppData(
            final int pid,
            final String pkg,
            final CharSequence appName) {

        this.pid = pid;
        packageName = pkg;
        this.appName = appName;
    }

    public int getPid() {
        return pid;
    }

    public String getPackage() {
        return packageName;
    }

    public CharSequence getAppName() {
        return appName;
    }

    @Override
    public String toString() {
        return "ForegroundProcessInfo [mPid=" + pid + ", mPackage=" + packageName + ", mAppName=" + appName + "]";
    }


}
