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
package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.general.LocaleData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.NetData;

public class InfoStore {
    private final Object mLock = new Object();

    private final Map<Key, Object> mStore = new HashMap<Key, Object>();

    @Nullable
    public NetData getNetData() {
        return (NetData) get(Key.NET_DATA);
    }

    @Nullable
    public LocaleData getLocaleData() {
        return (LocaleData) get(Key.GENERAL);
    }

    @Nullable
    public CpuData getCpuInfo() {
        return (CpuData) get(Key.CPU_INFO);
    }

    @NonNull
    public ForegroundAppData getForegroundProcessInfo() {
        return (ForegroundAppData) get(Key.PROCESS_INFO);
    }

    @Nullable
    public MemoryData getMemoryInfo() {
        return (MemoryData) get(Key.MEMORY_INFO);
    }

    public void set(final CpuData value) {
        put(Key.CPU_INFO, value);
    }

    public void set(final LocaleData value) {
        put(Key.GENERAL, value);
    }

    public void set(final ForegroundAppData value) {
        put(Key.PROCESS_INFO, value);
    }

    public void set(final MemoryData value) {
        put(Key.MEMORY_INFO, value);
    }

    public void set(final NetData value) {
        put(Key.NET_DATA, value);
    }

    private Object get(final Key key) {
        synchronized (mLock) {
            return mStore.get(key);
        }
    }

    private void put(final Key key, final Object value) {
        synchronized (mLock) {
            mStore.put(key, value);
        }
    }

    private enum Key {
        PROCESS_INFO,
        CPU_INFO,
        MEMORY_INFO,
        NET_DATA,
        GENERAL
    }
}
