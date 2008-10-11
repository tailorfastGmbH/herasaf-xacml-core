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
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AnyOfFunction;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestAnyOfFunction {

	private Function function;
	private Function compareFunction;

	@BeforeTest
	public void beforeTest() {
		this.function = new AnyOfFunction();
		this.compareFunction = new StringEqualFunction();
	}

	@DataProvider(name = "functionTest")
	public Object[][] createArgs() {
		return new Object[][] {
				new Object[] { "Hallo",
						createSet(new String[] { "Hallo" }),
						(true) },
				new Object[] {
						"Hallo",
						createSet(new String[] { "Hallo", "Test" }),
						(true) },
				new Object[] {
						"Du",
						createSet(new String[] { "Hallo", "Test" }),
						(false) },
				new Object[] {
						"hallo",
						createSet(new String[] {}),
						(false) },
		};
	}

	private List<String> createSet(String[] strings) {
		List<String> set = new ArrayList<String>();
		for (String str : strings){
			set.add(str);
		}
		return set;
	}


	@Test(dataProvider="functionTest")
	public void testFunction(String string, List<String> set2, Boolean expectedResult) throws Exception {
		assertEquals(function.handle(compareFunction, string, set2), expectedResult);
	}
}
