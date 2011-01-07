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

package org.herasaf.xacml.core.function.impl.setFunction;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.setFunction.DayTimeDurationSetEqualsFunction;
import org.herasaf.xacml.core.types.DayTimeDuration;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the SetEquals function works properly.
 * 
 * @author Florian Huonder
 */
public class TestSetEquals {
	private Function function;

	/**
	 * Initializes the function.
	 * This test is executed with the {@link DayTimeDurationSetEqualsFunction} function.
	 */
	@BeforeTest
	public void beforeTest() {
		this.function = new DayTimeDurationSetEqualsFunction();
	}

	/**
	 * Creates various test cases.
	 * @return The created test cases.
	 */
	@DataProvider(name = "functionTest")
	public Object[][] createArgs() {
		return new Object[][] {
				new Object[] { createSet(new String[] { "P1DT3H1M12.45S" }),
						createSet(new String[] { "P1DT3H1M12.45S" }),
						(true) },
				new Object[] {
						createSet(new String[] { "P1DT3H1M12.45S",
								"P1DT3H1M13S" }),
						createSet(new String[] { "P1DT3H1M12.45S" }),
						(false) },
				new Object[] {
						createSet(new String[] { "P1DT3H1M13.45S",
								"P1DT3H1M12S" }),
						createSet(new String[] { "P1DT3H1M13.45S",
								"P1DT3H1M12S" }), (true) },
				new Object[] {
						createSet(new String[] { "P1DT3H1M12.45S",
								"P1DT3H1M13S" }),
						createSet(new String[] { "P1DT3H1M12.46S" }),
						(false) },
				new Object[] {
						createSet(new String[] { "P1DT3H1M16.45S",
								"P1DT3H1M12S" }), createSet(new String[] {}),
						(false) },
				new Object[] { createSet(new String[] {}),
						createSet(new String[] {}), (true) },
				new Object[] {
						createSet(new String[] {}),
						createSet(new String[] { "P1DT3H1M14.45S",
								"P1DT3H1M12S" }), (false) },
				new Object[] {
						createSet(new String[] { "P1DT3H1M12S" }),
						createSet(new String[] { "P1DT3H1M13S",
								"P1DT3H1M12S" }), (false) }

		};
	}

	/**
	 * Creates Sets of {@link DayTimeDuration}s.
	 * 
	 * @param strings The strings that shall be added to a set.
	 * 
	 * @return The created set.
	 */
	private List<DayTimeDuration> createSet(String[] strings) {
		List<DayTimeDuration> durations = new ArrayList<DayTimeDuration>();
		for (String str : strings) {
			durations.add(new DayTimeDuration(str));
		}
		return durations;
	}

	/**
	 * Tests if the function works properly.
	 * 
	 * @param durations1 The first duration.
	 * @param durations2 The second duration.
	 * @param expectedResult The expected result.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "functionTest")
	public void testFunction(List<DayTimeDuration> durations1,
			List<DayTimeDuration> durations2, Boolean expectedResult)
			throws Exception {

		assertEquals(function.handle(durations1, durations2), expectedResult);
	}
}