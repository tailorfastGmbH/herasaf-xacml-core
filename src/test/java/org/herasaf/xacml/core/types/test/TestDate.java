/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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
import org.herasaf.xacml.core.types.Date;
import org.joda.time.DateTimeZone;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the {@link Date} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestDate {
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
	@DataProvider(name = "positiveCasesDefaultTimeZoneWithZulu")
	public Object[][] createPositiveCasesWithZulu() {
		return new Object[][] { new Object[] { "2006-11-11", "2006-11-11Z" },
				new Object[] { "2004-12-01", "2004-12-01Z" }, new Object[] { "2004-12-01", "2004-12-01Z" },
				new Object[] { "2004-12-01Z", "2004-12-01Z" }, new Object[] { "2004-12-01+01:00", "2004-12-01+01:00" }, };
	}

	/**
	 * Creates positive test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "positiveCasesDefaultTimeZoneWithNonZulu")
	public Object[][] createPositiveCasesWithNonZulu() {
		return new Object[][] { new Object[] { "2006-11-11", "2006-11-11+00:00" },
				new Object[] { "2004-12-01", "2004-12-01+00:00" }, new Object[] { "2004-12-01", "2004-12-01+00:00" },
				new Object[] { "2004-12-01", "2004-12-01+00:00" }, new Object[] { "2004-12-01", "2004-12-01+00:00" },
				new Object[] { "2004-12-01Z", "2004-12-01+00:00" },
				new Object[] { "2004-12-01+01:00", "2004-12-01+01:00" }, };
	}

	/**
	 * Creates negative test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] { "12:00:00" }, new Object[] { "2003-12-00" }, };
	}

	/**
	 * Creates comparison test cases. Uses the default timezone
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "comparison")
	public Object[][] createComparisonData() {
		return new Object[][] { new Object[] { "2004-11-01", "2004-12-01", -1 },
				new Object[] { "2005-12-01", "2004-12-01", 1 }, new Object[] { "2004-12-01", "2004-12-01", 0 },
				new Object[] { "1998-12-01", "2004-12-01", -1 }, new Object[] { "2035-09-01", "2004-12-01", 1 },
				new Object[] { "2000-03-12", "2000-03-12", 0 }, };
	}

	/**
	 * TimezoneTests, positive
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "timezoneTests")
	public Object[][] createPositiveTimeZoneCases() {
		return new Object[][] {
				new Object[] { "2006-11-11", "2006-11-11-09:00", "Etc/GMT+9" },
				new Object[] { "2004-12-01", "2004-12-01+00:00", "GMT" },
				new Object[] { "2004-12-01", "2004-12-01+03:00", "Etc/GMT-3" },
				new Object[] { "2004-12-01", "2004-12-01-01:00", "Etc/GMT+1" },

				// The following cases test if the default timezone is ignored when one is set.
				new Object[] { "2006-11-11-09:00", "2006-11-11-09:00", "GMT" },
				new Object[] { "2004-12-01+00:00", "2004-12-01+00:00", "GMT" },
				new Object[] { "2004-12-01+03:00", "2004-12-01+03:00", "GMT" },
				new Object[] { "2004-12-01-01:00", "2004-12-01-01:00", "GMT" }, };
	}

	/**
	 * Test if the {@link Date} objects are properly created.
	 * 
	 * @param input
	 *            The date in its String representation.
	 * @param expected
	 *            The expected String on {@link Date#toString()}.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "timezoneTests")
	public void testPositiveTimeZone(String input, String expected, String timezone) throws Exception {
		DateTimeZone defaultZone = DateTimeZone.getDefault();
		DateTimeZone.setDefault(DateTimeZone.forID(timezone));
		Date date = new Date(input);
		assertEquals(date.toString(), expected);
		DateTimeZone.setDefault(defaultZone);
	}

	/**
	 * Test if the {@link Date} objects are properly created.
	 * 
	 * @param input
	 *            The date in its String representation.
	 * @param expected
	 *            The expected String on {@link Date#toString()}.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "positiveCasesDefaultTimeZoneWithNonZulu")
	public void testWithoutZulu(String input, String expected) throws Exception {
		Date.useZuluUtcRepresentation(false);
		Date date = new Date(input);
		assertEquals(date.toString(), expected);
	}

	@Test(dataProvider = "positiveCasesDefaultTimeZoneWithZulu")
	public void testWithZulu(String input, String expected) throws Exception {
		Date.useZuluUtcRepresentation(true);
		Date date = new Date(input);
		assertEquals(date.toString(), expected);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal date representations.
	 * 
	 * @param input
	 *            The illegal date strings.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { SyntaxException.class })
	public void testInputFail(String input) throws Exception {
		new Date(input);
	}

	/**
	 * Tests if the comparison function of {@link Date} works properly.
	 * 
	 * @param input1
	 *            The first string.
	 * @param input2
	 *            The second date string.
	 * @param expected
	 *            The expected result (-1, 0, 1).
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "comparison")
	public void testCompare(String input1, String input2, int expected) throws Exception {
		Date one = new Date(input1);
		Date two = new Date(input2);

		assertEquals(one.compareTo(two), expected);
	}

	@Test(dataProvider = "comparison")
	public void testEquals(String input1, String input2, int expected) throws Exception {
		Date one = new Date(input1);
		Date two = new Date(input2);

		boolean expectedEqualsResult;
		if (expected == 0) {
			expectedEqualsResult = true;
		} else {
			expectedEqualsResult = false;
		}

		assertEquals(one.equals(two), expectedEqualsResult);
	}
}