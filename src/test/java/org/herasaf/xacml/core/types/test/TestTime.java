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

package org.herasaf.xacml.core.types.test;

import static org.testng.Assert.assertEquals;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.types.Time;
import org.joda.time.DateTimeZone;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TODO More tests.
 * 
 * Tests the {@link Time} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestTime {

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
	 * Creates positive test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "positiveCasesWithNonZulu")
	public Object[][] createPositiveCasesWithNonZulu() {
		return new Object[][] { new Object[] { "12:00:01.2", "12:00:01.2+00:00" },
				new Object[] { "12:00:01.35", "12:00:01.35+00:00" },
				new Object[] { "12:00:01.35Z", "12:00:01.35+00:00" },
				new Object[] { "12:00:01.35+00:00", "12:00:01.35+00:00" },
				new Object[] { "12:00:01.239", "12:00:01.239+00:00" }, new Object[] { "12:00:01", "12:00:01+00:00" },
				new Object[] { "12:00:01-05:00", "12:00:01-05:00" }, new Object[] { "24:00:00", "00:00:00+00:00" } };
	}

	@DataProvider(name = "positiveCasesWithZulu")
	public Object[][] createPositiveCasesWithZulu() {
		return new Object[][] { new Object[] { "12:00:01.2", "12:00:01.2Z" },
				new Object[] { "12:00:01.35", "12:00:01.35Z" }, new Object[] { "12:00:01.35Z", "12:00:01.35Z" },
				new Object[] { "12:00:01.35+00:00", "12:00:01.35Z" }, new Object[] { "12:00:01.239", "12:00:01.239Z" },
				new Object[] { "12:00:01", "12:00:01Z" }, new Object[] { "12:00:01-05:00", "12:00:01-05:00" },
				new Object[] { "24:00:00", "00:00:00Z" } };
	}

	/**
	 * Creates positive test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "timezoneCases")
	public Object[][] createTimezoneCases() {
		return new Object[][] { new Object[] { "12:00:01.239+01:00", "12:00:01.239+01:00" },
				new Object[] { "12:00:01-09:00", "12:00:01-09:00" },
				new Object[] { "12:00:01+00:00", "12:00:01+00:00" },
				new Object[] { "12:00:01-00:00", "12:00:01+00:00" }, };
	}

	/**
	 * Creates comparison test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "comparison")
	public Object[][] createComparisonData() {
		return new Object[][] { new Object[] { "12:00:00", "13:00:00", -1 },
				new Object[] { "15:00:00", "13:00:00", 1 }, new Object[] { "13:00:00", "13:00:00", 0 },
				new Object[] { "12:00:01", "12:00:01.001", -1 }, new Object[] { "15:03:00", "13:00:00.234", 1 },
				new Object[] { "13:00:00.978", "13:00:00.978", 0 }, new Object[] { "24:00:00", "00:00:00", 0 }, };
	}

	/**
	 * Creates negative test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] { "12.00.01" }, new Object[] { "2005-10-10" }, };
	}

	@Test(dataProvider = "positiveCasesWithNonZulu")
	public void testWithNonZulu(String input, String expected) throws Exception {
		Time.useZuluUtcRepresentation = false;
		Time.reInitializeFormatter();
		Time time = new Time(input);
		assertEquals(time.toString(), expected);
	}

	@Test(dataProvider = "positiveCasesWithZulu")
	public void testTimezoneInput(String input, String expected) throws Exception {
		Time.useZuluUtcRepresentation = true;
		Time.reInitializeFormatter();
		Time time = new Time(input);
		assertEquals(time.toString(), expected);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal time representations.
	 * 
	 * @param input
	 *            The illegal time strings.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { SyntaxException.class })
	public void testInputFail(String input) throws Exception {
		new Time(input);
	}

	/**
	 * Tests if the comparison function of {@link Time} works properly.
	 * 
	 * @param input1
	 *            The first string.
	 * @param input2
	 *            The second time string.
	 * @param expected
	 *            The expected result (-1, 0, 1).
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "comparison")
	public void testCompare(String input1, String input2, int expected) throws Exception {
		Time one = new Time(input1);
		Time two = new Time(input2);

		assertEquals(one.compareTo(two), expected);
	}
}