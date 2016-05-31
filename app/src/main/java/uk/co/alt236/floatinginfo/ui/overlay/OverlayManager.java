/*******************************************************************************
 * Copyright 2014 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uk.co.alt236.floatinginfo.ui.overlay;

import android.content.Context;
import android.view.View;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.NetData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

public class OverlayManager {
    private final InfoStore mInfoStore;
    private final TextOverlayController mTextOverlayController;

    private final TextWriter<CpuData> mCpuTextWriter;
    private final TextWriter<ForegroundAppData> mFgProcessTextWriter;
    private final TextWriter<MemoryData> mMemoryTextWriter;
    private final TextWriter<NetData> mNetDataTextWriter;

    public OverlayManager(final Context context, final InfoStore store) {
        mInfoStore = store;

        mTextOverlayController = new TextOverlayController(context);
        mCpuTextWriter = new CpuTextWriter();
        mMemoryTextWriter = new MemoryTextWriter();
        mFgProcessTextWriter = new FgProcessTextWriter();
        mNetDataTextWriter = new NetDataTextWriter();

        updateBackground();
        updateTextSize();
        updateTextColor();
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

            final NetData netData = mInfoStore.getNetData();
            mNetDataTextWriter.writeText(netData, sb);

            final CpuData cpuInfo = mInfoStore.getCpuInfo();
            mCpuTextWriter.writeText(cpuInfo, sb);

            final MemoryData memoryInfo = mInfoStore.getMemoryInfo();
            mMemoryTextWriter.writeText(memoryInfo, sb);
        }

        return sb.toCharSequence();
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

    public void updateTextSize() {
        mTextOverlayController.updateTextSize();
    }

    public void update() {
        mTextOverlayController.setText(getInfoText());
    }
}
