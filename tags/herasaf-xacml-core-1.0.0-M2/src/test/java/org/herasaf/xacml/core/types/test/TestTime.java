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

import org.herasaf.xacml.core.types.Time;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *  Tests the {@link Time} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestTime {
	
	/**
	 * Creates positive test cases. 
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "positiveCases")
	public Object[][] createPositiveCases() {
		return new Object[][] { new Object[] { "12:00:01.239" },
				new Object[] { "12:00:01.2" },
				new Object[] { "12:00:01.35" },
				new Object[] { "12:00:01" },
				new Object[] { "12:00:01-05:00" }
		};
	}

	/**
	 * Creates comparison test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "comparison")
	public Object[][] createComparisonData() {
		return new Object[][] { new Object[] { "12:00:00", "13:00:00", -1 },
				new Object[] { "15:00:00", "13:00:00", 1 },
				new Object[] { "13:00:00", "13:00:00", 0 },
				new Object[] { "12:00:01", "12:00:01.001", -1 },
				new Object[] { "15:03:00", "13:00:00.234", 1 },
				new Object[] { "13:00:00.978", "13:00:00.978", 0 }, };
	}

	/**
	 * Creates negative test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] { "12.00.01" },
				new Object[] { "2005-10-10" }, };
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
	 * Test if the {@link Time} objects are properly created.
	 * 
	 * @param input The time in its String representation.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "positiveCases")
	public void testInput1(String input) throws Exception {
		Time time = new Time(input);
		assertEquals(time.toString(), input);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal time representations.
	 * @param input The illegal time strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { IllegalArgumentException.class })
	public void testInputFail(String input) throws Exception {
		new Time(input);
	}

	/**
	 * Tests if an exception is thrown when a non-existing DatatypeFactory is set.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNoDataFactoryFound() throws Exception {
		System.setProperty("javax.xml.datatype.DatatypeFactory", "bla");
		new Time("12:00:01");
	}

	/**
	 * Tests if the comparison function of {@link Time} works properly.
	 * 
	 * @param input1 The first string.
	 * @param input2 The second time string.
	 * @param expected The expected result (-1, 0, 1).
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "comparison")
	public void testCompare(String input1, String input2, int expected)
			throws Exception {
		Time one = new Time(input1);
		Time two = new Time(input2);

		assertEquals(one.compareTo(two), expected);
	}
}