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

import org.herasaf.xacml.core.types.DayTimeDuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestDayTimeDuration {
	@DataProvider(name = "positivePossibleData")
	public Object[][] createPositivePossibleData() {
		return new Object[][] {
				new Object[] { "P9DT12H", "P9DT12H" },
				new Object[] { "P9DT12M1.23S", "P9DT12M1.23S" },
				new Object[] { "P9DT12M1.0S", "P9DT12M1.0S" },
				new Object[] { "P9DT12M", "P9DT12M" },
				new Object[] { "P9DT12.12S", "P9DT12.12S" },
				new Object[] { "P9DT132.0S", "P9DT132.0S" },
				new Object[] { "PT123H", "PT123H" },
				new Object[] { "PT123M1.23S", "PT123M1.23S" },
				new Object[] { "PT123M1.2S", "PT123M1.2S" },
				new Object[] { "PT123M", "PT123M" },
				new Object[] { "PT123M", "PT123M" },
				new Object[] { "PT123.3S", "PT123.3S" },
				new Object[] { "P1DT12H1M12.234S", "P1DT12H1M12.234S" },
				new Object[] { "P9DT12H1.1S", "P9DT12H1.1S" },
				new Object[] { "PT123H2M12.234S", "PT123H2M12.234S" },
				new Object[] { "PT123H1.9S", "PT123H1.9S" },
				new Object[] { "P9DT12H0.2342S", "P9DT12H0.2342S" },
				new Object[] { "P9DT12M1S","P9DT12M1S" },
				new Object[] { "P9DT132S", "P9DT132S"},
				new Object[] { "P50DT5H4M3S", "P50DT5H4M3S"},

		};
	}

	@DataProvider(name = "positiveComparableData")
	public Object[][] createPositiveComparableData() {
		return new Object[][] {
//				new Object[] { "P9DT"), "P9DT12H"), "-1") },
				new Object[] { "P9DT1.0S", "P9D", "1" },
//				new Object[] { "P9DT"), "P9DT0H"), "0") },
//				new Object[] { "P9DT"), "P9DT0.12S"), "-1") },
				new Object[] { "P9DT0.123S", "P9DT0.12S", "0" },
				new Object[] { "P9DT0.123S", "P9DT0.2S", "0" },
				new Object[] { "P9DT0.2S", "P9DT0.12S", "0" },
				new Object[] { "P9DT0.123S", "P9DT0.124S", "0" },
				new Object[] { "P9DT0.123S", "P9DT0.123S", "0" },
		};
	}


	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] {
				"P19DT" },
				new Object[] { "PT" },

				new Object[] { "P1DT12HM12.234S" },
				new Object[] { "P9DT12HM12S" },
				new Object[] { "PT123HM12.S" },
				new Object[] { "P9DT12HM12.S" },
				new Object[] { "P9DT12M1.S" },
				new Object[] { "P9DT12.S" },
				new Object[] { "PT123HM12S" },
				new Object[] { "PT123H.S" },
				new Object[] { "PT123.S" },
				new Object[] { "P9DT12H.2342S" },
				new Object[] { "P9DT12H.S" },
				new Object[] { "P9DT12M.23S" },
				new Object[] { "P9DT.17S" },
				new Object[] { "PT123H.2342S" },
				new Object[] { "PT123M1.S" },
				new Object[] { "PT123M.23S" },
		};
	}

	@AfterMethod
	public void afterMethod() {
		System.clearProperty("javax.xml.datatype.DatatypeFactory");
	}

	@Test(dataProvider="positivePossibleData", enabled = true)
	public void testsuccessfulPositiveValues(String input, String expected) throws Exception {
		DayTimeDuration duration = new DayTimeDuration(input);
		assertEquals(duration.toString(), expected);
	}

	@Test(dataProvider="positivePossibleData", enabled = true)
	public void testsuccessfulNegativeValues(String input, String expected) throws Exception {
		DayTimeDuration duration = new DayTimeDuration("-" + input);
		assertEquals(duration.toString(), "-" + expected);
	}

	@Test(dataProvider="positiveComparableData", enabled = true)
	public void testComparable(String input1, String input2, String input3) throws Exception {
		DayTimeDuration duration1 = new DayTimeDuration(input1);
		DayTimeDuration duration2 = new DayTimeDuration(input2);
		if (input3.equals("1") && duration1.compareTo(duration2) > 0) {
			assertTrue(true);
		} else if (input3.equals("0")) {
			assertEquals(duration1.compareTo(duration2), 0);
		} else if (input3.equals("-1") && duration1.compareTo(duration2) < 0) {
			assertTrue(true);
		} else {
			fail("For comparison " + input1 + " and " + input2 + " AssertEqualsValue " + input3 + " was expected but was " + duration1.compareTo(duration2));
		}
	}

	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class}, enabled = true)
	public void testFailuresPositiveValues(String input) throws Exception {
		new DayTimeDuration(input);
	}

	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class}, enabled = true)
	public void testFailuresNegativeValues(String input) throws Exception {
		new DayTimeDuration("-" + input);
	}
}