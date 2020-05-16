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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class CpuData {
    private final List<Integer> mPerCpuUtilisation;
    private final int mOverallCpu;
    private final boolean mHasError;
    private final String statFile;

    public CpuData(@NonNull final String statFile, final boolean error) {
        this(statFile, -1, error);
    }

    public CpuData(@NonNull final String statFile, final int overallCpu) {
        this(statFile, overallCpu, false);
    }

    private CpuData(@NonNull final String statFile, final int overallCpu, final boolean error) {
        mPerCpuUtilisation = new ArrayList<>();
        mOverallCpu = overallCpu;
        mHasError = error;
        this.statFile = statFile;
    }

    public void addUtilisation(final int value) {
        mPerCpuUtilisation.add(value);
    }

    public List<Integer> getPerCpuUtilisation() {
        return Collections.unmodifiableList(mPerCpuUtilisation);
    }

    public int getOverallCpu() {
        return mOverallCpu;
    }

    public boolean hasError() {
        return mHasError;
    }

    @NonNull
    public String getStatFile() {
        return statFile;
    }
}
