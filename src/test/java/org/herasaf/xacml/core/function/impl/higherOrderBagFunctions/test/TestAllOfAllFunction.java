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

package org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.equalityPredicates.StringEqualFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AllOfAllFunction;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the AllOfAll (higher order) function works properly.
 * 
 * @author Florian Huonder
 */
public class TestAllOfAllFunction {

	private Function function;
	private Function compareFunction;

	/**
	 * Initializes the compare function needed to pass to the AllOfAll (higher order) function.
	 * Initializes the AllOfAll (higher order) function.
	 */
	@BeforeTest
	public void beforeTest() {
		this.function = new AllOfAllFunction();
		this.compareFunction = new StringEqualFunction();
	}

	/**
	 * Creates the test cases.
	 * 
	 * @return The created test cases.
	 */
	@DataProvider(name = "functionTest")
	public Object[][] createArgs() {
		return new Object[][] {
				new Object[] { createSet(new String[] { "Hallo" }),
						createSet(new String[] { "Hallo" }),
						(true) },
				new Object[] {
						createSet(new String[] { "Hallo",
								"Du" }),
						createSet(new String[] { "Hallo" }),
						(false) },
				new Object[] {
						createSet(new String[] { "Hallo", "Test" }),
						createSet(new String[] { "Hallo", "Test" }),
						(false) },
				new Object[] {
						createSet(new String[] { "Test", "Test" }),
						createSet(new String[] { "Test", "Test" }),
						(true) },
				new Object[] {
						createSet(new String[] {}),
						createSet(new String[] { "Hallo" }),
						(false) },
				new Object[] {
						createSet(new String[] { "Hallo",
								"du" }), createSet(new String[] {}),
						(false) },
				new Object[] { createSet(new String[] {}),
						createSet(new String[] {}), (true) },
				new Object[] {
						createSet(new String[] {}),
						createSet(new String[] { "Hallo",
								"Du" }), (false) },
				new Object[] {
						createSet(new String[] { "Du", "Du", "Hallo" }),
						createSet(new String[] { "Hallo",
								"Du" }), (false) }
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
	 * Tests if the AllOfAll (higher order) function works properly.
	 * 
	 * @param set1 The first set with strings in it.
	 * @param set2 The second set with strings in it.
	 * @param expectedResult The expected result of the comparison.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="functionTest")
	public void testFunction(List<String> set1, List<String> set2, Boolean expectedResult) throws Exception {
		assertEquals(function.handle(compareFunction, set1, set2), expectedResult);
	}
}