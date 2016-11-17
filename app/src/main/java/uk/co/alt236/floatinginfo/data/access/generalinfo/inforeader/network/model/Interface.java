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

package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model;

import java.util.List;

public class Interface {
    private final String mName;
    private final List<IpAddress> mAddresses;

    public Interface(final String name, final List<IpAddress> ipAddresses) {
        mName = name;
        mAddresses = ipAddresses;
    }

    public String getName() {
        return mName;
    }

    public List<IpAddress> getAddresses() {
        return mAddresses;
    }
}
