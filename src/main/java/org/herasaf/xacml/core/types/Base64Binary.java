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
 * This is the representation of a value encoded in base64. The constructor
 * takes a String value and converts it into a base64 value. Via the
 * {@link #getValue()} method the base64 value can be retrieved.
 * 
 * @author Florian Huonder
 */
public class Base64Binary {
	private byte[] value;

	private static final byte EQUALS_SIGN = (byte) '=';
	private static final byte WHITE_SPACE_ENC = -5; // Indicates white space in
	// encoding
	private static final byte EQUALS_SIGN_ENC = -1; // Indicates equals sign in
	// encoding

	/**
	 * Translates a Base64 value to either its 6-bit reconstruction value or a
	 * negative number indicating some other meaning.
	 **/
	private static final byte[] DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal
			// 0
			// -
			// 8
			-5, -5, // Whitespace: Tab and Linefeed
			-9, -9, // Decimal 11 - 12
			-5, // Whitespace: Carriage Return
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 -
			// 26
			-9, -9, -9, -9, -9, // Decimal 27 - 31
			-5, // Whitespace: Space
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 42
			62, // Plus sign at decimal 43
			-9, -9, -9, // Decimal 44 - 46
			63, // Slash at decimal 47
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
			-9, -9, -9, // Decimal 58 - 60
			-1, // Equals sign at decimal 61
			-9, -9, -9, // Decimal 62 - 64
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through
			// 'N'
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O'
			// through 'Z'
			-9, -9, -9, -9, -9, -9, // Decimal 91 - 96
			26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a'
			// through 'm'
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n'
			// through 'z'
			-9, -9, -9, -9 // Decimal 123 - 126
	/*
	 * ,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 127 - 139
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 140 - 152
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 153 - 165
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 166 - 178
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 179 - 191
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 192 - 204
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 205 - 217
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 218 - 230
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 231 - 243
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9 // Decimal 244 - 255
	 */
	};

	/**
	 * Takes a string and decodes it into a base64 value.
	 * 
	 * @param value
	 *            The base64 data as string.
	 */
	public Base64Binary(String value) {
		this.value = decode(value);
	}

	/**
	 * Returns a {@code byte[]} of the base64 data.
	 * 
	 * @return The {@code byte[]} of the base64 data.
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
		if (obj instanceof Base64Binary) {
			if (this.value.length != ((Base64Binary) obj).getValue().length) {
				return false;
			}
			for (int i = 0; i < this.value.length; i++) {
				if (this.value[i] != ((Base64Binary) obj).getValue()[i]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Decodes data from Base64 notation.
	 * 
	 * @param s
	 *            the string to decode
	 * @return the decoded data
	 * @since 1.4
	 */
	private static byte[] decode(String s) {
		byte[] bytes = s.getBytes();
		return decode(bytes, 0, bytes.length);
	}

	/**
	 * Decodes Base64 content in byte array format and returns the decoded byte
	 * array.
	 * 
	 * @param source
	 *            The Base64 encoded data
	 * @param off
	 *            The offset of where to begin decoding
	 * @param len
	 *            The length of characters to decode
	 * @return The decoded data.
	 */
	private static byte[] decode(byte[] source, int off, int len) {
		int len34 = len * 3 / 4;
		byte[] outBuff = new byte[len34]; // Upper limit on size of output
		int outBuffPosn = 0;

		byte[] b4 = new byte[4];
		int b4Posn = 0;
		int i = 0;
		byte sbiCrop = 0;
		byte sbiDecode = 0;
		for (i = 0; i < len; i++) {
			sbiCrop = (byte) (source[i] & 0x7f); // Only the low seven bits
			sbiDecode = DECODABET[sbiCrop];

			if (sbiDecode >= WHITE_SPACE_ENC) { // White space, Equals sign or
				// better
				if (sbiDecode >= EQUALS_SIGN_ENC) {
					b4[b4Posn++] = sbiCrop;
					if (b4Posn > 3) {
						outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
						b4Posn = 0;

						// If that was the equals sign, break out of 'for' loop
						if (sbiCrop == EQUALS_SIGN) {
							break;
						}
					}
				}
			} else {
				throw new IllegalArgumentException("Bad Base64 input character at " + i + ": " + source[i]
						+ "(decimal)");
			}
		}
		byte[] out = new byte[outBuffPosn];
		System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
		return out;
	}

	/**
	 * Decodes four bytes from array <var>source</var> and writes the resulting
	 * bytes (up to three of them) to <var>destination</var>. The source and
	 * destination arrays can be manipulated anywhere along their length by
	 * specifying <var>srcOffset</var> and <var>destOffset</var>. This method
	 * does not check to make sure your arrays are large enough to accomodate
	 * <var>srcOffset</var> + 4 for the <var>source</var> array or
	 * <var>destOffset</var> + 3 for the <var>destination</var> array. This
	 * method returns the actual number of bytes that were converted from the
	 * Base64 encoding.
	 * 
	 * 
	 * @param source
	 *            the array to convert
	 * @param srcOffset
	 *            the index where conversion begins
	 * @param destination
	 *            the array to hold the conversion
	 * @param destOffset
	 *            the index where output will be put
	 * @return the number of decoded bytes converted
	 */
	private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset) {
		// Example: Dk=
		if (source[srcOffset + 2] == EQUALS_SIGN) {
			int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
					| ((DECODABET[source[srcOffset + 1]] << 24) >>> 12);

			destination[destOffset] = (byte) (outBuff >>> 16);
			return 1;
		}

		// Example: DkL=
		else if (source[srcOffset + 3] == EQUALS_SIGN) {
			int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
					| ((DECODABET[source[srcOffset + 1]] << 24) >>> 12)
					| ((DECODABET[source[srcOffset + 2]] << 24) >>> 18);

			destination[destOffset] = (byte) (outBuff >>> 16);
			destination[destOffset + 1] = (byte) (outBuff >>> 8);
			return 2;
		}

		// Example: DkLE
		else {
			int outBuff = ((DECODABET[source[srcOffset]] << 24) >>> 6)
					| ((DECODABET[source[srcOffset + 1]] << 24) >>> 12)
					| ((DECODABET[source[srcOffset + 2]] << 24) >>> 18)
					| ((DECODABET[source[srcOffset + 3]] << 24) >>> 24);

			destination[destOffset] = (byte) (outBuff >> 16);
			destination[destOffset + 1] = (byte) (outBuff >> 8);
			destination[destOffset + 2] = (byte) (outBuff);
			return 3;
		}
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