/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.function.impl.specialMatchFunctions.test;

import static org.testng.Assert.assertEquals;

import javax.security.auth.x500.X500Principal;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.specialMatchFunctions.X500NameMatchFunction;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the X500NameMatch function works properly.
 * 
 * @author Florian Huonder
 */
public class TestX500NameMatchFunction {
	private Function function;

	/**
	 * Initializes the function. 
	 */
	@BeforeTest
	public void beforeTest() {
		this.function = new X500NameMatchFunction();
	}

	/**
	 * Creates various test cases.
	 * @return The created test cases.
	 */
	@DataProvider(name = "functionTest")
	public Object[][] createArgs() {
		return new Object[][] {
				new Object[] {
						new X500Principal("CN=Steve Kille,O=Isode Limited,C=GB"),
						new X500Principal("CN=Steve Kille,O=Isode Limited,C=GB"),
						true },
				new Object[] {
						new X500Principal("CN=Steve Kille"),
						new X500Principal("CN=Steve Kille,O=Isode Limited,C=GB"),
						true },
				new Object[] {
						new X500Principal("cn=Steve Kille"),
						new X500Principal("CN=Steve Kille,O=Isode Limited,C=GB"),
						true },
				new Object[] {
						new X500Principal(
								"CN=Steve Kille,O=Isode Limited,C=GB,OU=Sales+CN=J. Smith"),
						new X500Principal("CN=Steve Kille,O=Isode Limited,C=GB"),
						false },
				new Object[] {
						new X500Principal("CN=Steve Kille,O=Isode Limited,C=GB"),
						new X500Principal(
								"OU=Sales+CN=J. Smith,O=Widget Inc.,C=US"),
						false }, };
	}

	/**
	 * Tests if the {@link X500NameMatchFunction} works properly.
	 * 
	 * @param principal1 The first X500Name principal argument.
	 * @param principal2 The second X500Name principal argument.
	 * @param expectedResult The expected result.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "functionTest")
	public void testFunction(X500Principal principal1,
			X500Principal principal2, Boolean expectedResult) throws Exception {
		assertEquals(function.handle(principal1, principal2), expectedResult);
	}
}