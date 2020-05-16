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

package uk.co.alt236.floatinginfo.overlay.writers;

import java.util.List;

import uk.co.alt236.floatinginfo.common.data.InfoStore;
import uk.co.alt236.floatinginfo.common.data.model.CpuData;
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;
import uk.co.alt236.floatinginfo.common.data.model.LocaleData;
import uk.co.alt236.floatinginfo.common.data.model.MemoryData;
import uk.co.alt236.floatinginfo.common.data.model.net.Interface;
import uk.co.alt236.floatinginfo.common.data.model.net.NetData;
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs;
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper;


public final class TextWriterWrapper implements TextWriter<InfoStore> {
    private final TextWriter<CpuData> mCpuTextWriter;
    private final TextWriter<ForegroundAppData> mFgProcessTextWriter;
    private final TextWriter<MemoryData> mMemoryTextWriter;
    private final TextWriter<NetData> mNetDataTextWriter;
    private final TextWriter<LocaleData> mLocaleDataWriter;
    private final TextWriter<List<Interface>> mInterfaceWriter;
    private final EnabledInfoPrefs mEnabledInfoPrefs;

    public TextWriterWrapper(EnabledInfoPrefs enabledInfoPrefs) {
        mEnabledInfoPrefs = enabledInfoPrefs;
        mCpuTextWriter = new CpuTextWriter();
        mMemoryTextWriter = new MemoryTextWriter(enabledInfoPrefs);
        mFgProcessTextWriter = new FgProcessTextWriter();
        mNetDataTextWriter = new NetDataTextWriter();
        mInterfaceWriter = new InterfaceWriter();
        mLocaleDataWriter = new LocaleDataTextWriter();
    }

    @Override
    public void writeText(final InfoStore input, final StringBuilderHelper sb) {

        if (input != null) {
            final ForegroundAppData procInfo = input.getForegroundProcessInfo();
            mFgProcessTextWriter.writeText(procInfo, sb);

            if (mEnabledInfoPrefs.isLocaleInfoEnabled()) {
                final LocaleData data = input.getLocaleData();
                mLocaleDataWriter.writeText(data, sb);
            }

            if (mEnabledInfoPrefs.isNetInfoEnabled()) {
                final NetData netData = input.getNetData();
                mNetDataTextWriter.writeText(netData, sb);

                if (mEnabledInfoPrefs.isIpInfoEnabled() && netData != null) {
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
