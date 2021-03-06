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
package uk.co.alt236.floatinginfo.data.access.generalinfo.monitortask

import uk.co.alt236.floatinginfo.common.data.model.CpuData
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData
import uk.co.alt236.floatinginfo.common.data.model.LocaleData
import uk.co.alt236.floatinginfo.common.data.model.MemoryData
import uk.co.alt236.floatinginfo.common.data.model.bt.BluetoothData
import uk.co.alt236.floatinginfo.common.data.model.net.NetData

data class Update(val foregroundAppData: ForegroundAppData,
                  val netData: NetData?,
                  val memoryData: MemoryData?,
                  val cpuData: CpuData?,
                  val generalData: LocaleData?,
                  val bluetoothData: BluetoothData?)