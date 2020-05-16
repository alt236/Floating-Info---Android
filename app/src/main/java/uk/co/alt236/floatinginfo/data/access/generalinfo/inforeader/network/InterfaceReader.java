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

package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.Interface;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.IpAddress;

/*package*/ class InterfaceReader {

    @NonNull
    public List<Interface> getInterfaces() {
        final List<Interface> retVal = new ArrayList<>();
        final List<NetworkInterface> ifaces = getNetworkInterfaces();

        for (final NetworkInterface iface : ifaces) {
            final List<IpAddress> addresses = new ArrayList<>();
            final String name = iface.getName();

            final List<InetAddress> inetAddresses = safeList(iface.getInetAddresses());
            for (InetAddress inetAddress : inetAddresses) {
                final IpAddress candidate = getAddress(inetAddress);
                if (candidate != null) {
                    addresses.add(candidate);
                }
            }

            if (!addresses.isEmpty()) {
                retVal.add(new Interface(name, addresses));
            }
        }

        return retVal;
    }

    @NonNull
    private List<NetworkInterface> getNetworkInterfaces() {
        List<NetworkInterface> retVal;
        try {
            retVal = safeList(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException e) {
            retVal = new ArrayList<>();
            e.printStackTrace();
        }

        return retVal;
    }

    private IpAddress getAddress(final InetAddress inetAddress) {

        final IpAddress retVal;
        final String addressString = inetAddress.getHostAddress();

        if (!inetAddress.isLoopbackAddress()
                && !TextUtils.isEmpty(addressString)) {

            if (inetAddress instanceof Inet6Address) {
                // drop ip6 port suffix
                final int delim = addressString.indexOf('%');
                final String ipv6 = delim < 0 ? addressString : addressString.substring(0, delim);
                retVal = new IpAddress(6, ipv6);

            } else if (inetAddress instanceof Inet4Address) {
                retVal = new IpAddress(4, addressString);
            } else {
                retVal = null;
            }
        } else {
            retVal = null;
        }

        return retVal;
    }

    @NonNull
    private <T> List<T> safeList(@Nullable final Enumeration<T> enumeration) {
        return enumeration == null ? new ArrayList<T>() : Collections.list(enumeration);
    }
}
