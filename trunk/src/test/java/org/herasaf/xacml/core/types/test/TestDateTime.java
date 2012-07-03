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
import org.herasaf.xacml.core.types.Date;
import org.herasaf.xacml.core.types.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the {@link DateTime} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestDateTime {
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
	@DataProvider(name = "positiveCasesWithNonZuluUtc")
	public Object[][] createPositiveNonZuluCases() {
		return new Object[][] { new Object[] { "2005-02-02T12:00:01.239", "2005-02-02T12:00:01.239+00:00" },
				new Object[] { "2005-02-02T12:00:01.239+00:00", "2005-02-02T12:00:01.239+00:00" },
				new Object[] { "2005-02-02T12:00:01.239Z", "2005-02-02T12:00:01.239+00:00" },
				new Object[] { "2005-02-02T12:00:01", "2005-02-02T12:00:01+00:00" },
				{ "2007-02-01T24:00:00", "2007-02-02T00:00:00+00:00" } };
	}

	@DataProvider(name = "positiveCasesWithZuluUtc")
	public Object[][] createPositiveCases() {
		return new Object[][] { new Object[] { "2005-02-02T12:00:01.239", "2005-02-02T12:00:01.239Z" },
				new Object[] { "2005-02-02T12:00:01.239+00:00", "2005-02-02T12:00:01.239Z" },
				new Object[] { "2005-02-02T12:00:01.239Z", "2005-02-02T12:00:01.239Z" },
				new Object[] { "2005-02-02T12:00:01", "2005-02-02T12:00:01Z" },
				{ "2007-02-01T24:00:00", "2007-02-02T00:00:00Z" } };
	}

	/**
	 * Creates negative test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] { "2005-02-02T12:00:01.239.9304" }, new Object[] { "12:00:00" },
				new Object[] { "2006-04-31" }, };
	}

	/**
	 * Creates comparison test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "comparison")
	public Object[][] createComparisonData() {
		return new Object[][] { new Object[] { "2005-02-02T12:00:00", "2005-02-02T12:00:01", -1 },
				new Object[] { "2005-02-03T12:00:01", "2005-02-02T12:00:01", 1 },
				new Object[] { "2005-02-02T12:00:01", "2005-02-02T12:00:01", 0 },
				new Object[] { "2005-02-02T12:00:01", "2005-02-02T12:00:01.001", -1 },
				new Object[] { "2005-02-02T12:00:01", "2005-02-02T12:00:00.999", 1 },
				new Object[] { "2005-02-02T12:00:01.645", "2005-02-02T12:00:01.645", 0 }, };
	}

	/**
	 * Test if the {@link DateTime} objects are properly created.
	 * 
	 * @param input
	 *            The date-time in its String representation.
	 * @param expected
	 *            The expected String on {@link DateTime#toString()}.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "positiveCasesWithNonZuluUtc")
	public void testNonZulu(String input, String expected) throws Exception {
		Date.useZuluUtcRepresentation(false);
		DateTime dateTime = new DateTime(input);
		assertEquals(dateTime.toString(), expected);
	}

	@Test(dataProvider = "positiveCasesWithZuluUtc")
	public void testZulu(String input, String expected) throws Exception {
		DateTime.useZuluUtcRepresentation(true);
		DateTime dateTime = new DateTime(input);
		assertEquals(dateTime.toString(), expected);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal date-time representations.
	 * 
	 * @param input
	 *            The illegal date-time strings.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { SyntaxException.class })
	public void testInputFail(String input) throws Exception {
		new DateTime(input);
	}

	/**
	 * Tests if the comparison function of {@link DateTime} works properly.
	 * 
	 * @param input1
	 *            The first string.
	 * @param input2
	 *            The second date-time string.
	 * @param expected
	 *            The expected result (-1, 0, 1).
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "comparison")
	public void testCompare(String input1, String input2, int expected) throws Exception {
		DateTime one = new DateTime(input1);
		DateTime two = new DateTime(input2);

		assertEquals(one.compareTo(two), expected);
	}
}