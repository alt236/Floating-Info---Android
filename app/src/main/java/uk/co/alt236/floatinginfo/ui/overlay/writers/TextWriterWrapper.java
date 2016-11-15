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

package uk.co.alt236.floatinginfo.ui.overlay.writers;

import java.util.List;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.Interface;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.NetData;
import uk.co.alt236.floatinginfo.data.prefs.EnabledInfoPrefs;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

public final class TextWriterWrapper implements TextWriter<InfoStore> {
    private final TextWriter<CpuData> mCpuTextWriter;
    private final TextWriter<ForegroundAppData> mFgProcessTextWriter;
    private final TextWriter<MemoryData> mMemoryTextWriter;
    private final TextWriter<NetData> mNetDataTextWriter;
    private final TextWriter<List<Interface>> mInterfaceWriter;
    private final EnabledInfoPrefs mEnabledInfoPrefs;

    public TextWriterWrapper(EnabledInfoPrefs enabledInfoPrefs) {
        mEnabledInfoPrefs = enabledInfoPrefs;
        mCpuTextWriter = new CpuTextWriter();
        mMemoryTextWriter = new MemoryTextWriter();
        mFgProcessTextWriter = new FgProcessTextWriter();
        mNetDataTextWriter = new NetDataTextWriter();
        mInterfaceWriter = new InterfaceWriter();
    }

    @Override
    public void writeText(final InfoStore input, final StringBuilderHelper sb) {

        if (input != null) {
            final ForegroundAppData procInfo = input.getForegroundProcessInfo();
            mFgProcessTextWriter.writeText(procInfo, sb);

            if (mEnabledInfoPrefs.isNetInfoEnabled()) {
                final NetData netData = input.getNetData();
                mNetDataTextWriter.writeText(netData, sb);

                if (mEnabledInfoPrefs.isIpInfoEnabled()) {
                    mInterfaceWriter.writeText(netData.getInterfaces(), sb);
                }
            }

            if (mEnabledInfoPrefs.isCpuInfoEnabled()) {
                final CpuData cpuInfo = input.getCpuInfo();
                mCpuTextWriter.writeText(cpuInfo, sb);
            }

            if (mEnabledInfoPrefs.isMemoryInfoEnabled()) {
                final MemoryData memoryInfo = input.getMemoryInfo();
                mMemoryTextWriter.writeText(memoryInfo, sb);
            }
        }
    }

    @Override
    public void clear() {
        mCpuTextWriter.clear();
        mMemoryTextWriter.clear();
        mFgProcessTextWriter.clear();
    }
}
