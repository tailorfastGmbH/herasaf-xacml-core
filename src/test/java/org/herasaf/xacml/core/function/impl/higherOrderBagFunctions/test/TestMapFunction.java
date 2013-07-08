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

package org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.MapFunction;
import org.herasaf.xacml.core.function.impl.stringConversionFunctions.StringNormalizeSpaceFunction;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the Map (higher order) function works properly.
 * 
 * @author Florian Huonder
 */
public class TestMapFunction {
	private Function function;
	private Function compareFunction;

	/**
	 * Initializes the function needed to pass to the map (higher order) function. Here it is the {@link StringNormalizeSpaceFunction} (trim).
	 * Initializes the map (higher order) function.
	 */
	@BeforeTest
	public void beforeTest() {
		this.function = new MapFunction();
		this.compareFunction = new StringNormalizeSpaceFunction();
	}

	/**
	 * Creates the test cases.
	 * 
	 * @return The created test cases.
	 */
	@DataProvider(name = "functionTest")
	public Object[][] createArgs() {
		return new Object[][] {
				new Object[] { createSet(new String[] { "    Hallo   " }),
						createSet(new String[] { "Hallo" })},
				new Object[] {
						createSet(new String[] { "\tHallo","\nDu" }),
						createSet(new String[] { "Hallo", "Du" }) },
				new Object[] {
						createSet(new String[] { }),
						createSet(new String[] { }) },
		};
	}

	/**
	 * Creates Sets of Strings.
	 * 
	 * @param strings The strings that shall be added to a set.
	 * 
	 * @return The created set.
	 */
	private List<String> createSet(String[] strings) {
		List<String> set = new ArrayList<String>();
		for (String str : strings){
			set.add(str);
		}
		return set;
	}

	/**
	 * Tests if the map function works properly.
	 * 
	 * @param set1 The input set containing strings.
	 * @param set2 The expected result set.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="functionTest")
	public void testFunction(List<String> set1, List<String> set2) throws Exception {
		assertEquals(function.handle(compareFunction, set1), set2);
	}
}