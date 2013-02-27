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

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.TimeDataTypeAttribute;
import org.joda.time.DateTimeZone;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link TimeDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestTimeDataTypeAttribute {
	private TimeDataTypeAttribute dataType;

	private DateTimeZone defaultZone;

	/**
	 * Sets the default timezone to +00:00 for testing.
	 */
	@BeforeTest
	public void init() {
		defaultZone = DateTimeZone.getDefault();

		DateTimeZone.setDefault(DateTimeZone.forOffsetHours(0));
	}

	@AfterTest
	public void cleanUp() {
		DateTimeZone.setDefault(defaultZone);
	}
	
	/**
	 * Creates negative test cases for the test.
	 * 
	 * @return The negative test cases.
	 */
	@DataProvider(name = "negativeData")
	public Object[][] initNegativeData() {
		return new Object[][] { new Object[] { "22:60:21" },
				new Object[] { "2005-10-10" },
				new Object[] { "2005-10-10T12:00:00" }, };
	}

	/**
	 * Creates positive test cases for the test.
	 * 
	 * @return The positive test cases.
	 */
	@DataProvider(name = "positiveData")
	public Object[][] initPositiveData() {
		return new Object[][] { new Object[] { "12:00:01.00", "12:00:01+00:00" },
				new Object[] { "00:00:00", "00:00:00+00:00" }, };
	}

	/**
	 * Initializes a new {@link TimeDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new TimeDataTypeAttribute();
	}

	/**
	 * Tests the positive data.
	 * 
	 * @param input The positive data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveData")
	public void testInput(String input, String expected) throws Exception {
		List<String> data = new ArrayList<String>();
		data.add(input);
		assertEquals(dataType.convertTo(data).toString(), expected);
	}

	/**
	 * Tests if the {@link TimeDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"http://www.w3.org/2001/XMLSchema#time");
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