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
 * Represents a http://www.w3.org/2001/XMLSchema#hexBinary.<br>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 109, for further information.
 * 
 * @author Florian Huonder
 */
public class HexBinary {
	private char[] value;

	/**
	 * Creates a new {@link HexBinary} from the given {@link String}
	 * -representation.
	 * 
	 * @param value
	 *            The {@link String} to create the {@link HexBinary} from.
	 */
	public HexBinary(String stringRepresentation) {
		if ((stringRepresentation.length() % 2) != 0) {
			throw new IllegalArgumentException(
					"A HexBinary string must have even length.");
		}
		value = new char[stringRepresentation.length()];
		// TODO check to lower case with the issue because of the to lower case
		// and locale dependency.
		stringRepresentation = stringRepresentation.toLowerCase();
		stringRepresentation.getChars(0, stringRepresentation.length(), value,
				0);
		// Added the characters to the char array value and check now if it
		// contains valid hex characters. If not, throw an exception and abort
		// the object creation
		for (char c : value) {
			if (!(('0' <= c && c <= '9') || ('a' <= c && c <= 'f'))) {
				throw new IllegalArgumentException("The String "
						+ stringRepresentation + " is not a hex string");
			}
		}
	}

	/**
	 * Returns the hey-value as {@code byte[]} array.
	 * 
	 * @return The {@code byte[]} of the {@link HexBinary}.
	 */
	public char[] getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj.getClass().isAssignableFrom(HexBinary.class)) {
				return ((HexBinary) obj).getValue().equals(value);
			}
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

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}