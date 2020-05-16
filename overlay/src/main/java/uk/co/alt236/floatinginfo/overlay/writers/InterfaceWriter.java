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

package uk.co.alt236.floatinginfo.overlay.writers;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import uk.co.alt236.floatinginfo.common.data.model.net.Interface;
import uk.co.alt236.floatinginfo.common.data.model.net.IpAddress;
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper;

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
        Collections.sort(input, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
    }

    private void sortAddresses(final List<IpAddress> input) {
        Collections.sort(input, (lhs, rhs) -> {
            final int lhsv = lhs.getVersion();
            final int rhsv = rhs.getVersion();

            return Integer.compare(lhsv, rhsv);
        });
    }

    @Override
    public void clear() {
        // NOOP
    }
}
