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

package org.herasaf.xacml.core.types.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.herasaf.xacml.core.types.YearMonthDuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *  Tests the {@link YearMonthDuration} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestYearMonthDuration {
	
	/**
	 * Creates positive test cases. 
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "positivePossibleCases")
	public Object[][] createPossiblePositiveCases(){
		return new Object[][] {
				new Object[]{"P91234Y2M", "P91234Y2M"},
				new Object[]{"P91234M", "P91234M"},
				new Object[]{"P91234Y", "P91234Y"},
		};
	}
	
	/**
	 * Creates comparison test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "positiveComparableCases")
	public Object[][] createComparableCases(){
		return new Object[][] {
				new Object[]{"P91234Y2M", "P91234Y2M", "0"},
				new Object[]{"P2M", "P1M", "1"},
				new Object[]{"P12M", "P1Y", "0"},
				new Object[]{"P1M", "P2M", "-1"},
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
				new Object[]{ "PY"},
				new Object[]{ "PM"},
				new Object[]{ "P4YM"},
				new Object[]{ "PY4M"},
		};
	}
	
	/**
	 * Clears the property javax.xml.datatype.DatatypeFactory.
	 */
	@AfterMethod
	public void afterMethod() {
		System.clearProperty("javax.xml.datatype.DatatypeFactory");
	}

	/**
	 * Test if the {@link YearMonthDuration} objects are properly created.
	 * Positive data.
	 * 
	 * @param input The year month duration in its String representation.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="positivePossibleCases", enabled = true)
	public void testsuccessfulPositiveValues(String input, String expected) throws Exception {
		YearMonthDuration duration = new YearMonthDuration(input);
		assertEquals(duration.toString(), expected);
	}

	/**
	 * Test if the {@link YearMonthDuration} objects are properly created.
	 * Negative data.
	 * 
	 * @param input The year month duration in its String representation.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="positivePossibleCases", enabled = true)
	public void testsuccessfulNegativeValues(String input, String expected) throws Exception {
		YearMonthDuration duration = new YearMonthDuration("-" + input);
		assertEquals(duration.toString(), "-" + expected);
	}

	/**
	 * Tests if the comparison function of {@link YearMonthDuration} works properly.
	 * 
	 * @param input1 The first string.
	 * @param input2 The second year month duration string.
	 * @param input3 A control flag..
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="positiveComparableCases", enabled = true)
	public void testComparable(String input1, String input2, String input3) throws Exception {
		YearMonthDuration duration1 = new YearMonthDuration(input1);
		YearMonthDuration duration2 = new YearMonthDuration(input2);
		if (input3.equals("1") && duration1.compareTo(duration2) > 0) {
			assertTrue(true);
		} else if (input3.equals("0")) {
			assertEquals(duration1.compareTo(duration2), 0);
		} else if (input3.equals("-1")
				&& duration1.compareTo(duration2) < 0) {
			assertTrue(true);
		} else {
			fail("For comparint " + input1 + " and " + input2
					+ " AssertEqualsValue " + input3
					+ " was expected but was "
					+ duration1.compareTo(duration2));
		}
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal year month duration representations.
	 * Positive cases.
	 * 
	 * @param input The illegal time strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class}, enabled = true)
	public void testFailuresPositiveValues(String input) throws Exception {
		new YearMonthDuration(input);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal year month duration representations.
	 * Negative cases.
	 * 
	 * @param input The illegal time strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class}, enabled = true)
	public void testFailuresNegativeValues(String input) throws Exception {
		new YearMonthDuration("-" + input);
	}
}