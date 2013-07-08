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

package org.herasaf.xacml.core.types.test;

import static org.testng.Assert.assertEquals;

import org.herasaf.xacml.core.types.PortRange;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *  Tests the {@link PortRange} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestPortRange {
	
	/**
	 * Creates positive test cases. 
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "positiveCases")
	public Object[][] createPositiveCases(){
		return new Object[][] {
				new Object[]{ "-1", "1"},
				new Object[]{ "65535-", "65535"},
				new Object[]{ "344", "344"},
		};
	}

	/**
	 * Creates positive test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "negativeCases")
	public Object[][] createNegativeCases(){
		return new Object[][] {
				new Object[]{ "--355"},
				new Object[]{ "65536"},
				new Object[]{ "12-34-"},
				new Object[]{ "-1-300"},
		};
	}
	
	/**
	 * Test if the {@link PortRange} objects are properly created.
	 * 
	 * @param input The port range in its String representation.
	 * @param expected The expected String on {@link PortRange#toString()}.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="positiveCases", enabled = true)
	public void testAllowed(String input, String expected) throws Exception {
			assertEquals(new PortRange(input).toString(), expected);
	}
	
	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal port range representations.

	 * @param input The illegal port range strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class}, enabled = true)
	public void testNotAllowed(String input) throws Exception {
		new PortRange(input);
	}
}