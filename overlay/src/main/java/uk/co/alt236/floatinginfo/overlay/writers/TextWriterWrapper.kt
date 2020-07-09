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

import uk.co.alt236.floatinginfo.common.data.InfoStore
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper
import uk.co.alt236.floatinginfo.overlay.writers.net.InterfaceWriter
import uk.co.alt236.floatinginfo.overlay.writers.net.NetDataTextWriter

class TextWriterWrapper(private val mEnabledInfoPrefs: EnabledInfoPrefs) : TextWriter<InfoStore?> {
    private val cpuTextWriter = CpuTextWriter()
    private val fgProcessTextWriter = FgProcessTextWriter()
    private val interfaceWriter = InterfaceWriter()
    private val localeDataWriter = LocaleDataTextWriter()
    private val memoryTextWriter = MemoryTextWriter(mEnabledInfoPrefs)
    private val netDataTextWriter = NetDataTextWriter()
    private val bluetoothTextWriter = BluetoothTextWriter()

    override fun writeText(input: InfoStore?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }

        writeProcessInfo(input, sb)
        writeLocaleInfo(input, sb)
        writeNetInfo(input, sb)
        writeBluetoothInfo(input, sb)
        writeCpuInfo(input, sb)
        writeMemoryInfo(input, sb)
    }

    override fun clear() {
        cpuTextWriter.clear()
        memoryTextWriter.clear()
        fgProcessTextWriter.clear()
    }

    private fun writeProcessInfo(input: InfoStore, sb: StringBuilderHelper) {
        val info = input.foregroundProcessInfo
        fgProcessTextWriter.writeText(info, sb)
    }

    private fun writeLocaleInfo(input: InfoStore, sb: StringBuilderHelper) {
        if (mEnabledInfoPrefs.isLocaleInfoEnabled) {
            val data = input.localeData
            localeDataWriter.writeText(data, sb)
        }
    }

    private fun writeNetInfo(input: InfoStore, sb: StringBuilderHelper) {
        if (mEnabledInfoPrefs.isNetInfoEnabled) {
            val netData = input.netData
            netDataTextWriter.writeText(netData, sb)
            if (mEnabledInfoPrefs.isIpInfoEnabled && netData != null) {
                interfaceWriter.writeText(netData.interfaces, sb)
            }
        }
    }

    private fun writeCpuInfo(input: InfoStore, sb: StringBuilderHelper) {
        if (mEnabledInfoPrefs.isCpuInfoEnabled) {
            val info = input.cpuInfo
            cpuTextWriter.writeText(info, sb)
        }
    }

    private fun writeMemoryInfo(input: InfoStore, sb: StringBuilderHelper) {
        if (mEnabledInfoPrefs.isMemoryInfoEnabled) {
            val info = input.memoryInfo
            memoryTextWriter.writeText(info, sb)
        }
    }

    private fun writeBluetoothInfo(input: InfoStore, sb: StringBuilderHelper) {
        if (mEnabledInfoPrefs.isBluetoothInfoEnabled) {
            val info = input.bluetoothInfo
            bluetoothTextWriter.writeText(info, sb)
        }
    }
}