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
package uk.co.alt236.floatinginfo.overlay.writers

import android.os.Build
import uk.co.alt236.floatinginfo.common.data.model.MemoryData
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs
import uk.co.alt236.floatinginfo.common.string.HumanReadable.getHumanReadableKiloByteCount
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper
import java.util.*

/**
 *
 */
/*package*/
internal class MemoryTextWriter(private val enabledInfoPrefs: EnabledInfoPrefs) : TextWriter<MemoryData> {
    private val mPeakKeeper = PeakKeeper()

    override fun writeText(input: MemoryData?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }

        sb.appendBold("Current Process Memory Utilisation")
        if (input.pid < 0) {
            sb.appendBold("E: Invalid PID")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sb.appendBold("E: This is a Nougat+ restriction.")
            }
        } else {
            sb.startKeyValueSection()
            constructMemoryLine(
                    sb,
                    "DalvikPrivateClean",
                    input.dalvikPrivateClean)
            constructMemoryLine(
                    sb,
                    "DalvikPrivateDirty",
                    input.dalvikPrivateDirty)
            constructMemoryLine(
                    sb,
                    "DalvikPss",
                    input.dalvikPss)
            constructMemoryLine(
                    sb,
                    "DalvikSharedClean",
                    input.dalvikSharedClean)
            constructMemoryLine(
                    sb,
                    "DalvikSharedDirty",
                    input.dalvikSharedDirty)
            constructMemoryLine(
                    sb,
                    "DalvikSwappablePss",
                    input.dalvikSwappablePss)
            constructMemoryLine(
                    sb,
                    "DalvikSwappedOut",
                    input.dalvikSwappedOut)
            constructMemoryLine(
                    sb,
                    "NativePrivateClean",
                    input.nativePrivateClean)
            constructMemoryLine(
                    sb,
                    "NativePrivateDirty",
                    input.nativePrivateDirty)
            constructMemoryLine(
                    sb,
                    "NativePss",
                    input.nativePss)
            constructMemoryLine(
                    sb,
                    "NativeSharedClean",
                    input.nativeSharedClean)
            constructMemoryLine(
                    sb,
                    "NativeSharedDirty",
                    input.nativeSharedDirty)
            constructMemoryLine(
                    sb,
                    "NativeSwappablePss",
                    input.nativeSwappablePss)
            constructMemoryLine(
                    sb,
                    "NativeSwappedOut",
                    input.nativeSwappedOut)
            constructMemoryLine(
                    sb,
                    "OtherPrivateClean",
                    input.otherPrivateClean)
            constructMemoryLine(
                    sb,
                    "OtherPrivateDirty",
                    input.otherPrivateDirty)
            constructMemoryLine(
                    sb,
                    "OtherPss",
                    input.otherPss)
            constructMemoryLine(
                    sb,
                    "OtherSharedClean",
                    input.otherSharedClean)
            constructMemoryLine(
                    sb,
                    "OtherSharedDirty",
                    input.otherSharedDirty)
            constructMemoryLine(
                    sb,
                    "OtherSwappablePss",
                    input.otherSwappablePss)
            constructMemoryLine(
                    sb,
                    "OtherSwappedOut",
                    input.otherSwappedOut)
            sb.endKeyValueSection()
        }
        sb.appendNewLine()
    }

    override fun clear() {
        mPeakKeeper.clear()
    }

    private fun constructMemoryLine(sb: StringBuilderHelper, key: String, value: Int) {
        val showZero = enabledInfoPrefs.showZeroMemoryItems()
        val showItem: Boolean

        showItem = when {
            showZero -> value >= 0
            value > 0 -> true
            else -> mPeakKeeper.getValue(key) > 0
        }

        if (!showItem) { // We assume negative values mean that something went wrong with reflection.
            return
        }

        var valueStr = getHumanReadableKiloByteCount(value.toLong(), true)
        mPeakKeeper.update(key, value)
        val peak = mPeakKeeper.getValue(key)
        if (peak > 0) {
            valueStr += " (Peak: " + getHumanReadableKiloByteCount(peak.toLong(), true) + ")"
        }
        sb.append(key, valueStr)
    }

    private class PeakKeeper {
        private val mLock = Any()
        private val mStore: MutableMap<String, Int> = HashMap()
        fun clear() {
            synchronized(mLock) { mStore.clear() }
        }

        fun getValue(key: String?): Int {
            synchronized(mLock) {
                val res = mStore[key]
                return res ?: 0
            }
        }

        fun update(key: String, value: Int) {
            synchronized(mLock) {
                val stored = mStore[key]
                val tmp: Int
                tmp = stored ?: -1
                if (value > tmp) {
                    mStore[key] = value
                }
            }
        }
    }
}