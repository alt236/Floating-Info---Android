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
package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CpuData {
    private final List<Integer> mPerCpuUtilisation;
    private final int mOverallCpu;
    private final boolean mHasError;

    /*package*/ CpuData(final boolean error) {
        this(-1, true);
    }

    /*package*/ CpuData(final int overallCpu) {
        this(overallCpu, false);
    }

    private CpuData(final int overallCpu, final boolean error) {
        mPerCpuUtilisation = new ArrayList<>();
        mOverallCpu = overallCpu;
        mHasError = error;
    }

    /*package*/ void addUtilisation(final int value) {
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
}
