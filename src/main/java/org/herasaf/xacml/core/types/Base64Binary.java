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

import java.util.Arrays;
import java.util.List;

/**
 * This is the representation of a value encoded in base64. The constructor
 * takes a String value and converts it into a base64 value. Via the
 * {@link #getValue()} method the base64 value can be retrieved.
 * 
 * @author Florian Huonder
 */
public class Base64Binary {
	private static final List<Character> BASE64CHARS = Arrays.asList('A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '+', '/');

	private String base64String;

	public Base64Binary(String stringRepresentation) {
		stringRepresentation = stringRepresentation.trim();
		if ((stringRepresentation.length() % 4) != 0 || stringRepresentation.length() == 0) {
			throw new IllegalArgumentException(
					"A Base64Binary string's length must be a multiple of four.");
		}
		
		char[] chars = new char[stringRepresentation.length()];
		stringRepresentation.getChars(0, stringRepresentation.length(), chars, 0);
		for(int i = 0; i < chars.length; i++){
			char c = chars[i];
			if(c == '=' && ((i != chars.length - 1 && i != chars.length - 2))){
				//= can only occur at one of the last two positions, depending on the length of the original string.
				throw new IllegalArgumentException("Given Base64 string is not a valid Base64 type.");
			}
			if(!BASE64CHARS.contains(c) && c != '='){
				throw new IllegalArgumentException("Base64 string contains '" + c + "' that is not a valid Base64 character.");
			}
		}
		
		base64String = stringRepresentation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return base64String.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * The base64 binary can be compared to either its String or byte[]
	 * representation.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj.getClass().isAssignableFrom(Base64Binary.class)) {
				return ((Base64Binary) obj).getValue().equals(base64String);
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return base64String;
	}

	/**
	 * Returns the byte value of the base64.
	 * 
	 * @return The byte value of the base64.
	 */
	public String getValue() {
		return base64String;
	}
}