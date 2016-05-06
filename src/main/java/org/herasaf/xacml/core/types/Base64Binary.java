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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the representation of a value encoded in Base64. The constructor
 * takes a Base64 string value and verifies that it is indeed a legal Base64
 * string. Via the {@link #getValue()} method the base64 value can be retrieved.
 * 
 * @author Florian Huonder
 */
public class Base64Binary {
	private static final Set<Character> BASE64CHARS;
	private static final Set<Character> IGNORED_WHITESPACE;
	private static final char PADDING_CHAR = '=';

	static {
		BASE64CHARS = new HashSet<Character>();
		Collections.addAll(BASE64CHARS, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
				'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
				'6', '7', '8', '9', '+', '/');

		IGNORED_WHITESPACE = new HashSet<Character>();
		Collections.addAll(IGNORED_WHITESPACE, '\t', '\n', '\r', ' ', '\u00A0', '\u2007', '\u202F');
	}

	private final String base64String;

	public Base64Binary(final String stringRepresentation) {
		final char[] characters = stringRepresentation.toCharArray();
		int relevantCharsCount = 0;
		boolean isPreviousPaddingCharacter = false;
		for (int i = 0; i < characters.length; ++i) {
			char character = characters[i];
			final boolean isWhitespaceCharacter = IGNORED_WHITESPACE.contains(character);
			if (isWhitespaceCharacter) {
				continue;
			}
			relevantCharsCount++;
			isPreviousPaddingCharacter = verifyCharacter(character, isPreviousPaddingCharacter);
		}
		verifyLength(relevantCharsCount);
		base64String = stringRepresentation;
	}

	private boolean verifyCharacter(final char character, final boolean isPreviousPaddingCharacter) {
		final boolean isPaddingCharacter = PADDING_CHAR == character;
		if (isPreviousPaddingCharacter && !isPaddingCharacter) {
			throw new IllegalArgumentException("Encountered non-padding character.");
		} else {
			if (!isPaddingCharacter) {
				verifyBase64Character(character);
			}
		}

		return isPaddingCharacter;
	}

	private void verifyBase64Character(final char character) {
		final boolean isBase64Character = BASE64CHARS.contains(character);
		if (!isBase64Character) {
			String message = String.format("Base64 string contains '%s' which is not a valid Base64 character.",
					character);
			throw new IllegalArgumentException(message);
		}
	}

	private void verifyLength(final int relevantCharsCount) {
		final boolean isNotMultipleOfFour = relevantCharsCount % 4 != 0;
		final boolean hasNoRelevantCharacters = relevantCharsCount == 0;
		if (isNotMultipleOfFour || hasNoRelevantCharacters) {
			throw new IllegalArgumentException("A Base64Binary string's length must be a multiple of four.");
		}
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