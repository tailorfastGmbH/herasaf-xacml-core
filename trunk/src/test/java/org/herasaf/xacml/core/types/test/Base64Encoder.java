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

package org.herasaf.xacml.core.types.test;

import org.herasaf.xacml.core.types.Base64Binary;

/**
 * A Base64 encoder used as utility to test the {@link Base64Binary} data type.
 * 
 * @author Florian Huonder
 */
public class Base64Encoder {
	/** Specify encoding (value is <tt>true</tt>). */
    public final static boolean ENCODE = true;

    /** Specify decoding (value is <tt>false</tt>). */
    public final static boolean DECODE = false;


    /** Maximum line length (76) of Base64 output. */
    public final static int MAX_LINE_LENGTH = 76;

    /** The equals sign (=) as a byte. */
    private final static byte EQUALS_SIGN = (byte)'=';

    /** The new line character (\n) as a byte. */
    private final static byte NEW_LINE = (byte)'\n';

    /** The 64 valid Base64 values. */
    private final static byte[] ALPHABET =
    {
        (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F', (byte)'G',
        (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L', (byte)'M', (byte)'N',
        (byte)'O', (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T', (byte)'U',
        (byte)'V', (byte)'W', (byte)'X', (byte)'Y', (byte)'Z',
        (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g',
        (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n',
        (byte)'o', (byte)'p', (byte)'q', (byte)'r', (byte)'s', (byte)'t', (byte)'u',
        (byte)'v', (byte)'w', (byte)'x', (byte)'y', (byte)'z',
        (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
        (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/'
    };

    /**
     * Encodes up to three bytes of the array <var>source</var>
     * and writes the resulting four Base64 bytes to <var>destination</var>.
     * The source and destination arrays can be manipulated
     * anywhere along their length by specifying
     * <var>srcOffset</var> and <var>destOffset</var>.
     * This method does not check to make sure your arrays
     * are large enough to accomodate <var>srcOffset</var> + 3 for
     * the <var>source</var> array or <var>destOffset</var> + 4 for
     * the <var>destination</var> array.
     * The actual number of significant bytes in your array is
     * given by <var>numSigBytes</var>.
     *
     * @param source the array to convert
     * @param srcOffset the index where conversion begins
     * @param numSigBytes the number of significant bytes in your array
     * @param destination the array to hold the conversion
     * @param destOffset the index where output will be put
     * @return the <var>destination</var> array
     * @since 1.3
     */
    private static byte[] encode3to4(
     byte[] source, int srcOffset, int numSigBytes,
     byte[] destination, int destOffset )
    {
        //           1         2         3
        // 01234567890123456789012345678901 Bit position
        // --------000000001111111122222222 Array position from threeBytes
        // --------|    ||    ||    ||    | Six bit groups to index ALPHABET
        //          >>18  >>12  >> 6  >> 0  Right shift necessary
        //                0x3f  0x3f  0x3f  Additional AND

        // Create buffer with zero-padding if there are only one or two
        // significant bytes passed in the array.
        // We have to shift left 24 in order to flush out the 1's that appear
        // when Java treats a value as negative that is cast from a byte to an int.
        int inBuff =   ( numSigBytes > 0 ? ((source[ srcOffset     ] << 24) >>>  8) : 0 )
                     | ( numSigBytes > 1 ? ((source[ srcOffset + 1 ] << 24) >>> 16) : 0 )
                     | ( numSigBytes > 2 ? ((source[ srcOffset + 2 ] << 24) >>> 24) : 0 );

        switch( numSigBytes )
        {
            case 3:
                destination[ destOffset     ] = ALPHABET[ (inBuff >>> 18)        ];
                destination[ destOffset + 1 ] = ALPHABET[ (inBuff >>> 12) & 0x3f ];
                destination[ destOffset + 2 ] = ALPHABET[ (inBuff >>>  6) & 0x3f ];
                destination[ destOffset + 3 ] = ALPHABET[ (inBuff       ) & 0x3f ];
                return destination;

            case 2:
                destination[ destOffset     ] = ALPHABET[ (inBuff >>> 18)        ];
                destination[ destOffset + 1 ] = ALPHABET[ (inBuff >>> 12) & 0x3f ];
                destination[ destOffset + 2 ] = ALPHABET[ (inBuff >>>  6) & 0x3f ];
                destination[ destOffset + 3 ] = EQUALS_SIGN;
                return destination;

            case 1:
                destination[ destOffset     ] = ALPHABET[ (inBuff >>> 18)        ];
                destination[ destOffset + 1 ] = ALPHABET[ (inBuff >>> 12) & 0x3f ];
                destination[ destOffset + 2 ] = EQUALS_SIGN;
                destination[ destOffset + 3 ] = EQUALS_SIGN;
                return destination;

            default:
                return destination;
        }
    }

    public static String encodeBytes( byte[] source ){
    	return encodeBytes( source, 0, source.length );
    }

    /**
     * Encodes a byte array into Base64 notation.
     *
     * @param source The data to convert
     * @param off Offset in array where conversion should begin
     * @param len Length of data to convert
     * @since 1.4
     */
    private static String encodeBytes( byte[] source, int off, int len ){
        int    len43   = len * 4 / 3;
        byte[] outBuff = new byte[   ( len43 )                      // Main 4:3
                                   + ( (len % 3) > 0 ? 4 : 0 )      // Account for padding
                                   + ( len43 / MAX_LINE_LENGTH ) ]; // New lines
        int d = 0;
        int e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        for( ; d < len2; d+=3, e+=4 )
        {
            encode3to4( source, d, 3, outBuff, e );

            lineLength += 4;
            if( lineLength == MAX_LINE_LENGTH )
            {
                outBuff[e+4] = NEW_LINE;
                e++;
                lineLength = 0;
            }
        }

        if( d < len ){
            encode3to4( source, d, len - d, outBuff, e );
            e += 4;
        }

        return new String( outBuff, 0, e );
    }


    /**
     * Encodes a string in Base64 notation with line breaks
     * after every 75 Base64 characters.
     *
     * @param s the string to encode
     * @return the encoded string
     * @since 1.3
     */
    public static String encodeString( String s ){
    	return encodeBytes( s.getBytes() );
    }
}