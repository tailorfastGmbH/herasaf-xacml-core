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

import org.herasaf.xacml.core.types.IPv4Address;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *  Tests the {@link IPv4Address} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestIPv4Address {
	
	/**
	 * Creates positive test cases. 
	 * 
	 * @return The test cases.
	 */
	@DataProvider (name = "positiveCases")
	public Object[][] createPositiveCases(){
		return new Object[][] {
				new Object[]{"192.168.0.1", "192.168.0.1"},
				new Object[]{"192.168.0.1/255.255.255.0", "192.168.0.1/255.255.255.0"},
				new Object[]{"192.168.0.1/255.255.255.0:1", "192.168.0.1/255.255.255.0:1"},
				new Object[]{"192.168.0.1:1-", "192.168.0.1:1-65535"},
				new Object[]{"192.168.0.1:65535", "192.168.0.1:65535"},
				new Object[]{"192.168.0.1:-65535", "192.168.0.1:1-65535"},
				new Object[]{"192.168.0.1:-3", "192.168.0.1:1-3"},
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
				new Object[]{ "256.168.0.1"},
				new Object[]{"192.168.0.1a:34"},
				new Object[]{ "192.168.0.1:12/255.255.255.0"},
				new Object[]{ "192.168.0.1/255.256.0.0"},
				new Object[]{ "193.168.0.1:0"},
				new Object[]{ "193.168.0.1:65536"},
		};
	}
	
	/**
	 * Test if the {@link IPv4Address} objects are properly created.
	 * One byte.
	 * 
	 * @param input The ipv4 in its String representation.
	 * @param expected The expected String on {@link IPv4Address#toString()}.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="positiveCases", enabled = true)
	public void testAllowed(String input, String expected) throws Exception {
		assertEquals(new IPv4Address(input).toString(), expected);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal ipv4 representations.
	 * Positive values.
	 * @param input The illegal ipv4 strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class}, enabled = true)
	public void testNotAllowed(String input) throws Exception {
			new IPv4Address(input);
	}
}