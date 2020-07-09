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

import android.content.Context
import android.os.AsyncTask
import androidx.annotation.CallSuper
import uk.co.alt236.floatinginfo.common.data.model.CpuData
import uk.co.alt236.floatinginfo.common.data.model.LocaleData
import uk.co.alt236.floatinginfo.common.data.model.MemoryData
import uk.co.alt236.floatinginfo.common.data.model.bt.BluetoothData
import uk.co.alt236.floatinginfo.common.data.model.net.NetData
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs
import uk.co.alt236.floatinginfo.data.access.generalinfo.monitortask.ProcessMonitor.UpdateCallback
import uk.co.alt236.floatinginfo.inforeader.bt.BluetoothInfoReader
import uk.co.alt236.floatinginfo.inforeader.cpu.CpuUtilisationReader
import uk.co.alt236.floatinginfo.inforeader.fgappinfo.ForegroundAppDiscovery
import uk.co.alt236.floatinginfo.inforeader.general.LocaleInfoReader
import uk.co.alt236.floatinginfo.inforeader.memory.MemoryInfoReader
import uk.co.alt236.floatinginfo.inforeader.network.NetDataReader
import uk.co.alt236.floatinginfo.util.Constants

internal class InnerTask(context: Context, private val mEnabledInfoPrefs: EnabledInfoPrefs, private val callback: UpdateCallback) : AsyncTask<Void, Update, Void?>() {
    private val mForegroundAppDiscovery: ForegroundAppDiscovery = ForegroundAppDiscovery(context)
    private val mNetDataReader: NetDataReader = NetDataReader(context)
    private val mLocaleInfoReader: LocaleInfoReader = LocaleInfoReader(context)
    private val mCpuUtilisationReader: CpuUtilisationReader = CpuUtilisationReader()
    private val mMemoryInfoReader: MemoryInfoReader = MemoryInfoReader(context)
    private val bluetoothInfoReader = BluetoothInfoReader(context)

    override fun doInBackground(vararg voids: Void): Void? {
        while (!isCancelled) {
            val appData = mForegroundAppDiscovery.getForegroundAppData()
            publishProgress(
                    Update(
                            appData,
                            netData,
                            getMemoryData(appData.pid),
                            cpuData,
                            localeData,
                            bluetoothData))
            if (!isCancelled) {
                try {
                    Thread.sleep(Constants.PROC_MONITOR_SLEEP.toLong())
                } catch (e: InterruptedException) {
                    // NOOP
                }
            }
        }
        return null
    }

    private val netData: NetData?
        get() {
            return if (mEnabledInfoPrefs.isNetInfoEnabled) {
                mNetDataReader.update()
                mNetDataReader.netData
            } else {
                null
            }
        }

    private fun getMemoryData(pid: Int): MemoryData? {
        return if (mEnabledInfoPrefs.isMemoryInfoEnabled) {
            mMemoryInfoReader.update(pid)
            mMemoryInfoReader.info
        } else {
            null
        }
    }

    private val cpuData: CpuData?
        get() {
            return if (mEnabledInfoPrefs.isCpuInfoEnabled) {
                mCpuUtilisationReader.update()
                mCpuUtilisationReader.cpuInfo
            } else {
                null
            }
        }

    private val localeData: LocaleData?
        get() {
            return if (mEnabledInfoPrefs.isLocaleInfoEnabled) {
                mLocaleInfoReader.update()
                mLocaleInfoReader.data
            } else {
                null
            }
        }

    private val bluetoothData: BluetoothData?
        get() {
            return if (mEnabledInfoPrefs.isBluetoothInfoEnabled) {
                bluetoothInfoReader.update()
                bluetoothInfoReader.data
            } else {
                null
            }
        }

    @CallSuper
    override fun onProgressUpdate(vararg values: Update) {
        callback.onUpdate(values[0])
    }
}