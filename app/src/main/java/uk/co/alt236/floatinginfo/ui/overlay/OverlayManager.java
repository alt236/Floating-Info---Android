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
package uk.co.alt236.floatinginfo.ui.overlay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.Interface;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.NetData;
import uk.co.alt236.floatinginfo.data.prefs.EnabledInfo;
import uk.co.alt236.floatinginfo.data.prefs.OverlayPrefs;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

public class OverlayManager {
    private final InfoStore mInfoStore;
    private final TextOverlayController mTextOverlayController;
    private final EnabledInfo mEnabledInfo;

    private final TextWriter<CpuData> mCpuTextWriter;
    private final TextWriter<ForegroundAppData> mFgProcessTextWriter;
    private final TextWriter<MemoryData> mMemoryTextWriter;
    private final TextWriter<NetData> mNetDataTextWriter;
    private final TextWriter<List<Interface>> mInterfaceWriter;

    public OverlayManager(final Context context,
                          final InfoStore store) {
        mInfoStore = store;
        mEnabledInfo = new EnabledInfo(context);

        mCpuTextWriter = new CpuTextWriter();
        mMemoryTextWriter = new MemoryTextWriter();
        mFgProcessTextWriter = new FgProcessTextWriter();
        mNetDataTextWriter = new NetDataTextWriter();
        mInterfaceWriter = new InterfaceWriter();

        final OverlayPrefs prefs = new OverlayPrefs(context);
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        mTextOverlayController = new TextOverlayController(layoutInflater, prefs);

        updateBackground();
        updateTextSize();
        updateTextColor();
        updateAlignment();
    }

    public void clearPeakUsage() {
        mCpuTextWriter.clear();
        mMemoryTextWriter.clear();
        mFgProcessTextWriter.clear();
    }

    private CharSequence getInfoText() {
        final StringBuilderHelper sb = new StringBuilderHelper();

        if (mInfoStore != null) {
            final ForegroundAppData procInfo = mInfoStore.getForegroundProcessInfo();
            mFgProcessTextWriter.writeText(procInfo, sb);

            if (mEnabledInfo.isNetInfoEnabled()) {
                final NetData netData = mInfoStore.getNetData();
                mNetDataTextWriter.writeText(netData, sb);

                if (mEnabledInfo.isIpInfoEnabled()) {
                    mInterfaceWriter.writeText(netData.getInterfaces(), sb);
                }
            }

            if (mEnabledInfo.isCpuInfoEnabled()) {
                final CpuData cpuInfo = mInfoStore.getCpuInfo();
                mCpuTextWriter.writeText(cpuInfo, sb);
            }

            if (mEnabledInfo.isMemoryInfoEnabled()) {
                final MemoryData memoryInfo = mInfoStore.getMemoryInfo();
                mMemoryTextWriter.writeText(memoryInfo, sb);
            }
        }

        return sb.toString().trim();
    }

    public CharSequence getSharePayload() {
        return getInfoText();
    }

    public View getView() {
        return mTextOverlayController.getView();
    }

    public void updateBackground() {
        mTextOverlayController.updateBackground();
    }

    public void updateTextColor() {
        mTextOverlayController.updateTextColor();
    }

    public void updateAlignment() {
        mTextOverlayController.updateAlignment();
    }

    public void updateTextSize() {
        mTextOverlayController.updateTextSize();
    }

    public void update() {
        mTextOverlayController.setText(getInfoText());
    }
}
