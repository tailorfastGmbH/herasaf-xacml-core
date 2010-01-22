/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents an IP-V4 address.
 * 
 * @author Florian Huonder
 * @see IPAddress
 */
public class IPv4Address extends IPAddress {
	private static final String REGEX = "^[0-2](\\d\\d)?.[0-2](\\d\\d)?.[0-2](\\d\\d)?.[0-2](\\d\\d)?(/[0-2](\\d\\d)?.[0-2](\\d\\d)?.[0-2](\\d\\d)?.[0-2](\\d\\d)?)?(:[^\\*\\.:]+)??$";
	private InetAddress ip;
	private InetAddress mask;
	private PortRange portRange;

	/**
	 * Creates a new {@link IPv4Address} from the given {@link String}.
	 * 
	 * @param value
	 *            The String to create the {@link IPv4Address} from.
	 */
	public IPv4Address(String value) {
		if (!value.matches(REGEX)) {
			throw new IllegalArgumentException(value + " is not a valid IP Address");
		}
		try {
			int slashPosition = value.indexOf("/");
			int colonPosition = value.indexOf(":");

			if (slashPosition != -1 && colonPosition != -1) {
				ip = Inet4Address.getByName(value.substring(0, slashPosition));
				mask = Inet4Address.getByName(value.substring(slashPosition + 1, colonPosition));
				portRange = new PortRange(value.substring(colonPosition + 1, value.length()));
			} else if (slashPosition != -1) {
				ip = Inet4Address.getByName(value.substring(0, slashPosition));
				mask = Inet4Address.getByName(value.substring(slashPosition + 1, value.length()));
			} else if (colonPosition != -1) {
				ip = Inet4Address.getByName(value.substring(0, colonPosition));
				portRange = new PortRange(value.substring(colonPosition + 1, value.length()));
			} else {
				ip = Inet4Address.getByName(value.substring(0, value.length()));
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
		builder.append(ip.getHostAddress());
		if (mask != null) {
			builder.append("/");
			builder.append(mask.getHostAddress());
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