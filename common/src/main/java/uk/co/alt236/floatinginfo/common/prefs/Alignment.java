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

package uk.co.alt236.floatinginfo.common.prefs;

public enum Alignment {
    TOP_LEFT("TOP_LEFT"),
    TOP_CENTER("TOP_CENTER"),
    TOP_RIGHT("TOP_RIGHT"),
    CENTER_LEFT("CENTER_LEFT"),
    CENTER_CENTER("CENTER_CENTER"),
    CENTER_RIGHT("CENTER_RIGHT"),
    BOTTOM_LEFT("BOTTOM_LEFT"),
    BOTTOM_CENTER("BOTTOM_CENTER"),
    BOTTOM_RIGHT("BOTTOM_RIGHT");

    private final String key;

    Alignment(final String key) {
        this.key = key;
    }

    public static Alignment fromString(final String value) {
        Alignment retVal = null;

        for (final Alignment alignment : Alignment.values()) {
            if (alignment.key.equalsIgnoreCase(value)) {
                retVal = alignment;
                break;
            }
        }
        return retVal;
    }
}
