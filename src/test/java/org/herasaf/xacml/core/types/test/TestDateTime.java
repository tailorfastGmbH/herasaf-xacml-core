/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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

import org.herasaf.xacml.core.types.DateTime;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *  Tests the {@link DateTime} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestDateTime {
	private DateTime dateTime;

	/**
	 * Creates positive test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "positiveCases")
	public Object[][] createPositiveCases() {
		return new Object[][] {
				new Object[] { "2005-02-02T12:00:01.239",
						"2005-02-02T12:00:01.239" },
				new Object[] { "2005-02-02T12:00:01",
						"2005-02-02T12:00:01" }, };
	}

	/**
	 * Creates negative test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] {
				new Object[] { "2005-02-02T12:00:01.239.9304" },
				new Object[] { "2005-2-02T12:00:01.239" },
				new Object[] { "12:00:00" },
				new Object[] { "2006-04-31" }, };
	}

	/**
	 * Creates comparison test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "comparison")
	public Object[][] createComparisonData() {
		return new Object[][] {
				new Object[] { "2005-02-02T12:00:00", "2005-02-02T12:00:01", -1 },
				new Object[] { "2005-02-03T12:00:01", "2005-02-02T12:00:01", 1 },
				new Object[] { "2005-02-02T12:00:01", "2005-02-02T12:00:01", 0 },
				new Object[] { "2005-02-02T12:00:01",
						"2005-02-02T12:00:01.001", -1 },
				new Object[] { "2005-02-02T12:00:01",
						"2005-02-02T12:00:00.999", 1 },
				new Object[] { "2005-02-02T12:00:01.645",
						"2005-02-02T12:00:01.645", 0 }, };
	}

	/**
	 * Clears the property javax.xml.datatype.DatatypeFactory.
	 * This is only relevant for the {@link #testNoDataFactoryFound()} method.
	 */
	@AfterMethod
	public void afterMethod() {
		System.clearProperty("javax.xml.datatype.DatatypeFactory");
	}

	/**
	 * Test if the {@link DateTime} objects are properly created.
	 * 
	 * @param input The date-time in its String representation.
	 * @param expected The expected String on {@link DateTime#toString()}.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "positiveCases")
	public void testInput(String input, String expected) throws Exception {
		dateTime = new DateTime(input);
		assertEquals(dateTime.toString(), expected);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal date-time representations.
	 * @param input The illegal date-time strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { IllegalArgumentException.class })
	public void testInputFail1(String input) throws Exception {
		new DateTime(input);
	}

	/**
	 * Tests if an exception is thrown when a non-existing DatatypeFactory is set.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNoDataFactoryFound() throws Exception {
		System.setProperty("javax.xml.datatype.DatatypeFactory", "bla");
		new DateTime("2005-02-02T12:00:01");
	}

	/**
	 * Tests if the comparison function of {@link DateTime} works properly.
	 * 
	 * @param input1 The first string.
	 * @param input2 The second date-time string.
	 * @param expected The expected result (-1, 0, 1).
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "comparison")
	public void testCompare(String input1, String input2, int expected)
			throws Exception {
		DateTime one = new DateTime(input1);
		DateTime two = new DateTime(input2);

		assertEquals(one.compareTo(two), expected);
	}
}