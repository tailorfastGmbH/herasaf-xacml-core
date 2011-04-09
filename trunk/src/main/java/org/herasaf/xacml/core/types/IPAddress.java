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

/**
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:ipAddress".<br>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 103, for further information.
 * 
 * @author Florian Huonder
 * @see IPv4Address
 * @see IPv6Address
 */
public abstract class IPAddress {
	/**
	 * Creates a new {@link IPAddress} from the given {@link String}.
	 * {@link IPAddress} is abstract and their is a concrete implementation for
	 * {@link IPv4Address} and {@link IPv6Address}.
	 * 
	 * @param ipAddress
	 *            The {@link String} to created the {@link IPAddress} from.
	 * @return The newly created {@link IPAddress}.
	 */
	public static IPAddress newInstance(String ipAddress) {
		if (ipAddress.contains("[")) {
			return new IPv6Address(ipAddress);
		}
		return new IPv4Address(ipAddress);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();
}