/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
 * Holistic Enterprise-Ready Application Security Architecture Framework
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.herasaf.xacml.core.types;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents an IP-V4 address.
 * 
 * @author Florian Huonder
 * @see IPAddress
 */
public class IPv6Address extends IPAddress {
	private static final String REGEX = "\\[[\\.:0-9A-Fa-f]+\\](/\\[[\\.:0-9A-Fa-f]+\\])?(:[\\d\\-]+)?";
	private InetAddress ip;
	private InetAddress mask;
	private PortRange portRange;

	/**
	 * Creates a new {@link IPv6Address} from the given {@link String}.
	 * 
	 * @param value
	 *            The String to create the {@link IPv6Address} from.
	 */
	public IPv6Address(String value) {
		if (!value.matches(REGEX)) {
			throw new IllegalArgumentException(value + " is not a valid IP Address");
		}
		try {
			int slashPosition = value.indexOf("/");
			int bracketPosition = value.lastIndexOf("]");
			int colonPosition = value.lastIndexOf(":");
			boolean hasRange = colonPosition - bracketPosition == 1;

			if (slashPosition != -1 && hasRange) {
				ip = Inet6Address.getByName(value.substring(1, slashPosition - 1));
				mask = Inet6Address.getByName(value.substring(slashPosition + 2, colonPosition - 1));
				portRange = new PortRange(value.substring(colonPosition + 1, value.length()));
			} else if (slashPosition != -1) {
				ip = Inet6Address.getByName(value.substring(1, slashPosition - 1));
				mask = Inet6Address.getByName(value.substring(slashPosition + 2, value.length() - 1));
			} else if (hasRange) {
				ip = Inet6Address.getByName(value.substring(1, colonPosition - 1));
				portRange = new PortRange(value.substring(colonPosition + 1, value.length()));
			} else {
				ip = Inet6Address.getByName(value.substring(1, value.length() - 1));
			}
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		/*
		 * If ip is an Inet4Address-compatible IP-Address, Java automatically
		 * converts it into an Inet4Adress, therefore it must be converted back
		 * into IPv6 style.
		 */
		if (ip instanceof Inet4Address) {
			builder.append("0:0:0:0:0:ffff:");
		}
		builder.append(ip.getHostAddress());
		builder.append("]");

		if (mask != null) {
			builder.append("/[");
			if (mask instanceof Inet4Address) {
				builder.append("0:0:0:0:0:ffff:");
			}
			builder.append(mask.getHostAddress());
			builder.append("]");
		}
		if (portRange != null) {
			builder.append(":");
			builder.append(portRange.toString());
		}

		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return toString().equals(obj.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}