/*
 * Copyright 2008 HERAS-AF (www.herasaf.org)
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
 * TODO REVIEW René.
 * 
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:dnsName".<br>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 103, for further information.
 * 
 * @author Florian Huonder
 */
public class DnsName {
	private static final String DNS_NAME_PART_DELIMITER = "\\.";
	private static final String PORT_RANGE_DELIMITER = ":";
	private static final String AC = "[^!#\\$%&'()\\*\\+,/:;=\\?@\\[\\]\\s]"; // AC
	// =
	// allowed
	// characters
	private static final String MATCHPATTERN = "^((" + AC + "+(\\." + AC + "+)*)|(\\*)|(\\*\\." + AC + "+(\\." + AC
			+ "+)*))(:[\\d\\-]+)??$";
	private PortRange portRange;
	private String[] dnsNameParts = null;

	/**
	 * Initializes a new {@link DnsName} from the given string.
	 * 
	 * @param stringRepresentation
	 *            The {@link String} to create the {@link DnsName} from.
	 */
	public DnsName(String stringRepresentation) {
		if (!stringRepresentation.matches(MATCHPATTERN)) {
			throw new IllegalArgumentException("String is not a dns name: " + stringRepresentation);
		}
		int colonPosition = stringRepresentation.indexOf(PORT_RANGE_DELIMITER);
		if (colonPosition != -1) {
			portRange = new PortRange(stringRepresentation.substring(colonPosition + 1, stringRepresentation.length()));
			dnsNameParts = stringRepresentation.split(PORT_RANGE_DELIMITER)[0].split(DNS_NAME_PART_DELIMITER);
		} else {
			dnsNameParts = stringRepresentation.split(DNS_NAME_PART_DELIMITER);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder dnsName = new StringBuilder();
		for (int i = 0; i < dnsNameParts.length; i++) {
			String dnsNamePart;
			dnsNamePart = dnsNameParts[i];
			if (i != 0) {
				dnsNamePart = "." + dnsNamePart;
			}
			dnsName.append(dnsNamePart);
		}
		if (portRange != null) {
			dnsName.append(PORT_RANGE_DELIMITER + portRange.toString());
		}
		return dnsName.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return toString().equals(obj.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}