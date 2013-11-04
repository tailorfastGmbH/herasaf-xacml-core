/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:rfc822Name".<br>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 110, for further information.
 * 
 * @author Stefan Oberholzer
 */
public class RFC822Name {
	private String value;

	/**
	 * Initializes a new {@link RFC822Name}.
	 * 
	 * @param value
	 *            The value to create the {@link RFC822Name} from.
	 */
	public RFC822Name(String value) {
		// check that the string is an address, ie, that it has one and only
		// one '@' character in it
		String[] parts = value.split("@");
		if (parts.length != 2) {
			// this is malformed input
			throw new IllegalArgumentException("Invalid RFC822Name: " + value);
		}

		// cannonicalize the name
		this.value = parts[0] + "@" + parts[1].toLowerCase();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return value;
	}

	/**
	 * <p>
	 * An RFC822 name consists of a local-part followed by "@" followed by a
	 * domain-part. The local-part is case-sensitive, while the domain-part
	 * (which is usually a DNS name) is not case-sensitive.4
	 * </p>
	 * 
	 * <p>
	 * The second argument contains a complete rfc822Name. The first argument is
	 * a complete or partial rfc822Name used to select appropriate values in the
	 * second argument as follows.
	 * </p>
	 * 
	 * <p>
	 * In order to match a particular address in the second argument, the first
	 * argument must specify the complete mail address to be matched. For
	 * example, if the first argument is �Anderson@sun.com�, this matches a
	 * value in the second argument of �Anderson@sun.com� and
	 * �Anderson@SUN.COM�, but not �Anne.Anderson@sun.com�, �anderson@sun.com�
	 * or �Anderson@east.sun.com�.
	 * </p>
	 * 
	 * <p>
	 * In order to match any address at a particular domain in the second
	 * argument, the first argument must specify only a domain name (usually a
	 * DNS name). For example, if the first argument is �sun.com�, this matches
	 * a value in the first argument of �Anderson@sun.com� or �Baxter@SUN.COM�,
	 * but not �Anderson@east.sun.com�.
	 * </p>
	 * 
	 * <p>
	 * In order to match any address in a particular domain in the second
	 * argument, the first argument must specify the desired domain-part with a
	 * leading ".". For example, if the first argument is �.east.sun.com�, this
	 * matches a value in the second argument of "Anderson@east.sun.com" and
	 * "anne.anderson@ISRG.EAST.SUN.COM" but not "Anderson@sun.com".
	 * </p>
	 * 
	 * @param value
	 *            The {@link String}-value of the possible rfc822Name.
	 * @return True if the value is an rfc822Name, false otherwise.
	 */
	public boolean match(String value) {

		/*
		 * Checks if the compValue is a complete RFC822Name
		 */
		if (value.contains("@")) {
			String[] compValue = value.split("@");
			String[] thisValue = this.value.split("@");
			if (compValue.length != 2) {
				return false;
			}
			/*
			 * The part before the @ is case sensitive
			 */
			if (!compValue[0].equals(thisValue[0])) {
				return false;
			}
			/*
			 * The Domain part isn't case sensitive
			 */
			return thisValue[1].equalsIgnoreCase(compValue[1]);
		}

		/*
		 * If the domain part begins with a '.', it is only a part of a complete
		 * domain name.
		 */
		if (value.charAt(0) == '.') {
			String[] compValue = value.split("\\.");
			String[] thisValue = this.value.split("@")[1].split("\\.");
			/*
			 * If the splited Value of this object is longer than the value to
			 * compare with, it can't match.
			 */
			if (compValue.length > thisValue.length) {
				return false;
			}

			/*
			 * Checks if both arrays, beginning with the end of the Array. The
			 * first value in the array is always empty because the compValue
			 * begins with a point.
			 */
			for (int i = 0; i < compValue.length - 1; i++) {
				if (!compValue[compValue.length - 1 - i].equalsIgnoreCase(thisValue[thisValue.length - 1 - i])) {
					return false;
				}
			}
			return true;
		}
		/*
		 * If none of the upper cases applies, the compValue is a complete
		 * domain name.
		 */
		return this.value.split("@")[1].equalsIgnoreCase(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RFC822Name) {
			RFC822Name object = (RFC822Name) obj;
			return object.value.equals(this.value);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}