package uk.co.alt236.floatinginfo.ui.overlay;

import java.util.HashMap;
import java.util.Map;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;
import uk.co.alt236.floatinginfo.util.Util;

/**
 *
 */
/*package*/ class MemoryTextWriter implements TextWriter<MemoryData> {
    private final PeakKeeper mPeakKeeper;

    public MemoryTextWriter() {
        mPeakKeeper = new PeakKeeper();
    }

    @Override
    public void writeText(final MemoryData input, final StringBuilderHelper sb) {
        if (input != null) {
            sb.appendBold("Current Process Memory Utilisation");
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
            sb.appendNewLine();
        }
    }

    @Override
    public void clear() {
        mPeakKeeper.clear();
    }

    private void constructMemoryLine(final StringBuilderHelper sb, final String key, final int value) {
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
                    return res.intValue();
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
                    tmp = stored.intValue();
                }

                if (value > tmp) {
                    mStore.put(key, value);
                }
            }
        }
    }
}
