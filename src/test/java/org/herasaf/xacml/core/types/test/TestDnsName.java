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

import org.herasaf.xacml.core.types.DnsName;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.fail;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class TestDnsName {
	@DataProvider (name = "positiveCases")
	public Object[][] createPositiveCases(){
		return new Object[][] {
				new Object[]{"mydomain.ch", "mydomain.ch"},
				new Object[]{"ch", "ch"},
				new Object[]{"sub1.mydomain.ch", "sub1.mydomain.ch"},
				new Object[]{"sub1.sub2.subdomain3.mydomain.ch", "sub1.sub2.subdomain3.mydomain.ch"},
				new Object[]{"*.mydomain.ch", "*.mydomain.ch"},
				new Object[]{"*.sub1.sub2.subdomain3.mydomain.ch", "*.sub1.sub2.subdomain3.mydomain.ch"},
				new Object[]{"*", "*"},
				new Object[]{"*.mydomain.ch:30-", "*.mydomain.ch:30-65535"},
				new Object[]{"*.sub1.sub2.subdomain3.mydomain.ch:30-", "*.sub1.sub2.subdomain3.mydomain.ch:30-65535"},
				new Object[]{"*:30-", "*:30-65535"},
				new Object[]{"*.mydomain.ch:-30", "*.mydomain.ch:1-30"},
				new Object[]{"*.sub1.sub2.subdomain3.mydomain.ch:-30", "*.sub1.sub2.subdomain3.mydomain.ch:1-30"},
				new Object[]{"*:-30", "*:1-30"},
				new Object[]{"*.mydomain.ch:12-30", "*.mydomain.ch:12-30"},
				new Object[]{"*.sub1.sub2.subdomain3.mydomain.ch:12-30", "*.sub1.sub2.subdomain3.mydomain.ch:12-30"},
				new Object[]{"*:12-30", "*:12-30"},
				new Object[]{"*.mydomain.ch:30-", "*.mydomain.ch:30-65535"},
				new Object[]{"*.mydomain.ch:-30", "*.mydomain.ch:1-30"},
				new Object[]{"*.mydomain.ch:12-30", "*.mydomain.ch:12-30"}
		};
	}

	@DataProvider (name = "negativeCases")
	public Object[][] createNegativeCases(){
		return new Object[][] {
				new Object[]{ ""},
				new Object[]{ "sub1.*.domain.ch"},
				new Object[]{ "mydomain.ch:65536"},
				new Object[]{ "mydomain.ch:0"},
				new Object[]{ "*.mydomain.ch:0"},
				new Object[]{ "mydomain.ch:*"},
				new Object[]{ "mydomain.ch:12-3"},
				new Object[]{ "mydomain.ch:0-12"},
				new Object[]{ "mydomain.ch:12-0"},
				new Object[]{ "my domain ch"},
				new Object[]{ "*.sub1.sub2.subdomain3.mydomain .ch:-30"},
				new Object[]{ "sub1.*.domain.ch:30"},
				new Object[]{ "sub1.*.domain.ch:-30"},
				new Object[]{ "sub1.*.domain.ch:30-"},
				new Object[]{ "sub1.*.domain.ch:12-30"},
				new Object[]{ "sub1.*.domain.ch:30-12"}
		};
	}
	
	@Test (dataProvider = "positiveCases")
	public void testPositives(String input, String output) throws Exception {
		DnsName dnsName = new DnsName(input);
		assertEquals(dnsName.toString(), output);
	}
	
	@Test (dataProvider = "negativeCases")
	public void testNegatives(String negativeCase) throws Exception {
		try {
			new DnsName(negativeCase);
			fail("It must not be possible to have the following port range: " + negativeCase);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}