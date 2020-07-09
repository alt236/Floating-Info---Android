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

package uk.co.alt236.floatinginfo.common.data.model.bt

import android.bluetooth.BluetoothAdapter

enum class ScanMode(private val androidMode: Int) {
    NONE(BluetoothAdapter.SCAN_MODE_NONE),
    CONNECTABLE(BluetoothAdapter.SCAN_MODE_CONNECTABLE),
    CONNECTABLE_DISCOVERABLE(BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);

    companion object {
        fun fromAndroidState(androidMode: Int): ScanMode {
            return values().find { it.androidMode == androidMode } ?: NONE
        }
    }
}