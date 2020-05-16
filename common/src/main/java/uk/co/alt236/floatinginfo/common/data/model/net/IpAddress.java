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

package uk.co.alt236.floatinginfo.common.data.model.net;

public class IpAddress {
    private final int version;
    private final String address;

    public IpAddress(final int version, final String address) {
        this.version = version;
        this.address = address;
    }

    public int getVersion() {
        return version;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "IpAddress{" +
                "version=" + version +
                ", address='" + address + '\'' +
                '}';
    }
}
