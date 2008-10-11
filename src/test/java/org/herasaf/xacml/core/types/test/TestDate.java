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

import org.herasaf.xacml.core.types.Date;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestDate {
	private Date date;

	@DataProvider(name = "positiveCases")
	public Object[][] createPositiveCases() {
		return new Object[][] {
				new Object[] { "02006-11-11",
						"2006-11-11" },
				new Object[] { "2004-12-01",
						"2004-12-01" },
				new Object[] { "2004-12-01",
						"2004-12-01" },
				new Object[] { "2004-12-01+01:00",
						"2004-12-01+01:00" }, };
	}

	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] { "12:00:00" },
				new Object[] { "+2003-12-12" }, };
	}

	@DataProvider(name = "comparison")
	public Object[][] createComparisonData() {
		return new Object[][] {
				new Object[] { "2004-11-01", "2004-12-01", -1 },
				new Object[] { "2005-12-01", "2004-12-01", 1 },
				new Object[] { "2004-12-01", "2004-12-01", 0 },
				new Object[] { "1998-12-01", "2004-12-01", -1 },
				new Object[] { "2035-09-01", "2004-12-01", 1 },
				new Object[] { "2000-03-12", "2000-03-12", 0 }, };
	}

	@AfterMethod
	public void afterMethod() {
		System.clearProperty("javax.xml.datatype.DatatypeFactory");
	}

	@Test(dataProvider = "positiveCases")
	public void testInput1(String input, String expected) throws Exception {
		date = new Date(input);
		assertEquals(expected, date.toString());
	}

	@Test(dataProvider = "negativeCases", expectedExceptions = { IllegalArgumentException.class })
	public void testInputFail(String input) throws Exception {
		new Date(input);
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testNoDataFactoryFound() throws Exception {
		System.setProperty("javax.xml.datatype.DatatypeFactory", "bla");
		new Date("2005-02-02");
	}

	@Test(dataProvider = "comparison")
	public void testCompare(String input1, String input2, int expected)
			throws Exception {
		Date one = new Date(input1);
		Date two = new Date(input2);

		assertEquals(one.compareTo(two), expected);
	}
}