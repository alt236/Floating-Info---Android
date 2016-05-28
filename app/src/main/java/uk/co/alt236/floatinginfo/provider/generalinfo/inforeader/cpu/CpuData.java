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
package uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CpuData {
    private final List<Integer> mPerCpuUtilisation;
    private final int mOveralCpu;

    public CpuData(final int overalCpu) {
        mPerCpuUtilisation = new ArrayList<Integer>();
        mOveralCpu = overalCpu;
    }

    public void addCpuUtil(final int value) {
        mPerCpuUtilisation.add(value);
    }

    public List<Integer> getPerCpuUtilisation() {
        return Collections.unmodifiableList(mPerCpuUtilisation);
    }

    public int getOveralCpu() {
        return mOveralCpu;
    }
}
