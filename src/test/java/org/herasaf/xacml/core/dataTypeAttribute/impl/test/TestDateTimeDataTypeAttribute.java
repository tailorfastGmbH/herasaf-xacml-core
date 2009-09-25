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

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.BooleanDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DateTimeDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link DateTimeDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestDateTimeDataTypeAttribute {
	private DateTimeDataTypeAttribute dataType;

	/**
	 * Creates negative test cases for the test.
	 * 
	 * @return The negative test cases.
	 */
	@DataProvider(name = "negativeData")
	public Object[][] initNegativeData() {
		return new Object[][] { new Object[] { "12:00:00" },
				new Object[] { "2006-04-31" },
				new Object[] { "2006-02-29T12:00:00" }, };
	}

	/**
	 * Creates positive test cases for the test.
	 * 
	 * @return The positive test cases.
	 */
	@DataProvider(name = "positiveData")
	public Object[][] initPositiveData() {
		return new Object[][] {
				new Object[] { "2006-03-31T12:00:00" },
				new Object[] { "2004-02-29T00:00:00.000001" }, };
	}

	/**
	 * Initializes a new {@link BooleanDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new DateTimeDataTypeAttribute();
	}

	/**
	 * Tests the positive data.
	 * 
	 * @param input The positive data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveData")
	public void testInput(String input) throws Exception {
		assertEquals(input, dataType.convertTo(input).toString());
	}

	/**
	 * Tests if the {@link BooleanDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"http://www.w3.org/2001/XMLSchema#dateTime");
	}

	/**
	 * Tests the negative data.
	 * 
	 * @param input The negative data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "negativeData", expectedExceptions = { SyntaxException.class })
	public void testWrongInput(String input) throws Exception {
		dataType.convertTo(input);
	}
}