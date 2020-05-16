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

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.data.prefs.EnabledInfoPrefs;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;
import uk.co.alt236.floatinginfo.util.Util;

/**
 *
 */
/*package*/ class MemoryTextWriter implements TextWriter<MemoryData> {
    private final PeakKeeper mPeakKeeper;
    private final EnabledInfoPrefs mEnabledInfo;

    public MemoryTextWriter(final EnabledInfoPrefs enabledInfoPrefs) {
        mPeakKeeper = new PeakKeeper();
        mEnabledInfo = enabledInfoPrefs;
    }

    @Override
    public void writeText(final MemoryData input, @NonNull final StringBuilderHelper sb) {
        if (input != null) {
            sb.appendBold("Current Process Memory Utilisation");

            if (input.getPid() < 0) {
                sb.appendBold("E: Invalid PID");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    sb.appendBold("E: Since Nougat apps can no longer get the PID of other apps");
                }
            } else {
                sb.startKeyValueSection();
                constructMemoryLine(
                        sb,
                        "DalvikPrivateClean",
                        input.getDalvikPrivateClean());
                constructMemoryLine(
                        sb,
                        "DalvikPrivateDirty",
                        input.getDalvikPrivateDirty());
                constructMemoryLine(
                        sb,
                        "DalvikPss",
                        input.getDalvikPss());
                constructMemoryLine(
                        sb,
                        "DalvikSharedClean",
                        input.getDalvikSharedClean());
                constructMemoryLine(
                        sb,
                        "DalvikSharedDirty",
                        input.getDalvikSharedDirty());
                constructMemoryLine(
                        sb,
                        "DalvikSwappablePss",
                        input.getDalvikSwappablePss());
                constructMemoryLine(
                        sb,
                        "DalvikSwappedOut",
                        input.getDalvikSwappedOut());

                constructMemoryLine(
                        sb,
                        "NativePrivateClean",
                        input.getNativePrivateClean());
                constructMemoryLine(
                        sb,
                        "NativePrivateDirty",
                        input.getNativePrivateDirty());
                constructMemoryLine(
                        sb,
                        "NativePss",
                        input.getNativePss());
                constructMemoryLine(
                        sb,
                        "NativeSharedClean",
                        input.getNativeSharedClean());
                constructMemoryLine(
                        sb,
                        "NativeSharedDirty",
                        input.getNativeSharedDirty());
                constructMemoryLine(
                        sb,
                        "NativeSwappablePss",
                        input.getNativeSwappablePss());
                constructMemoryLine(
                        sb,
                        "NativeSwappedOut",
                        input.getNativeSwappedOut());

                constructMemoryLine(
                        sb,
                        "OtherPrivateClean",
                        input.getOtherPrivateClean());
                constructMemoryLine(
                        sb,
                        "OtherPrivateDirty",
                        input.getOtherPrivateDirty());
                constructMemoryLine(
                        sb,
                        "OtherPss",
                        input.getOtherPss());
                constructMemoryLine(
                        sb,
                        "OtherSharedClean",
                        input.getOtherSharedClean());
                constructMemoryLine(
                        sb,
                        "OtherSharedDirty",
                        input.getOtherSharedDirty());
                constructMemoryLine(
                        sb,
                        "OtherSwappablePss",
                        input.getOtherSwappablePss());
                constructMemoryLine(
                        sb,
                        "OtherSwappedOut",
                        input.getOtherSwappedOut());

                sb.endKeyValueSection();
            }
            sb.appendNewLine();
        }
    }

    @Override
    public void clear() {
        mPeakKeeper.clear();
    }

    private void constructMemoryLine(final StringBuilderHelper sb, final String key, final int value) {
        final boolean showZero = mEnabledInfo.showZeroMemoryItems();

        final boolean showItem;
        if (showZero) {
            showItem = value >= 0;
        } else if (value > 0) {
            showItem = true;
        } else {
            showItem = mPeakKeeper.getValue(key) > 0;
        }

        if (showItem) { // We assume negative values mean that something went
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


    private static class PeakKeeper {

        private final Object mLock = new Object();
        private final Map<String, Integer> mStore = new HashMap<String, Integer>();

        public void clear() {
            synchronized (mLock) {
                mStore.clear();
            }
        }

        public int getValue(final String key) {
            synchronized (mLock) {
                final Integer res = mStore.get(key);
                if (res == null) {
                    return 0;
                } else {
                    return res;
                }
            }
        }

        public void update(final String key, final int value) {
            synchronized (mLock) {
                final Integer stored = mStore.get(key);
                final int tmp;

                if (stored == null) {
                    tmp = -1;
                } else {
                    tmp = stored;
                }

                if (value > tmp) {
                    mStore.put(key, value);
                }
            }
        }
    }
}
