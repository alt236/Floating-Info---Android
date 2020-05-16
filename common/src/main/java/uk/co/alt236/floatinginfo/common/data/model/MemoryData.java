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
package uk.co.alt236.floatinginfo.common.data.model;

import android.os.Debug.MemoryInfo;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MemoryData {
    private final static Set<String> sReflectionErrorKeys = new HashSet<String>();

    /** The private clean pages used by dalvik heap. */
    /** @hide We may want to expose this, eventually. */
    private final int dalvikPrivateClean;

    /** The private dirty pages used by dalvik heap. */
    private final int dalvikPrivateDirty;
    /**
     * The proportional set size for dalvik heap. (Doesn't include other Dalvik
     * overhead.)
     */
    private final int dalvikPss;
    /** The shared clean pages used by dalvik heap. */
    /** @hide We may want to expose this, eventually. */
    private final int dalvikSharedClean;
    /** The shared dirty pages used by dalvik heap. */
    private final int dalvikSharedDirty;
    /** The proportional set size that is swappable for dalvik heap. */
    /** @hide We may want to expose this, eventually. */
    private final int dalvikSwappablePss;
    /** The dirty dalvik pages that have been swapped out. */
    /** @hide We may want to expose this, eventually. */
    private final int dalvikSwappedOut;

    /** The private clean pages used by the native heap. */
    /** @hide We may want to expose this, eventually. */
    private final int nativePrivateClean;
    /** The private dirty pages used by the native heap. */
    private final int nativePrivateDirty;
    /** The proportional set size for the native heap. */
    private final int nativePss;
    /** The shared clean pages used by the native heap. */
    /** @hide We may want to expose this, eventually. */
    private final int nativeSharedClean;
    /** The shared dirty pages used by the native heap. */
    private final int nativeSharedDirty;
    /** The proportional set size that is swappable for the native heap. */
    /** @hide We may want to expose this, eventually. */
    private final int nativeSwappablePss;
    /** The dirty native pages that have been swapped out. */
    /** @hide We may want to expose this, eventually. */
    private final int nativeSwappedOut;

    /** The private clean pages used by everything else. */
    /** @hide We may want to expose this, eventually. */
    private final int otherPrivateClean;
    /** The private dirty pages used by everything else. */
    private final int otherPrivateDirty;
    /** The proportional set size for everything else. */
    private final int otherPss;
    /** The shared clean pages used by everything else. */
    /** @hide We may want to expose this, eventually. */
    private final int otherSharedClean;
    /** The shared dirty pages used by everything else. */
    private final int otherSharedDirty;
    /** The proportional set size that is swappable for everything else. */
    /** @hide We may want to expose this, eventually. */
    private final int otherSwappablePss;
    /** The dirty pages used by anyting else that have been swapped out. */
    /** @hide We may want to expose this, eventually. */
    private final int otherSwappedOut;
    private final int pid;

    public MemoryData(final int pid, final MemoryInfo mi) {
        this.pid = pid;
        dalvikPrivateClean = getIntReflectively(mi, "dalvikPrivateClean");
        dalvikPrivateDirty = mi.dalvikPrivateDirty;
        dalvikPss = mi.dalvikPss;
        dalvikSharedClean = getIntReflectively(mi, "dalvikSharedClean");
        dalvikSharedDirty = mi.dalvikSharedDirty;
        dalvikSwappablePss = getIntReflectively(mi, "dalvikSwappablePss");
        dalvikSwappedOut = getIntReflectively(mi, "dalvikSwappedOut");

        nativePrivateClean = getIntReflectively(mi, "nativePrivateClean");
        nativePrivateDirty = mi.nativePrivateDirty;
        nativePss = mi.nativePss;
        nativeSharedClean = getIntReflectively(mi, "nativeSharedClean");
        nativeSharedDirty = mi.nativeSharedDirty;
        nativeSwappablePss = getIntReflectively(mi, "nativeSwappablePss");
        nativeSwappedOut = getIntReflectively(mi, "nativeSwappedOut");

        otherPrivateClean = getIntReflectively(mi, "otherPrivateClean");
        otherPrivateDirty = mi.otherPrivateDirty;
        otherPss = mi.otherPss;
        otherSharedClean = getIntReflectively(mi, "otherSharedClean");
        otherSharedDirty = mi.otherSharedDirty;
        otherSwappablePss = getIntReflectively(mi, "otherSwappablePss");
        otherSwappedOut = getIntReflectively(mi, "otherSwappedOut");
    }

    private MemoryData(final int pid) {
        this.pid = pid;
        dalvikPrivateClean = 0;
        dalvikPrivateDirty = 0;
        dalvikPss = 0;
        dalvikSharedClean = 0;
        dalvikSharedDirty = 0;
        dalvikSwappablePss = 0;
        dalvikSwappedOut = 0;

        nativePrivateClean = 0;
        nativePrivateDirty = 0;
        nativePss = 0;
        nativeSharedClean = 0;
        nativeSharedDirty = 0;
        nativeSwappablePss = 0;
        nativeSwappedOut = 0;

        otherPrivateClean = 0;
        otherPrivateDirty = 0;
        otherPss = 0;
        otherSharedClean = 0;
        otherSharedDirty = 0;
        otherSwappablePss = 0;
        otherSwappedOut = 0;
    }

    public int getPid() {
        return pid;
    }

    public int getDalvikPrivateClean() {
        return dalvikPrivateClean;
    }

    public int getDalvikPrivateDirty() {
        return dalvikPrivateDirty;
    }

    public int getDalvikPss() {
        return dalvikPss;
    }

    public int getDalvikSharedClean() {
        return dalvikSharedClean;
    }

    public int getDalvikSharedDirty() {
        return dalvikSharedDirty;
    }

    public int getDalvikSwappablePss() {
        return dalvikSwappablePss;
    }

    public int getDalvikSwappedOut() {
        return dalvikSwappedOut;
    }

    public static MemoryData getBlank(final int pid) {
        return new MemoryData(pid);
    }

    private static int getIntReflectively(final MemoryInfo mi, final String name) {
        if (!sReflectionErrorKeys.contains(name)) {
            if (mi != null) {
                final Class<?> clazz = mi.getClass();
                final Field field;
                try {
                    field = clazz.getField(name);
                    return field.getInt(mi);
                } catch (final NoSuchFieldException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                } catch (final IllegalAccessException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                } catch (final IllegalArgumentException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                } catch (final NullPointerException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public int getNativePrivateClean() {
        return nativePrivateClean;
    }

    public int getNativePrivateDirty() {
        return nativePrivateDirty;
    }

    public int getNativePss() {
        return nativePss;
    }

    public int getNativeSharedClean() {
        return nativeSharedClean;
    }

    public int getNativeSharedDirty() {
        return nativeSharedDirty;
    }

    public int getNativeSwappablePss() {
        return nativeSwappablePss;
    }

    public int getNativeSwappedOut() {
        return nativeSwappedOut;
    }

    public int getOtherPrivateClean() {
        return otherPrivateClean;
    }

    public int getOtherPrivateDirty() {
        return otherPrivateDirty;
    }

    public int getOtherPss() {
        return otherPss;
    }

    public int getOtherSharedClean() {
        return otherSharedClean;
    }

    public int getOtherSharedDirty() {
        return otherSharedDirty;
    }

    public int getOtherSwappablePss() {
        return otherSwappablePss;
    }

    public int getOtherSwappedOut() {
        return otherSwappedOut;
    }

    /**
     * Return total shared clean memory usage in kB.
     */
    public int getTotalPrivateClean() {
        return dalvikPrivateClean + nativePrivateClean + otherPrivateClean;
    }

    /**
     * Return total private dirty memory usage in kB.
     */
    public int getTotalPrivateDirty() {
        return dalvikPrivateDirty + nativePrivateDirty + otherPrivateDirty;
    }

    /**
     * Return total PSS memory usage in kB.
     */
    public int getTotalPss() {
        return dalvikPss + nativePss + otherPss;
    }

    /**
     * Return total shared clean memory usage in kB.
     */
    public int getTotalSharedClean() {
        return dalvikSharedClean + nativeSharedClean + otherSharedClean;
    }

    /**
     * Return total shared dirty memory usage in kB.
     */
    public int getTotalSharedDirty() {
        return dalvikSharedDirty + nativeSharedDirty + otherSharedDirty;
    }

    /**
     * Return total PSS memory usage in kB.
     */
    public int getTotalSwappablePss() {
        return dalvikSwappablePss + nativeSwappablePss + otherSwappablePss;
    }

    /**
     * Return total swapped out memory in kB.
     */
    public int getTotalSwappedOut() {
        return dalvikSwappedOut + nativeSwappedOut + otherSwappedOut;
    }

    /**
     * Return total PSS memory usage in kB.
     */
    public int getTotalUss() {
        return dalvikPrivateClean + dalvikPrivateDirty
                + nativePrivateClean + nativePrivateDirty
                + otherPrivateClean + otherPrivateDirty;
    }

}
