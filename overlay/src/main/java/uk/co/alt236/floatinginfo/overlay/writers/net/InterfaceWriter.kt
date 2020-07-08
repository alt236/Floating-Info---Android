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
package uk.co.alt236.floatinginfo.overlay.writers.net

import uk.co.alt236.floatinginfo.common.data.model.net.Interface
import uk.co.alt236.floatinginfo.common.data.model.net.IpAddress
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper
import uk.co.alt236.floatinginfo.overlay.writers.TextWriter

/*package*/
internal class InterfaceWriter : TextWriter<List<Interface>> {

    override fun writeText(input: List<Interface>?, sb: StringBuilderHelper) {
        if (!isValid(input)) {
            return
        }

        sb.appendBold("Interfaces")
        for ((name, addresses) in sortInterfaces(input!!)) {
            sb.appendBold(name)
            sb.startKeyValueSection()

            for (address in sortAddresses(addresses)) {
                sb.append("IP v" + address.version, address.address)
            }
            sb.endKeyValueSection()
        }

        sb.appendNewLine()
    }

    private fun isValid(interfaces: List<Interface>?): Boolean {
        return interfaces != null && !interfaces.isEmpty()
    }

    private fun sortInterfaces(input: List<Interface>): List<Interface> {
        return input.sortedWith(interfaceComparator)
    }

    private fun sortAddresses(input: List<IpAddress>): List<IpAddress> {
        return input.sortedWith(ipAddressComparator)
    }

    override fun clear() {
        // NOOP
    }

    private companion object {
        val interfaceComparator = java.util.Comparator<Interface> { o1, o2 -> o1.name.compareTo(o2.name, ignoreCase = true) }
        val ipAddressComparator = java.util.Comparator<IpAddress> { o1, o2 -> o1.address.compareTo(o2.address, ignoreCase = true) }
    }
}