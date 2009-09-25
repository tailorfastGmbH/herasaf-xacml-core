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

package org.herasaf.xacml.core.dataTypeAttribute.impl.test;

import static org.testng.Assert.assertEquals;

import org.herasaf.xacml.core.dataTypeAttribute.impl.Base64BinaryDataTypeAttribute;
import org.herasaf.xacml.core.types.Base64Binary;
import org.herasaf.xacml.core.types.test.Base64Encoder;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link Base64BinaryDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestBase64BinaryDataTypeAttribute {
	private Base64BinaryDataTypeAttribute dataType;
	
	/**
	 * Creates the test cases for the test.
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name="data")
	public Object[][] createData(){
		return new Object[][]{
				new Object[] {"My test string."},
				new Object[] {""},
				new Object[] {"1234567890"},
				new Object[] {"#sfdlkj@"},
				new Object[] {"This is a sentence without a punctuation mark"},
		};
	}
	
	/**
	 * Initializes a new {@link Base64BinaryDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new Base64BinaryDataTypeAttribute();
	}
	
	/**
	 * Converts various {@link String}s into {@link Base64Binary} and checks if it worked fine.
	 * @param input A {@link String} to convert
	 * @throws Exception In case of an error.
	 */
	@Test (dataProvider="data")
	public void testInput(String input) throws Exception{
		String base64Data =  Base64Encoder.encodeString(input);
		assertEquals(dataType.convertTo(base64Data).getValue(), new Base64Binary(base64Data).getValue());
	}
	
	/**
	 * Tests if the {@link Base64BinaryDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(), "http://www.w3.org/2001/XMLSchema#base64Binary");
	}
}