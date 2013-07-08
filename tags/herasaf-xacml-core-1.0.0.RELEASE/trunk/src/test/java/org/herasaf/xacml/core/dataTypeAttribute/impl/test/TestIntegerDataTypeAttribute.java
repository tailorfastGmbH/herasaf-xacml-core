/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.HexBinaryDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.IntegerDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link HexBinaryDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestIntegerDataTypeAttribute {
	private IntegerDataTypeAttribute dataType;

	/**
	 * Creates negative test cases for the test.
	 * 
	 * @return The negative test cases.
	 */
	@DataProvider(name = "negativeData")
	public Object[][] initNegativeData() {
		return new Object[][] { new Object[] { "1.0" },
				new Object[] { "1,0" }, };
	}

	/**
	 * Creates positive test cases for the test.
	 * 
	 * @return The positive test cases.
	 */
	@DataProvider(name = "positiveData")
	public Object[][] initPositiveData() {
		return new Object[][] { new Object[] { "1" },
				new Object[] { "-1" },
				new Object[] { "999999999999999999" },
				new Object[] { "-999999999999999999" }, };
	}

	/**
	 * Initializes a new {@link HexBinaryDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new IntegerDataTypeAttribute();
	}

	/**
	 * Tests the positive data.
	 * 
	 * @param input The positive data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveData")
	public void testInput(String input) throws Exception {
		List<String> data = new ArrayList<String>();
		data.add(input);
		assertEquals(new BigInteger(input), dataType.convertTo(data));
	}

	/**
	 * Tests if the {@link HexBinaryDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"http://www.w3.org/2001/XMLSchema#integer");
	}

	/**
	 * Tests the negative data.
	 * 
	 * @param input The negative data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "negativeData", expectedExceptions = { SyntaxException.class })
	public void testWrongInput(String input) throws Exception {
		List<String> data = new ArrayList<String>();
		data.add(input);
		dataType.convertTo(data);
	}
}