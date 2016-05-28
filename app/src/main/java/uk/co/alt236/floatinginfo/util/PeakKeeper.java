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

import java.util.HashMap;
import java.util.Map;

public class PeakKeeper {

    private final Object mLock = new Object();
    private final Map<String, Integer> mStore = new HashMap<String, Integer>();

    public void clear() {
        synchronized (mLock) {
            mStore.clear();
        }
    }

    public int getValue(String key) {
        synchronized (mLock) {
            final Integer res = mStore.get(key);
            if (res == null) {
                return 0;
            } else {
                return res.intValue();
            }
        }
    }

    public void update(String key, int value) {
        synchronized (mLock) {
            final Integer stored = mStore.get(key);
            final int tmp;

            if (stored == null) {
                tmp = -1;
            } else {
                tmp = stored.intValue();
            }

            if (value > tmp) {
                mStore.put(key, value);
            }
        }
    }

}
