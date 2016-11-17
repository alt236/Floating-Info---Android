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

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.Interface;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.IpAddress;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/*package*/class InterfaceWriter implements TextWriter<List<Interface>> {

    @Override
    public void writeText(final List<Interface> input, @NonNull final StringBuilderHelper sb) {
        if (isValid(input)) {
            sb.appendBold("Interfaces");
            sortInterfaces(input);

            for (final Interface iface : input) {
                sb.appendBold(iface.getName());
                sb.startKeyValueSection();

                sortAddresses(iface.getAddresses());
                for (final IpAddress address : iface.getAddresses()) {
                    sb.append("IP v" + address.getVersion(), address.getAddress());
                }
                sb.endKeyValueSection();
            }

            sb.appendNewLine();
        }
    }

    private boolean isValid(final List<Interface> interfaces) {
        return interfaces != null && !interfaces.isEmpty();
    }

    private void sortInterfaces(final List<Interface> input) {
        Collections.sort(input, new Comparator<Interface>() {
            @Override
            public int compare(final Interface lhs, final Interface rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
    }

    private void sortAddresses(final List<IpAddress> input) {
        Collections.sort(input, new Comparator<IpAddress>() {
            @Override
            public int compare(final IpAddress lhs, final IpAddress rhs) {
                final int lhsv = lhs.getVersion();
                final int rhsv = rhs.getVersion();

                return lhsv < rhsv ? -1 : (lhsv == rhsv ? 0 : 1);
            }
        });
    }

    @Override
    public void clear() {
        // NOOP
    }
}
