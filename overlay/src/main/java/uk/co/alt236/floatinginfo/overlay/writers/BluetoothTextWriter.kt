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

import uk.co.alt236.floatinginfo.common.data.model.bt.BluetoothData
import uk.co.alt236.floatinginfo.common.data.model.bt.LightBluetoothDevice
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper

/**
 *
 */
/*package*/
internal class BluetoothTextWriter : TextWriter<BluetoothData> {
    override fun writeText(input: BluetoothData?, sb: StringBuilderHelper) {
        if (input == null) {
            return
        }

        sb.appendBold("Bluetooth Info")
        sb.startKeyValueSection()
        sb.append("Enabled", input.enabled)
        //sb.append("Address", input.address.pretty())
        sb.append("Scan Mode", input.scanMode.toString())
        sb.endKeyValueSection()
        //addCurrentDevices(sb, input.bondedDevices)
        sb.appendNewLine()
    }

    private fun addCurrentDevices(sb: StringBuilderHelper, devices: List<LightBluetoothDevice>) {
        if (devices.isEmpty()) {
            return;
        }
        sb.appendBold("Current Devices")
        sb.startKeyValueSection()
        for (device in devices) {
            sb.append(device.name.pretty(), device.address.pretty())
        }
        sb.endKeyValueSection()
    }

    private fun String?.pretty(): String {
        return if (this.isNullOrBlank()) {
            "n/a"
        } else {
            this
        }
    }

    override fun clear() {
        // NOOP
    }
}