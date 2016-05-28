/*******************************************************************************
 * Copyright 2014 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uk.co.alt236.floatinginfo.util;

import java.util.Locale;

public class Util {


    public static String getHumanReadableKiloByteCount(long kbytes, boolean si) {
        return getHumanReadableByteCount(kbytes * 1024, si);
    }

    public static String getHumanReadableByteCount(long bytes, boolean si) {
        final int unit = si ? 1000 : 1024;

        if (bytes < unit) {
            return bytes + " B";
        }

        final int exp = (int) (Math.log(bytes) / Math.log(unit));
        final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
