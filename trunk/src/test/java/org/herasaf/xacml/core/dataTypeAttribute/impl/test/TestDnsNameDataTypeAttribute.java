/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.dataTypeAttribute.impl.test;

import static org.testng.Assert.assertEquals;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DnsNameDataTypeAttribute;
import org.herasaf.xacml.core.types.DnsName;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link DnsNameDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestDnsNameDataTypeAttribute {
	private DnsNameDataTypeAttribute dataType;

	/**
	 * Creates positive test cases for the test.
	 * 
	 * @return The positive test cases.
	 */
	@DataProvider(name = "positiveCases")
	public Object[][] createPositiveCases() {
		return new Object[][] {
				new Object[] { "mydomain.ch" },
				new Object[] { "ch" },
				new Object[] { "sub1.mydomain.ch" },
				new Object[] { "sub1.sub2.subdomain3.mydomain.ch" },
				new Object[] { "*.mydomain.ch" },
				new Object[] { "*.sub1.sub2.subdomain3.mydomain.ch" },
				new Object[] { "*" },
				new Object[] { "*.mydomain.ch:30-" },
				new Object[] { 
						"*.sub1.sub2.subdomain3.mydomain.ch:30-" },
				new Object[] { "*:30-" },
				new Object[] { "*.mydomain.ch:-30" },
				new Object[] { 
						"*.sub1.sub2.subdomain3.mydomain.ch:-30" },
				new Object[] { "*:-30" },
				new Object[] { "*.mydomain.ch:12-30" },
				new Object[] { 
						"*.sub1.sub2.subdomain3.mydomain.ch:12-30" },
				new Object[] { "*:12-30" },
				new Object[] { "*.mydomain.ch:30-" },
				new Object[] { "*.mydomain.ch:-30" },
				new Object[] { "*.mydomain.ch:12-30" } };
	}

	/**
	 * Creates negative test cases for the test.
	 * 
	 * @return The negative test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] {
				new Object[] { "" },
				new Object[] { "sub1.*.domain.ch" },
				new Object[] { "mydomain.ch:65536" },
				new Object[] { "mydomain.ch:0" },
				new Object[] { "*.mydomain.ch:0" },
				new Object[] { "mydomain.ch:*" },
				new Object[] { "mydomain.ch:12-3" },
				new Object[] { "mydomain.ch:0-12" },
				new Object[] { "mydomain.ch:12-0" },
				new Object[] { "my domain ch" },
				new Object[] { 
						"*.sub1.sub2.subdomain3.mydomain .ch:-30" },
				new Object[] { "sub1.*.domain.ch:30" },
				new Object[] { "sub1.*.domain.ch:-30" },
				new Object[] { "sub1.*.domain.ch:30-" },
				new Object[] { "sub1.*.domain.ch:12-30" },
				new Object[] { "sub1.*.domain.ch:30-12" } };
	}

	/**
	 * Initializes a new {@link DnsNameDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new DnsNameDataTypeAttribute();
	}

	/**
	 * Tests the positive data.
	 * 
	 * @param input The positive data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveCases")
	public void testInput(String input) throws Exception {
		assertEquals(dataType.convertTo(input), new DnsName(input));
	}

	/**
	 * Tests the negative data.
	 * 
	 * @param input The negative data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { SyntaxException.class })
	public void testInputtrueWrongSpelled(String input) throws Exception {
		dataType.convertTo(input);
	}

	/**
	 * Tests if the {@link DnsNameDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"urn:oasis:names:tc:xacml:2.0:data-type:dnsName");
	}
}