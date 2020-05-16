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
package uk.co.alt236.floatinginfo.common.data

import uk.co.alt236.floatinginfo.common.data.model.CpuData
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData
import uk.co.alt236.floatinginfo.common.data.model.LocaleData
import uk.co.alt236.floatinginfo.common.data.model.MemoryData
import uk.co.alt236.floatinginfo.common.data.model.net.NetData
import java.util.*

class InfoStore {
    private val mLock = Any()
    private val mStore: MutableMap<Key, Any?> = EnumMap(Key::class.java)

    val netData: NetData?
        get() = get(Key.NET_DATA) as NetData?

    val localeData: LocaleData?
        get() = get(Key.GENERAL) as LocaleData?

    val cpuInfo: CpuData?
        get() = get(Key.CPU_INFO) as CpuData?

    val foregroundProcessInfo: ForegroundAppData
        get() = get(Key.PROCESS_INFO) as ForegroundAppData

    val memoryInfo: MemoryData?
        get() = get(Key.MEMORY_INFO) as MemoryData?

    fun set(value: CpuData?) {
        put(Key.CPU_INFO, value)
    }

    fun set(value: LocaleData?) {
        put(Key.GENERAL, value)
    }

    fun set(value: ForegroundAppData?) {
        put(Key.PROCESS_INFO, value)
    }

    fun set(value: MemoryData?) {
        put(Key.MEMORY_INFO, value)
    }

    fun set(value: NetData?) {
        put(Key.NET_DATA, value)
    }

    private operator fun get(key: Key): Any? {
        synchronized(mLock) { return mStore[key] }
    }

    private fun put(key: Key, value: Any?) {
        synchronized(mLock) { mStore.put(key, value) }
    }

    private enum class Key {
        PROCESS_INFO, CPU_INFO, MEMORY_INFO, NET_DATA, GENERAL
    }
}