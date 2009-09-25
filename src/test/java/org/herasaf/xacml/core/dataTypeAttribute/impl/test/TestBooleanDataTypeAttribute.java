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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.BooleanDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link BooleanDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestBooleanDataTypeAttribute {
	private BooleanDataTypeAttribute dataType;

	/**
	 * Creates positive true test cases for the test.
	 * 
	 * @return The positive true test cases.
	 */
	@DataProvider(name = "positiveDataTrue")
	public Object[][] createPositiveDataTrue() {
		return new Object[][] { new Object[] { "1" },
				new Object[] { "true" } };
	}

	/**
	 * Creates positive false test cases for the test.
	 * 
	 * @return The positive false test cases.
	 */
	@DataProvider(name = "positiveDataFalse")
	public Object[][] createPositiveDataFalse() {
		return new Object[][] { new Object[] { "0" },
				new Object[] { "false" } };
	}

	/**
	 * Creates negative test cases for the test.
	 * 
	 * @return The negative test cases.
	 */
	@DataProvider(name = "negativeData")
	public Object[][] createNegativeData() {
		return new Object[][] { new Object[] { "True" },
				new Object[] { "hallo" } };
	}

	/**
	 * Initializes a new {@link BooleanDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new BooleanDataTypeAttribute();
	}

	/**
	 * Tests the positive true data.
	 * 
	 * @param input The positive true data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveDataTrue")
	public void testInputTrue(String input) throws Exception {
		assertTrue(dataType.convertTo(input));
	}

	/**
	 * Tests the positive false data.
	 * 
	 * @param input The positive false data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveDataFalse")
	public void testInputFalse(String input) throws Exception {
		assertFalse(dataType.convertTo(input));
	}

	/**
	 * Tests the negative data.
	 * 
	 * @param input The negative data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "negativeData", expectedExceptions = { SyntaxException.class })
	public void testInputtrueWrongSpelled(String input) throws Exception {
		dataType.convertTo(input);
	}

	/**
	 * Tests if the {@link BooleanDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"http://www.w3.org/2001/XMLSchema#boolean");
	}
}