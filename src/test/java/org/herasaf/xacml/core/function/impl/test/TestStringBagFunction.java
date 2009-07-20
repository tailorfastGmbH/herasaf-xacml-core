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

package org.herasaf.xacml.core.function.impl.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.bagFunctions.StringBagFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestStringBagFunction {
	private Function stringBagFunction;

	@BeforeMethod
	public void init() {
		stringBagFunction = new StringBagFunction();
	}

	@SuppressWarnings("unchecked")
	@Test(enabled = true)
	public void testHandleStringsTwoArgs() throws Exception {
		String[] expectedResult = { "George", "John" };

		List<String> actualResult = (List<String>) stringBagFunction.handle(
				"George", "John");

		assertEquals(actualResult.size(), expectedResult.length);
		assertTrue(actualResult.containsAll(Arrays.asList(expectedResult)));
	}

	@SuppressWarnings("unchecked")
	@Test(enabled = true)
	public void testHandleStringsWithoutArgs() throws Exception {
		String[] expectedResult = {};

		List<String> actualResult = (List<String>) stringBagFunction.handle();

		assertEquals(actualResult.size(), expectedResult.length);
		assertTrue(actualResult.containsAll(Arrays.asList(expectedResult)));
	}

	@Test(enabled = true)
	public void testID() {
		assertEquals(stringBagFunction.toString(),
				"urn:oasis:names:tc:xacml:1.0:function:string-bag");
	}
}
