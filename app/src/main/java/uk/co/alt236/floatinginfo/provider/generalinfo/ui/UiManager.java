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
package uk.co.alt236.floatinginfo.provider.generalinfo.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.provider.generalinfo.asynctask.ForegroundProcessInfo;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.util.PeakKeeper;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;
import uk.co.alt236.floatinginfo.util.Util;

public class UiManager {
    private final Context mContext;
    private final InfoStore mInfoStore;
    private final PeakKeeper mPeakKeeper;
    private final SharedPreferences mPrefs;
    private final TextView mTextView;

    public UiManager(Context context, InfoStore store) {
        mPeakKeeper = new PeakKeeper();
        mContext = context;
        mInfoStore = store;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mTextView = (TextView) generateView();
        setBackground();
        setTextSize();
        setTextColor();
    }

    public void clearPeakUsage() {
        mPeakKeeper.clear();
    }

    private void constructMemoryLine(StringBuilderHelper sb, String key, int value) {
        if (value >= 0) { // We assume negative values mean that something went
            // wrong with reflection.
            String valueStr = Util.getHumanReadableKiloByteCount(value, true);
            mPeakKeeper.update(key, value);
            final int peak = mPeakKeeper.getValue(key);
            if (peak > 0) {
                valueStr += " (Peak: " + Util.getHumanReadableKiloByteCount(peak, true) + ")";
            }

            sb.append(key, valueStr);
        }
    }

    private View generateView() {
        final LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return (TextView) inflator.inflate(R.layout.screen_overlay, null);
    }

    private CharSequence getInfoText() {
        final StringBuilderHelper sb = new StringBuilderHelper();

        if (mInfoStore != null) {
            final ForegroundProcessInfo procInfo = mInfoStore.getForegroundProcessInfo();
            if (procInfo != null) {
                sb.appendBold("Foreground Application Info");
                sb.startKeyValueSection();
                sb.append("App Name", String.valueOf(procInfo.getAppName()));
                sb.append("Package", procInfo.getPackage());
                sb.append("PID", procInfo.getPid());
                sb.endKeyValueSection();
                sb.appendNewLine();
            }

            final CpuData cpuInfo = mInfoStore.getCpuInfo();

            if (cpuInfo != null) {
                sb.appendBold("Global CPU Utilisation");
                sb.startKeyValueSection();
                sb.append("Total", String.valueOf(cpuInfo.getOveralCpu()) + "%");
                final List<Integer> list = cpuInfo.getPerCpuUtilisation();

                int count = 0;

                for (Integer value : list) {
                    sb.append("CPU" + count, String.valueOf(value) + "%");
                    count++;
                }
                sb.endKeyValueSection();
                sb.appendNewLine();
            }

            final MemoryData memoryInfo = mInfoStore.getMemoryInfo();

            if (memoryInfo != null) {
                sb.appendBold("Current Process Memory Utilisation");
                sb.startKeyValueSection();
                constructMemoryLine(
                        sb,
                        "DalvikPrivateClean",
                        memoryInfo.getDalvikPrivateClean());
                constructMemoryLine(
                        sb,
                        "DalvikPrivateDirty",
                        memoryInfo.getDalvikPrivateDirty());
                constructMemoryLine(
                        sb,
                        "DalvikPss",
                        memoryInfo.getDalvikPss());
                constructMemoryLine(
                        sb,
                        "DalvikSharedClean",
                        memoryInfo.getDalvikSharedClean());
                constructMemoryLine(
                        sb,
                        "DalvikSharedDirty",
                        memoryInfo.getDalvikSharedDirty());
                constructMemoryLine(
                        sb,
                        "DalvikSwappablePss",
                        memoryInfo.getDalvikSwappablePss());
                constructMemoryLine(
                        sb,
                        "DalvikSwappedOut",
                        memoryInfo.getDalvikSwappedOut());

                constructMemoryLine(
                        sb,
                        "NativePrivateClean",
                        memoryInfo.getNativePrivateClean());
                constructMemoryLine(
                        sb,
                        "NativePrivateDirty",
                        memoryInfo.getNativePrivateDirty());
                constructMemoryLine(
                        sb,
                        "NativePss",
                        memoryInfo.getNativePss());
                constructMemoryLine(
                        sb,
                        "NativeSharedClean",
                        memoryInfo.getNativeSharedClean());
                constructMemoryLine(
                        sb,
                        "NativeSharedDirty",
                        memoryInfo.getNativeSharedDirty());
                constructMemoryLine(
                        sb,
                        "NativeSwappablePss",
                        memoryInfo.getNativeSwappablePss());
                constructMemoryLine(
                        sb,
                        "NativeSwappedOut",
                        memoryInfo.getNativeSwappedOut());

                constructMemoryLine(
                        sb,
                        "OtherPrivateClean",
                        memoryInfo.getOtherPrivateClean());
                constructMemoryLine(
                        sb,
                        "OtherPrivateDirty",
                        memoryInfo.getOtherPrivateDirty());
                constructMemoryLine(
                        sb,
                        "OtherPss",
                        memoryInfo.getOtherPss());
                constructMemoryLine(
                        sb,
                        "OtherSharedClean",
                        memoryInfo.getOtherSharedClean());
                constructMemoryLine(
                        sb,
                        "OtherSharedDirty",
                        memoryInfo.getOtherSharedDirty());
                constructMemoryLine(
                        sb,
                        "OtherSwappablePss",
                        memoryInfo.getOtherSwappablePss());
                constructMemoryLine(
                        sb,
                        "OtherSwappedOut",
                        memoryInfo.getOtherSwappedOut());

                sb.endKeyValueSection();
                sb.appendNewLine();
            }
        }

        return sb.toCharSequence();
    }

    public CharSequence getSharePayload() {
        return getInfoText();
    }

    public View getView() {
        return mTextView;
    }

    public void setBackground() {
        final int v = mPrefs.getInt(mContext.getString(R.string.pref_bg_opacity), 0);
        final int level = 0;
        if (v > 0) {
            int a = (int) ((float) v / 100f * 255);
            mTextView.setBackgroundColor(Color.argb(a, level, level, level));
        } else {
            mTextView.setBackground(null);
        }
    }

    public void setTextColor() {
        final int alpha = mPrefs.getInt(
                mContext.getString(R.string.pref_text_opacity),
                mContext.getResources().getInteger(R.integer.default_text_opacity));
        final int red = mPrefs.getInt(
                mContext.getString(R.string.pref_text_color_red),
                mContext.getResources().getInteger(R.integer.default_text_red));
        final int green = mPrefs.getInt(
                mContext.getString(R.string.pref_text_color_green),
                mContext.getResources().getInteger(R.integer.default_text_green));
        final int blue = mPrefs.getInt(
                mContext.getString(R.string.pref_text_color_blue),
                mContext.getResources().getInteger(R.integer.default_text_blue));
        mTextView.setTextColor(Color.argb(alpha, red, green, blue));
        mTextView.setShadowLayer(1.5f, -1, 1, Color.DKGRAY);
    }

    public void setTextSize() {
        final int sp = 6 + mPrefs.getInt(
                mContext.getString(R.string.pref_text_size),
                0);
        mTextView.setTextSize(sp);
    }

    public void update() {
        mTextView.setText(getInfoText());
    }
}
