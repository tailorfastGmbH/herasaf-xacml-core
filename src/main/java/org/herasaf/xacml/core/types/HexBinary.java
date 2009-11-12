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
 * TODO JAVADOC
 * 
 * The Name of this datatype is http://www.w3.org/2001/XMLSchema#hexBinary.<br>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 103, for further information.
 * 
 * @author Florian Huonder
 * @version 1.0
 */
public class HexBinary {
	private byte[] value;

	/**
	 * Creates a new {@link HexBinary} from the given {@link String}
	 * -representation.
	 * 
	 * @param value
	 *            The {@link String} to create the {@link HexBinary} from.
	 */
	public HexBinary(String value) {
		if ((value.length() % 2) != 0) {
			throw new IllegalArgumentException("A HexBinary string must have even length.");
		}
		byte[] result = new byte[value.length() / 2];
		int j = 0;
		for (int i = 0; i < value.length();) {
			byte b;
			char c = value.charAt(i++);
			char d = value.charAt(i++);
			if (c >= '0' && c <= '9') {
				b = (byte) ((c - '0') << 4);
			} else if (c >= 'A' && c <= 'F') {
				b = (byte) ((c - 'A' + 10) << 4);
			} else if (c >= 'a' && c <= 'f') {
				b = (byte) ((c - 'a' + 10) << 4);
			} else {
				throw new IllegalArgumentException("Invalid hex digit: " + c);
			}
			if (d >= '0' && d <= '9') {
				b += (byte) (d - '0');
			} else if (d >= 'A' && d <= 'F') {
				b += (byte) (d - 'A' + 10);
			} else if (d >= 'a' && d <= 'f') {
				b += (byte) (d - 'a' + 10);
			} else {
				throw new IllegalArgumentException("Invalid hex digit: " + d);
			}
			result[j++] = b;
		}
		this.value = result;
	}

	/**
	 * Returns the hey-value as {@code byte[]} array.
	 * 
	 * @return The {@code byte[]} of the {@link HexBinary}.
	 */
	public byte[] getValue() {
		return value.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HexBinary) {
			if (this.value.length != ((HexBinary) obj).getValue().length) {
				return false;
			}
			for (int i = 0; i < this.value.length; i++) {
				if (this.value[i] != ((HexBinary) obj).getValue()[i]) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashValue = 0;
		for (int i = 0; i < value.length; i++) {
			hashValue += value[i] * (value.length ^ (value.length - i - 1));
		}
		return hashValue;
	}
}