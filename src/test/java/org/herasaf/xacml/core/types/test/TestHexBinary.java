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

import static org.testng.Assert.assertEquals;

import org.herasaf.xacml.core.types.HexBinary;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *  Tests the {@link HexBinary} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestHexBinary {

	/**
	 * Creates positive test cases. One byte.
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "positiveCasesOneByte")
	public Object[][] createPositiveCasesOneByte(){
		return new Object[][] {
				new Object[]{ "03", 3},
				new Object[]{ "ff", 255},
				new Object[]{ "Ff", 255},
				new Object[]{ "8A", 138},
		};
	}
	
	/**
	 * Creates positive test cases. Two bytes.
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "positiveCasesTwoBytes")
	public Object[][] createPositiveCasesTwoBytes(){
		return new Object[][] {
				new Object[]{ "FFFF", 255, 255},
				new Object[]{ "Ff03", 255, 3},
				new Object[]{ "FF07", 255, 7},
				new Object[]{ "A110", 161, 16},
		};
	}
	
	/**
	 * Creates negative test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "negativeCases")
	public Object[][] createNegativeCases(){
		return new Object[][] {
				new Object[]{ "r3"},
				new Object[]{ "12h32"},
		};
	}
	
	/**
	 * Test if the {@link HexBinary} objects are properly created.
	 * One byte.
	 * 
	 * @param input The hey-binary in its String representation.
	 * @param expected The expected String on {@link HexBinary#getValue()}.
	 * @throws Exception If an error occurs.
	 */
	@Test (dataProvider="positiveCasesOneByte")
	public void testOneByteValues(String input, int expected) throws Exception{
		HexBinary hexBinary = new HexBinary(input);
		assertEquals(hexBinary.getValue()[0], (byte)expected);
	}
	
	/**
	 * Test if the {@link HexBinary} objects are properly created.
	 * Two bytes.
	 * 
	 * @param input The hey-binary in its String representation.
	 * @param expectedFirstByte The expected first byte on {@link HexBinary#getValue()}.
	 * @param expectedSecondByte The expected second byte on {@link HexBinary#getValue()}.
	 * @throws Exception If an error occurs.
	 */
	@Test (dataProvider="positiveCasesTwoBytes")
	public void testOnlyUpperCaseCharacters(String input, int expectedFirstByte, int expectedSecondByte) throws Exception{
		HexBinary hexBinary = new HexBinary(input);
		assertEquals(hexBinary.getValue()[0], (byte)expectedFirstByte);
		assertEquals(hexBinary.getValue()[1], (byte)expectedSecondByte);
	}
	
	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal hey-binary representations.
	 * Positive values.
	 * @param input The illegal hey-binary strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class})
	public void testNotAllowedCharacters(String input) throws Exception{
		new HexBinary(input);
	}
}
