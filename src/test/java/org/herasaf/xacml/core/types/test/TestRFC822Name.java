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

import org.herasaf.xacml.core.types.RFC822Name;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *  Tests the {@link RFC822Name} basic data type.
 * 
 * @author Florian Huonder
 */
public class TestRFC822Name {
	
	/**
	 * Creates positive test cases. 
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "positiveCases")
	public Object[][] createPositiveCases() {
		return new Object[][] { new Object[] { "@hallo.ch" },
				new Object[] { "Hallo@blabla.ch" },
				new Object[] { "Hans.Meier@hallo.velo.ch" },
				new Object[] { "HansMeier@192.169.0.1" }, };
	}

	/**
	 * Creates positive test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] { "hallo.ch" },
				new Object[] { "hallo@" },
				new Object[] { "hallo" },
				new Object[] { "Hallo@ch.du@ja" }, };
	}

	/**
	 * Creates match test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "matchCases")
	public Object[][] createMatchCases() {
		return new Object[][] {
				new Object[] { "@hallo.ch", "hallo.ch",
						true },
				new Object[] { "@hallo.ch", "HALLO.ch",
						true },
				new Object[] { "@hallo.ch",
						"@hallo.ch", true },
				new Object[] { "@hallo.ch",
						"@HALLO.CH", true },
				new Object[] { "@hallo.ch", ".ch",
						true },
				new Object[] { "@hallo.ch", ".CH",
						true },
				new Object[] { "@hallo.ch", "test.ch",
						false },
				new Object[] { "@hallo.ch", ".de",
						false },
				new Object[] { "test@hallo.ch",
						"test@hallo.ch", true },
				new Object[] { "test@hallo.ch",
						"hallo.ch", true },
				new Object[] { "test@hallo.ch", ".ch",
						true },
				new Object[] { "test@hallo.ch",
						"Test@test.ch", false },
				new Object[] { "test@hallo.ch",
						"@test.ch", false }, };
	}

	/**
	 * Test if the {@link RFC822Name} objects are properly created.
	 * 
	 * @param input The rfc822Name in its String representation.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "positiveCases", enabled = true)
	public void testAllowed(String input) throws Exception {
		assertEquals(new RFC822Name(input).toString(), input);
	}

	/**
	 * Tests if an {@link IllegalArgumentException} is thrown on illegal rfc822Name representations.

	 * @param input The illegal rfc822Name strings.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { IllegalArgumentException.class }, enabled = true)
	public void testNotAllowed(String input) throws Exception {
		new RFC822Name(input);
	}

	/**
	 * Tests the match of {@link RFC822Name}.

	 * @param rfcName The first input.
	 * @param matchValue The second argument.
	 * @param result The expected result.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "matchCases", enabled = true)
	public void testMatchCases(String rfcName, String matchValue, Boolean result)
			throws Exception {
		assertEquals( (Boolean)(new RFC822Name(rfcName)).match(matchValue), result);
	}
}